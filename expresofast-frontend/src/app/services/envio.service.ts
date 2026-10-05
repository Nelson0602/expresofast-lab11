import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { Envio, EnvioRegistroPayload } from '../models/envio.model';

// Servicio que se comunica con la API REST de envíos
@Injectable({
  providedIn: 'root'
})
export class EnvioService {
  private http = inject(HttpClient);
  private baseUrl = environment.API_URL + 'envios';

  obtenerEnvios(): Observable<Envio[]> {
    return this.http.get<Envio[]>(this.baseUrl);
  }

  obtenerPorRastreo(codigo: string): Observable<Envio> {
    return this.http.get<Envio>(`${this.baseUrl}/rastreo/${codigo}`);
  }

  // Envía el envío completo, con su lista de paquetes, en un solo POST
  crearEnvio(payload: EnvioRegistroPayload): Observable<Envio> {
    return this.http.post<Envio>(this.baseUrl, payload);
  }

  // Envía el nuevo estado al backend con PATCH
  actualizarEstado(id: number, nuevoEstado: string): Observable<Envio> {
    return this.http.patch<Envio>(`${this.baseUrl}/${id}/estado`, { estado: nuevoEstado });
  }

  // Pregunta al backend si un número de rastreo ya existe
  existeTracking(trackingNumber: string): Observable<boolean> {
    return this.http
      .get<{ exists: boolean }>(`${this.baseUrl}/check-tracking/${encodeURIComponent(trackingNumber)}`)
      .pipe(map((response) => response.exists));
  }
}
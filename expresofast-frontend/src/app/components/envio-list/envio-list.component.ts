import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ENVIO_STATES, Envio } from '../../models/envio.model';
import { EnvioService } from '../../services/envio.service';

// Muestra la tabla de envíos y permite cambiar el estado de cada uno
@Component({
  selector: 'app-envio-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './envio-list.component.html',
  styleUrl: './envio-list.component.css'
})
export class EnvioListComponent implements OnInit {
  private envioService = inject(EnvioService);

  shipments: Envio[] = [];
  states: string[] = ENVIO_STATES;
  loading = false;
  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.loadShipments();
  }

  // Pide la lista de envíos al backend
  loadShipments(): void {
    this.loading = true;
    this.errorMessage = '';
    this.envioService.obtenerEnvios().subscribe({
      next: (data) => {
        this.shipments = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar la lista. Verifique que el backend esté encendido.';
        this.loading = false;
      }
    });
  }

  // Envía el nuevo estado al backend y actualiza la fila si todo sale bien
  changeStatus(shipment: Envio, newStatus: string): void {
    this.successMessage = '';
    this.envioService.actualizarEstado(shipment.id, newStatus).subscribe({
      next: (updated) => {
        shipment.estado = updated.estado;
        this.successMessage = `El envío ${updated.codigoRastreo} ahora está ${updated.estado}.`;
      },
      error: () => {
        this.loadShipments();
        this.errorMessage = 'No se pudo actualizar el estado del envío.';
      }
    });
  }
}
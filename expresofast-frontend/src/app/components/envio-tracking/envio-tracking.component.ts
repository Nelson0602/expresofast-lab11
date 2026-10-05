import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Envio } from '../../models/envio.model';
import { EnvioService } from '../../services/envio.service';

// Busca un envío por su código de rastreo y muestra su avance
@Component({
  selector: 'app-envio-tracking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './envio-tracking.component.html',
  styleUrl: './envio-tracking.component.css'
})
export class EnvioTrackingComponent {
  private envioService = inject(EnvioService);

  trackingCode = '';
  shipment: Envio | null = null;
  errorMessage = '';
  loading = false;
  steps: string[] = ['PENDIENTE', 'EN_TRANSITO', 'ENTREGADO'];

  // Consulta el envío en el backend usando el código escrito
  search(): void {
    const code = this.trackingCode.trim().toUpperCase();
    this.shipment = null;
    this.errorMessage = '';

    if (code === '') {
      this.errorMessage = 'Escriba un código de rastreo.';
      return;
    }

    this.loading = true;
    this.envioService.obtenerPorRastreo(code).subscribe({
      next: (data) => {
        this.shipment = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.status === 404
          ? `No se encontró ningún envío con el código ${code}.`
          : 'No se pudo consultar el envío. Verifique que el backend esté encendido.';
        this.loading = false;
      }
    });
  }

  // Calcula el porcentaje de la barra según el estado actual
  getProgress(): number {
    if (!this.shipment) {
      return 0;
    }
    const index = this.steps.indexOf(this.shipment.estado);
    if (index < 0) {
      return 0;
    }
    return Math.round(((index + 1) / this.steps.length) * 100);
  }

  // Indica si un paso ya fue alcanzado por el envío
  isStepDone(step: string): boolean {
    if (!this.shipment) {
      return false;
    }
    const current = this.steps.indexOf(this.shipment.estado);
    return current >= this.steps.indexOf(step) && current >= 0;
  }
}
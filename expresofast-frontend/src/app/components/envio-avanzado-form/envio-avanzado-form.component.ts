import { Component, inject } from '@angular/core';
import { AbstractControl, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EnvioRegistroPayload } from '../../models/envio.model';
import { EnvioService } from '../../services/envio.service';
import { deliveryAfterDispatchValidator, trackingAvailableValidator } from '../../validators/envio.validators';

// Formulario reactivo y tipado para registrar un envío con varios paquetes
@Component({
  selector: 'app-envio-avanzado-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './envio-avanzado-form.component.html',
  styleUrl: './envio-avanzado-form.component.css'
})
export class EnvioAvanzadoFormComponent {
  private envioService = inject(EnvioService);
  private fb = inject(NonNullableFormBuilder);

  saving = false;
  createdCode = '';
  errorMessage = '';

  // Formulario tipado: cada control tiene su tipo y TypeScript lo revisa al compilar
  form = this.fb.group(
    {
      numeroTracking: this.fb.control('', {
        validators: [Validators.required, Validators.maxLength(30), Validators.pattern(/^[A-Za-z0-9-]+$/)],
        asyncValidators: [trackingAvailableValidator(this.envioService)]
      }),
      destinatario: this.fb.control('', [Validators.required, Validators.maxLength(120)]),
      direccionDestino: this.fb.control('', [Validators.required, Validators.maxLength(250)]),
      montoFlete: this.fb.control<number | null>(null, [Validators.required, Validators.min(0.01)]),
      fechaDespacho: this.fb.control('', Validators.required),
      fechaEntregaEstimada: this.fb.control('', Validators.required),
      paquetes: this.fb.array([this.createPackageGroup()], Validators.minLength(1))
    },
    { validators: [deliveryAfterDispatchValidator()] }
  );

  // Atajo para usar el FormArray en la plantilla
  get paquetes() {
    return this.form.controls.paquetes;
  }

  // Crea el grupo de campos de un paquete nuevo
  private createPackageGroup() {
    return this.fb.group({
      descripcion: this.fb.control('', [Validators.required, Validators.maxLength(255)]),
      pesoKg: this.fb.control<number | null>(null, [
        Validators.required,
        Validators.min(0.01),
        Validators.max(999.99)
      ])
    });
  }

  // Agrega un bloque de paquete vacío al final del arreglo
  addPackage(): void {
    this.paquetes.push(this.createPackageGroup());
  }

  // Quita un paquete, pero siempre deja al menos uno
  removePackage(index: number): void {
    if (this.paquetes.length > 1) {
      this.paquetes.removeAt(index);
    }
  }

  // Indica si un campo debe mostrar su error en pantalla
  showError(control: AbstractControl): boolean {
    return control.invalid && (control.touched || control.dirty);
  }

  // Revisa el formulario y manda el envío completo al backend
  save(): void {
    this.createdCode = '';
    this.errorMessage = '';

    if (this.form.invalid || this.form.pending) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    const payload: EnvioRegistroPayload = {
      numeroTracking: raw.numeroTracking.trim().toUpperCase(),
      destinatario: raw.destinatario.trim(),
      direccionDestino: raw.direccionDestino.trim(),
      montoFlete: raw.montoFlete as number,
      fechaDespacho: raw.fechaDespacho,
      fechaEntregaEstimada: raw.fechaEntregaEstimada,
      paquetes: raw.paquetes.map((item) => ({
        descripcion: item.descripcion.trim(),
        pesoKg: item.pesoKg as number
      }))
    };

    this.saving = true;
    this.envioService.crearEnvio(payload).subscribe({
      next: (created) => {
        this.createdCode = created.codigoRastreo;
        this.saving = false;
        this.resetForm();
      },
      error: (error) => {
        if (error.status === 409) {
          this.errorMessage = 'Este número de rastreo ya está en uso';
        } else if (error.status === 400) {
          this.errorMessage = 'El backend rechazó los datos. Revise los campos del formulario.';
        } else {
          this.errorMessage = 'No se pudo registrar el envío. Verifique que el backend esté encendido.';
        }
        this.saving = false;
      }
    });
  }

  // Deja el formulario limpio y con un solo paquete vacío
  private resetForm(): void {
    this.paquetes.clear();
    this.paquetes.push(this.createPackageGroup());
    this.form.reset();
  }
}
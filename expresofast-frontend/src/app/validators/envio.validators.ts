import { AbstractControl, AsyncValidatorFn, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Observable, catchError, map, of, switchMap, timer } from 'rxjs';
import { EnvioService } from '../services/envio.service';

// Validador síncrono del FormGroup: la entrega debe ser posterior al despacho
export function deliveryAfterDispatchValidator(): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
        const dispatchValue = group.get('fechaDespacho')?.value;
        const deliveryValue = group.get('fechaEntregaEstimada')?.value;

        // Si falta alguna fecha, el required de cada campo se encarga del error
        if (!dispatchValue || !deliveryValue) {
            return null;
        }

        const dispatchTime = new Date(dispatchValue).getTime();
        const deliveryTime = new Date(deliveryValue).getTime();

        return deliveryTime > dispatchTime ? null : { fechasInvalidas: true };
    };
}

// Validador asíncrono: consulta al backend si el número de rastreo ya está tomado
export function trackingAvailableValidator(envioService: EnvioService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
        const trackingNumber = String(control.value ?? '').trim();

        if (trackingNumber === '') {
            return of(null);
        }

        // La espera de 400 ms evita consultar al backend con cada tecla
        return timer(400).pipe(
            switchMap(() => envioService.existeTracking(trackingNumber)),
            map((exists) => (exists ? { trackingTomado: true } : null)),
            catchError(() => of(null))
        );
    };
}
// Paquete tal como lo devuelve el backend
export interface Paquete {
    id: number;
    descripcion: string;
    pesoKg: number;
}

// Envío tal como lo devuelve el backend
export interface Envio {
    id: number;
    codigoRastreo: string;
    destinatario: string;
    direccionDestino: string;
    montoFlete: number;
    estado: string;
    fechaCreacion: string;
    fechaDespacho: string | null;
    fechaEntregaEstimada: string | null;
    paquetes: Paquete[];
}

// Datos de un paquete que se envían al backend
export interface PaquetePayload {
    descripcion: string;
    pesoKg: number;
}

// Datos que se envían al backend para registrar un envío con sus paquetes
export interface EnvioRegistroPayload {
    numeroTracking: string;
    destinatario: string;
    direccionDestino: string;
    montoFlete: number;
    fechaDespacho: string;
    fechaEntregaEstimada: string;
    paquetes: PaquetePayload[];
}

// Estados permitidos para un envío
export const ENVIO_STATES: string[] = ['PENDIENTE', 'EN_TRANSITO', 'ENTREGADO', 'CANCELADO'];
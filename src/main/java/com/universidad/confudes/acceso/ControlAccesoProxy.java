package com.universidad.confudes.acceso;

import com.universidad.confudes.certificados.ServicioCertificados;
import org.springframework.stereotype.Service;

/**
 * Proxy de protección: controla el acceso a la emisión de
 * certificados verificando el rol del solicitante antes de
 * delegar en el servicio real. Si el permiso no se concede, la
 * llamada real nunca ocurre — a diferencia de un Decorator, que
 * siempre deja pasar la llamada y solo añade comportamiento
 * alrededor de ella.
 */
@Service
public class ControlAccesoProxy implements ServicioCertificados {

    private final ServicioCertificados servicioReal;
    private final ServicioAutenticacion servicioAutenticacion;

    public ControlAccesoProxy(ServicioCertificados servicioReal,
                                ServicioAutenticacion servicioAutenticacion) {
        this.servicioReal = servicioReal;
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @Override
    public byte[] emitir(String eventoId, String participanteId) {
        if (!servicioAutenticacion.tienePermiso(participanteId, "STAFF")) {
            return null; // acceso denegado, el servicio real nunca se invoca
        }
        return servicioReal.emitir(eventoId, participanteId);
    }
}
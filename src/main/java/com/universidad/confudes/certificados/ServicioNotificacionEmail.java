package com.universidad.confudes.certificados;

import org.springframework.stereotype.Service;

// Servicio existente — no modificar.
@Service
public class ServicioNotificacionEmail {
    public void notificarCertificadoListo(String participanteId, String eventoId) {
        System.out.println("Notificando a " + participanteId + " sobre certificado del evento " + eventoId);
    }
}
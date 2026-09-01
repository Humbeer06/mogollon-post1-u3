package com.universidad.confudes.certificados;

import org.springframework.stereotype.Service;

// Servicio existente — no modificar.
@Service
public class ServicioPlantillaPDF {
    public byte[] generarPDF(String participanteId, String eventoId) {
        String contenido = "Certificado para " + participanteId + " - Evento " + eventoId;
        return contenido.getBytes();
    }
}
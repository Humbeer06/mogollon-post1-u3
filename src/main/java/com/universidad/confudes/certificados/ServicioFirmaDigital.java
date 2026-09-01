package com.universidad.confudes.certificados;

import org.springframework.stereotype.Service;

// Servicio existente — no modificar.
@Service
public class ServicioFirmaDigital {
    public byte[] firmar(byte[] documento) {
        byte[] firmado = new byte[documento.length + 6];
        System.arraycopy(documento, 0, firmado, 0, documento.length);
        System.arraycopy("-FIRMA".getBytes(), 0, firmado, documento.length, 6);
        return firmado;
    }
}
package com.universidad.confudes.certificados;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

// Reducido a un único colaborador: CertificacionFacade.
@RestController
@RequestMapping("/api/certificados")
public class ControladorCertificados {

    private final CertificacionFacade certificacionFacade;

    public ControladorCertificados(CertificacionFacade certificacionFacade) {
        this.certificacionFacade = certificacionFacade;
    }

    @PostMapping("/{eventoId}/{participanteId}")
    public ResponseEntity<byte[]> emitirCertificado(@PathVariable String eventoId,
                                                       @PathVariable String participanteId) {
        byte[] pdfFirmado = certificacionFacade.emitir(eventoId, participanteId);
        return pdfFirmado != null ? ResponseEntity.ok(pdfFirmado) : ResponseEntity.badRequest().build();
    }
}
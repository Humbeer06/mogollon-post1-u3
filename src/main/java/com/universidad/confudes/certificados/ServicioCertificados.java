package com.universidad.confudes.certificados;

/**
 * Contrato común entre el Facade base y sus decoradores. Permite
 * que ControladorCertificados dependa de esta abstracción sin
 * saber si está recibiendo el CertificacionFacade original o
 * una combinación de mejoras envolviéndolo.
 */
public interface ServicioCertificados {
    byte[] emitir(String eventoId, String participanteId);
}
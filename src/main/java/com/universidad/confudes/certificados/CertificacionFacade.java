package com.universidad.confudes.certificados;

import org.springframework.stereotype.Service;

/**
 * Facade: reduce a un único colaborador la orquestación de los
 * cuatro servicios que participan en la emisión de un certificado
 * (reserva de sala, generación de PDF, firma digital y
 * notificación). El cliente (ControladorCertificados) ya no
 * necesita conocer ni coordinar directamente estos cuatro
 * servicios, solo invoca emitir().
 */
@Service
public class CertificacionFacade implements ServicioCertificados {
    private final ServicioReservaSala servicioReservaSala;
    private final ServicioPlantillaPDF servicioPlantillaPDF;
    private final ServicioFirmaDigital servicioFirmaDigital;
    private final ServicioNotificacionEmail servicioNotificacionEmail;

    public CertificacionFacade(ServicioReservaSala servicioReservaSala,
                                 ServicioPlantillaPDF servicioPlantillaPDF,
                                 ServicioFirmaDigital servicioFirmaDigital,
                                 ServicioNotificacionEmail servicioNotificacionEmail) {
        this.servicioReservaSala = servicioReservaSala;
        this.servicioPlantillaPDF = servicioPlantillaPDF;
        this.servicioFirmaDigital = servicioFirmaDigital;
        this.servicioNotificacionEmail = servicioNotificacionEmail;
    }

    /**
     * @return el PDF firmado, o null si no hay reserva confirmada
     *         para el evento.
     */
    @Override
    public byte[] emitir(String eventoId, String participanteId) {
        if (!servicioReservaSala.tieneReservaConfirmada(eventoId)) {
            return null;
        }
        byte[] pdf = servicioPlantillaPDF.generarPDF(participanteId, eventoId);
        byte[] pdfFirmado = servicioFirmaDigital.firmar(pdf);
        servicioNotificacionEmail.notificarCertificadoListo(participanteId, eventoId);
        return pdfFirmado;
    }
}
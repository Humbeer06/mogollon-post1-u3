package com.universidad.confudes.certificados;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CertificacionFacadeTest {

    @Test
    void emiteCertificadoFirmadoCuandoHayReservaConfirmada() {
        CertificacionFacade facade = new CertificacionFacade(
            new ServicioReservaSala(),
            new ServicioPlantillaPDF(),
            new ServicioFirmaDigital(),
            new ServicioNotificacionEmail()
        );

        byte[] resultado = facade.emitir("EVT-100", "PART-200");

        assertNotNull(resultado);
        String contenido = new String(resultado);
        assertTrue(contenido.endsWith("-FIRMA"));
    }
}
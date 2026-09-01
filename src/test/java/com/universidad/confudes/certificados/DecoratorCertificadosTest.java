package com.universidad.confudes.certificados;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorCertificadosTest {

    private ServicioCertificados crearFacadeBase() {
        return new CertificacionFacade(
            new ServicioReservaSala(),
            new ServicioPlantillaPDF(),
            new ServicioFirmaDigital(),
            new ServicioNotificacionEmail()
        );
    }

    @Test
    void aplicaUnaSolaMejora() {
        ServicioCertificados servicio = new MarcaAguaDecorator(crearFacadeBase());
        String resultado = new String(servicio.emitir("EVT-1", "PART-1"));
        assertTrue(resultado.endsWith("-MARCA_AGUA"));
    }

    @Test
    void combinaTresMejorasEnCualquierOrdenSinClasesNuevas() {
        ServicioCertificados servicio = new TraduccionDecorator(
            new CodigoQRDecorator(
                new MarcaAguaDecorator(
                    crearFacadeBase()
                )
            ), "EN"
        );

        String resultado = new String(servicio.emitir("EVT-2", "PART-2"));

        // Verifica que las tres mejoras se aplicaron, en el orden
        // en que se apilaron los decoradores (de adentro hacia afuera).
        assertTrue(resultado.contains("-FIRMA"));
        assertTrue(resultado.contains("-MARCA_AGUA"));
        assertTrue(resultado.contains("-QR_VERIFICACION"));
        assertTrue(resultado.endsWith("-TRADUCIDO_EN"));
    }

    @Test
    void combinaSoloDosMejorasSinNecesitarClaseEspecifica() {
        // Demuestra que no existe una clase "CertificadoConQRYTraduccion":
        // la misma combinatoria de decoradores basta.
        ServicioCertificados servicio = new TraduccionDecorator(
            new CodigoQRDecorator(crearFacadeBase()), "FR"
        );

        String resultado = new String(servicio.emitir("EVT-3", "PART-3"));

        assertFalse(resultado.contains("-MARCA_AGUA"));
        assertTrue(resultado.contains("-QR_VERIFICACION"));
        assertTrue(resultado.endsWith("-TRADUCIDO_FR"));
    }
}
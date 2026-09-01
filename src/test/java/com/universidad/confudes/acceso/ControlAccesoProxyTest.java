package com.universidad.confudes.acceso;

import com.universidad.confudes.certificados.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControlAccesoProxyTest {

    private ServicioCertificados crearFacadeBase() {
        return new CertificacionFacade(
            new ServicioReservaSala(),
            new ServicioPlantillaPDF(),
            new ServicioFirmaDigital(),
            new ServicioNotificacionEmail()
        );
    }

    @Test
    void permiteEmisionCuandoElUsuarioTieneRolStaff() {
        ControlAccesoProxy proxy = new ControlAccesoProxy(crearFacadeBase(), new ServicioAutenticacion());

        byte[] resultado = proxy.emitir("EVT-1", "ADMIN-001");

        assertNotNull(resultado);
    }

    @Test
    void bloqueaLaLlamadaRealCuandoElUsuarioNoTienePermiso() {
        ContadorLlamadas contador = new ContadorLlamadas(crearFacadeBase());
        ControlAccesoProxy proxy = new ControlAccesoProxy(contador, new ServicioAutenticacion());

        byte[] resultado = proxy.emitir("EVT-1", "PART-999");

        assertNull(resultado);
        assertEquals(0, contador.getVecesInvocado());
    }

    private static class ContadorLlamadas implements ServicioCertificados {
        private final ServicioCertificados envuelto;
        private int vecesInvocado = 0;

        ContadorLlamadas(ServicioCertificados envuelto) {
            this.envuelto = envuelto;
        }

        @Override
        public byte[] emitir(String eventoId, String participanteId) {
            vecesInvocado++;
            return envuelto.emitir(eventoId, participanteId);
        }

        int getVecesInvocado() {
            return vecesInvocado;
        }
    }
}
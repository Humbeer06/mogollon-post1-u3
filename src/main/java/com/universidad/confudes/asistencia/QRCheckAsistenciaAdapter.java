package com.universidad.confudes.asistencia;

import com.universidad.confudes.externo.qrcheck.QRCheckClient;
import com.universidad.confudes.externo.qrcheck.QRCheckRequest;
import com.universidad.confudes.externo.qrcheck.QRCheckResponse;
import org.springframework.stereotype.Service;

/**
 * Adapter: traduce el contrato del proveedor externo QRCheckAPI
 * (QRCheckClient) al contrato interno ServicioAsistencia que ya
 * usa ControladorCheckIn. Aísla en un único lugar la conversión de
 * tipos y de código de respuesta, sin tocar ninguna de las clases
 * dadas.
 */
@Service
public class QRCheckAsistenciaAdapter implements ServicioAsistencia {

    private final QRCheckClient qrCheckClient;

    public QRCheckAsistenciaAdapter(QRCheckClient qrCheckClient) {
        this.qrCheckClient = qrCheckClient;
    }

        @Override
    public ResultadoCheckIn registrarAsistencia(String eventoId, String participanteId, String credencialQR) {
        long idEventoNumerico = Long.parseLong(eventoId);

        QRCheckRequest request = new QRCheckRequest(credencialQR, idEventoNumerico);
        QRCheckResponse response = qrCheckClient.validar(request);

        boolean exitoso = response.getCodigoRespuesta() == 200;
        return new ResultadoCheckIn(exitoso, response.getDetalle());
    }
}
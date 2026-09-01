package com.universidad.confudes.certificados;

public class CodigoQRDecorator implements ServicioCertificados {

    private final ServicioCertificados envuelto;

    public CodigoQRDecorator(ServicioCertificados envuelto) {
        this.envuelto = envuelto;
    }

    @Override
    public byte[] emitir(String eventoId, String participanteId) {
        byte[] certificado = envuelto.emitir(eventoId, participanteId);
        if (certificado == null) {
            return null;
        }
        byte[] qr = "-QR_VERIFICACION".getBytes();
        byte[] resultado = new byte[certificado.length + qr.length];
        System.arraycopy(certificado, 0, resultado, 0, certificado.length);
        System.arraycopy(qr, 0, resultado, certificado.length, qr.length);
        return resultado;
    }
}
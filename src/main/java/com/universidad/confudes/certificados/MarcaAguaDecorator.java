package com.universidad.confudes.certificados;

public class MarcaAguaDecorator implements ServicioCertificados {

    private final ServicioCertificados envuelto;

    public MarcaAguaDecorator(ServicioCertificados envuelto) {
        this.envuelto = envuelto;
    }

    @Override
    public byte[] emitir(String eventoId, String participanteId) {
        byte[] certificado = envuelto.emitir(eventoId, participanteId);
        if (certificado == null) {
            return null;
        }
        byte[] marcaAgua = "-MARCA_AGUA".getBytes();
        byte[] resultado = new byte[certificado.length + marcaAgua.length];
        System.arraycopy(certificado, 0, resultado, 0, certificado.length);
        System.arraycopy(marcaAgua, 0, resultado, certificado.length, marcaAgua.length);
        return resultado;
    }
}
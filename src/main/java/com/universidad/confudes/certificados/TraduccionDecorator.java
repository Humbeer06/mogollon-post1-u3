package com.universidad.confudes.certificados;

public class TraduccionDecorator implements ServicioCertificados {

    private final ServicioCertificados envuelto;
    private final String idioma;

    public TraduccionDecorator(ServicioCertificados envuelto, String idioma) {
        this.envuelto = envuelto;
        this.idioma = idioma;
    }

    @Override
    public byte[] emitir(String eventoId, String participanteId) {
        byte[] certificado = envuelto.emitir(eventoId, participanteId);
        if (certificado == null) {
            return null;
        }
        byte[] traduccion = ("-TRADUCIDO_" + idioma).getBytes();
        byte[] resultado = new byte[certificado.length + traduccion.length];
        System.arraycopy(certificado, 0, resultado, 0, certificado.length);
        System.arraycopy(traduccion, 0, resultado, certificado.length, traduccion.length);
        return resultado;
    }
}
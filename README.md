# ConfUDES — Patrones Estructurales

## Descripción

Repositorio del post-contenido de la Unidad 3 de Patrones de Diseño de Software. Un proyecto Spring Boot único que resuelve cuatro necesidades reales del backend ficticio de ConfUDES (gestión de eventos universitarios) aplicando los patrones estructurales Adapter, Facade, Decorator y Proxy.

## Cómo ejecutar

mvn clean package
mvn test

Los 8 tests (2 por cada una de las 4 necesidades) deben pasar sin errores.


## Decisiones de diseño

### Necesidad 1 — Adapter para QRCheckAPI

**Patrón elegido:** Adapter

**Justificación:** El problema presenta un único colaborador externo (`QRCheckClient`, del SDK de QRCheckAPI) cuyo contrato (`QRCheckRequest`/`QRCheckResponse`) es incompatible con el contrato interno que `ControladorCheckIn` ya espera (`ServicioAsistencia`). No hay múltiples colaboradores que orquestar, solo una traducción de tipos y de código de respuesta entre dos formas distintas de expresar el mismo resultado. Eso descarta Facade, que resuelve un problema distinto: reducir la cantidad de colaboradores que un cliente conoce, no traducir un contrato incompatible.

`QRCheckAsistenciaAdapter` implementa `ServicioAsistencia` (el contrato que `ControladorCheckIn` ya usa, sin modificarlo) y por dentro traduce hacia `QRCheckClient`, construyendo el `QRCheckRequest` a partir de los parámetros que llegan del controlador, e interpretando el `codigoRespuesta` de la `QRCheckResponse` como el booleano `exitoso` que `ResultadoCheckIn` necesita. Ni `ControladorCheckIn` ni `QRCheckClient` requirieron modificación alguna.


### Necesidad 2 — Facade para emisión de certificados

**Patrón elegido:** Facade

**Justificación:** `ControladorCertificados`, en su versión original, conocía y orquestaba directamente cuatro servicios distintos (`ServicioReservaSala`, `ServicioPlantillaPDF`, `ServicioFirmaDigital`, `ServicioNotificacionEmail`), cada uno con su propio método y su propia responsabilidad. El problema no era un contrato incompatible entre dos piezas (lo que apuntaría a Adapter), sino un exceso de colaboradores que el cliente no debería necesitar conocer directamente.

`CertificacionFacade` centraliza esa orquestación en un único método público, `emitir()`, que valida la reserva, genera el PDF, lo firma y notifica al participante, en ese orden. `ControladorCertificados` quedó reducido a una sola dependencia (`CertificacionFacade`), y su método `emitirCertificado()` pasó de coordinar cuatro servicios a solo tres líneas de código: invocar el Facade y traducir el resultado a una respuesta HTTP.


### Necesidad 3 — Decorator para mejoras del certificado

**Patrón elegido:** Decorator

**Justificación:** Las tres mejoras (marca de agua, código QR, traducción) son opcionales, combinables entre sí en cualquier cantidad y orden, y deben aplicarse sobre el resultado ya generado por `CertificacionFacade`. Una alternativa aparente sería usar herencia, creando subclases como `CertificadoConMarcaAgua` o `CertificadoConQRYTraduccion`, pero esa opción se descarta porque el número de subclases necesarias crecería combinatoriamente con cada mejora nueva: con solo 3 mejoras ya existen 8 combinaciones posibles (incluyendo la ausencia de todas), y cada una exigiría su propia subclase si se intentara cubrir con herencia estática.

`ServicioCertificados` es la interfaz común que implementan tanto `CertificacionFacade` como los tres decoradores (`MarcaAguaDecorator`, `CodigoQRDecorator`, `TraduccionDecorator`). Cada decorador envuelve una instancia de `ServicioCertificados` (que puede ser el Facade original o ya otro decorador), delega en ella y agrega su propia mejora al resultado. Esto permite apilar cualquier combinación de mejoras en tiempo de ejecución, como demuestra `DecoratorCertificadosTest`, sin necesitar una clase nueva por cada combinación: la misma composición de tres objetos decoradores cubre las 8 combinaciones posibles, no solo las 3 que se probaron explícitamente.


### Necesidad 4 — Proxy para control de acceso

**Patrón elegido:** Proxy

**Justificación:** Esta necesidad es estructuralmente casi idéntica a la anterior: `ControlAccesoProxy` también implementa `ServicioCertificados` y envuelve otra instancia de la misma interfaz, exactamente como los decoradores de la Necesidad 3. La diferencia no está en la estructura, sino en la intención y en el comportamiento ante el caso negativo. Un Decorator siempre deja pasar la llamada al objeto envuelto y solo añade comportamiento alrededor de ella; nunca decide si esa llamada ocurre o no. `ControlAccesoProxy`, en cambio, decide si la llamada real llega a ejecutarse: si `ServicioAutenticacion.tienePermiso()` devuelve `false`, el servicio envuelto nunca se invoca, y el método retorna `null` sin ejecutar ninguna de las cuatro operaciones de `CertificacionFacade`.

Esta distinción se verifica de forma concreta en `ControlAccesoProxyTest`, mediante la clase auxiliar `ContadorLlamadas`: cuando el permiso se deniega, el contador de invocaciones permanece en cero, confirmando que el servicio real jamás se ejecutó, no solo que su resultado se descartó después. Tratar este caso como un Decorator habría sido incorrecto porque un Decorator no tiene, por diseño, la responsabilidad de decidir si la operación subyacente ocurre.

## Reflexión: Composite y Flyweight

Ninguna de las cuatro necesidades de este laboratorio encajaba naturalmente con Composite o Flyweight, y vale la pena explicar por qué, ya que ambos también son patrones estructurales del catálogo GoF. Composite resuelve el problema de tratar de forma uniforme a un objeto individual y a una composición de esos mismos objetos, típicamente en estructuras jerárquicas o de árbol (como un sistema de archivos con carpetas y archivos). Ninguna de las cuatro necesidades de ConfUDES presentaba una estructura jerárquica de ese tipo: no hay un "certificado compuesto por certificados", ni una organización de eventos anidados que debiera tratarse de forma recursiva.

Flyweight resuelve el problema de reducir el consumo de memoria cuando existen muchísimas instancias de objetos que comparten la mayor parte de su estado, separando el estado compartido (intrínseco) del estado único de cada instancia (extrínseco). En este laboratorio no apareció un escenario de ese volumen ni esa necesidad de optimización de memoria; los certificados y las verificaciones de acceso se procesan como operaciones puntuales, no como una colección masiva de objetos similares que deban compartir estado para no agotar recursos.

## Conclusiones

Este laboratorio permitió aplicar los cinco patrones estructurales en un contexto donde la elección correcta dependía de un diagnóstico previo, no de seguir una receta indicada de antemano. La distinción más útil de todo el ejercicio fue la de la Necesidad 4: comprobar que dos patrones pueden ser estructuralmente idénticos en su código y, aun así, ser conceptualmente distintos por su intención, algo que solo se puede verificar con un test que efectivamente demuestre la diferencia de comportamiento, no solo con la explicación en prosa.
package com.cafeteriasoma.app.service.interfaces;

/**
 * Servicio encargado del envío de correos electrónicos en la aplicación.
 * Permite enviar mensajes simples de texto o HTML.
 */
public interface EmailService {

    /**
     * Envía un correo electrónico simple (texto plano).
     *
     * @param to      Dirección de correo del destinatario.
     * @param subject Asunto del mensaje.
     * @param text    Cuerpo del mensaje (texto plano).
     */
    void sendSimpleMessage(String to, String subject, String text);

    /**
     * Envía un correo electrónico con formato HTML.
     *
     * @param to      Dirección de correo del destinatario.
     * @param subject Asunto del mensaje.
     * @param htmlBody Cuerpo del mensaje (HTML).
     */
    void sendHtmlMessage(String to, String subject, String htmlBody);
}

package com.cafeteriasoma.app.util;

/**
 * Genera plantillas HTML simples y profesionales para correos electrónicos del sistema.
 */
public class EmailTemplateBuilder {

    /**
     * Genera una plantilla HTML para enviar un código de verificación.
     *
     * @param nombre Nombre del usuario.
     * @param codigo Código de verificación.
     * @return Cadena HTML lista para enviar.
     */
    public static String buildVerificationCodeEmail(String nombre, String codigo) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                <table align="center" width="100%%" style="max-width: 600px; background-color: white; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                  <tr>
                    <td style="padding: 20px; text-align: center; background-color: #3F51B5; color: white; border-radius: 8px 8px 0 0;">
                      <h2>Verificación de cuenta - Cafetería Soma</h2>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding: 30px;">
                      <p>Hola <strong>%s</strong>,</p>
                      <p>Gracias por registrarte en <strong>Cafetería Soma</strong>. Para completar el proceso de verificación de tu cuenta, usa el siguiente código:</p>

                      <div style="text-align: center; margin: 25px 0;">
                        <span style="font-size: 28px; font-weight: bold; color: #3F51B5; letter-spacing: 4px;">%s</span>
                      </div>

                      <p>🔒 Este código es válido por <strong>15 minutos</strong>.</p>
                      <p>Por motivos de seguridad:</p>
                      <ul style="color: #555;">
                        <li>No compartas este código con nadie.</li>
                        <li>Si no solicitaste esta verificación, ignora este correo o contacta con un administrador.</li>
                      </ul>

                      <p style="margin-top: 20px;">Gracias por confiar en nosotros ☕</p>
                      <p>— El equipo de Cafetería Soma</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="text-align: center; font-size: 12px; color: #aaa; padding: 15px; border-top: 1px solid #eee;">
                      Este correo fue generado automáticamente. Por favor, no respondas a este mensaje.
                    </td>
                  </tr>
                </table>
              </body>
            </html>
            """.formatted(nombre, codigo);
    }
}

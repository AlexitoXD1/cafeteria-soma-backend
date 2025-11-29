package com.cafeteriasoma.app.service.interfaces;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.entity.VerificationCode;

public interface VerificationCodeService {

    /**
     * Genera un nuevo código de verificación para el usuario.
     * Si el usuario ya tenía un código previo, se invalida antes de crear el nuevo.
     *
     * @param usuario Usuario al cual se le asignará el código.
     * @return El código de verificación recién generado.
     */
    VerificationCode generateCode(Usuario usuario);

    /**
     * Verifica si el código ingresado es válido para el correo asociado.
     * Si la verificación es exitosa, se marca como usado y el usuario se activa.
     *
     * @param correo Correo del usuario.
     * @param codigo Código ingresado.
     * @return true si la verificación fue exitosa, false si falló.
     */
    boolean verifyCode(String correo, String codigo);

    /**
     * Invalida cualquier código activo que tenga el usuario.
     * Útil cuando se genera uno nuevo.
     *
     * @param usuario Usuario cuyo código será invalidado.
     */
    void invalidateOldCodes(Usuario usuario);
}

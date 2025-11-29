package com.cafeteriasoma.app.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.entity.VerificationCode;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.repository.VerificationCodeRepository;
import com.cafeteriasoma.app.service.interfaces.EmailService;
import com.cafeteriasoma.app.service.interfaces.VerificationCodeService;
import com.cafeteriasoma.app.util.EmailTemplateBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public VerificationCode generateCode(Usuario usuario) {
        // Invalida códigos anteriores si los hay
        invalidateOldCodes(usuario);

        // Genera un código aleatorio de 6 caracteres
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        String codigo = sb.toString();

        VerificationCode verificationCode = VerificationCode.builder()
                .usuario(usuario)
                .codigo(codigo)
                .fechaCreacion(LocalDateTime.now())
                .fechaExpiracion(LocalDateTime.now().plusMinutes(15))
                .usado(false)
                .build();

        // Guarda el código
        VerificationCode saved = verificationCodeRepository.save(verificationCode);

        // Genera el HTML del correo
        String html = EmailTemplateBuilder.buildVerificationCodeEmail(usuario.getNombre(), codigo);

        // Envía el correo con manejo de excepción SMTP
        try {
            emailService.sendHtmlMessage(
                    usuario.getCorreo(),
                    "Código de verificación - Cafetería Soma",
                    html
            );
            System.out.println("Correo de verificación enviado a: " + usuario.getCorreo());
        } catch (MailException e) {
            System.err.println("Error al enviar el correo a " + usuario.getCorreo() + ": " + e.getMessage());
            e.printStackTrace(); // muestra el stack trace completo para debug
        }

        return saved;
    }

    @Override
    @Transactional
    public boolean verifyCode(String correo, String codigo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();
        Optional<VerificationCode> codeOpt = verificationCodeRepository.findByUsuario(usuario);
        if (codeOpt.isEmpty()) return false;

        VerificationCode verificationCode = codeOpt.get();

        boolean valido = !verificationCode.getUsado()
                && verificationCode.getCodigo().equalsIgnoreCase(codigo)
                && verificationCode.getFechaExpiracion().isAfter(LocalDateTime.now());

        if (valido) {
            verificationCode.setUsado(true);
            verificationCodeRepository.save(verificationCode);

            usuario.setActivo(true);
            usuarioRepository.save(usuario);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void invalidateOldCodes(Usuario usuario) {
        var codes = verificationCodeRepository.findByUsuario(usuario);
        codes.ifPresent(code -> {
            code.setUsado(true);
            verificationCodeRepository.save(code);
        });
    }
}

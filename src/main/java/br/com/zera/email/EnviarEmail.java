package br.com.zera.email;

import br.com.zera.config.EnvConfig;
import br.com.zera.exception.EmailFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Classe utilitária que envia e-mails pelo servidor SMTP do Gmail.
 *
 * O remetente e a senha de app vêm das variáveis EMAIL_USER e EMAIL_PASSWORD,
 * lidas pelo EnvConfig (arquivo .env ou variáveis de ambiente).
 *
 * Esta classe não deve ser instanciada.
 *
 * @author Pedro Rufino
 */
public final class EnviarEmail {

    /** Servidor SMTP do Gmail */
    private static final String HOST = "smtp.gmail.com";

    /** Porta do Gmail para conexão com STARTTLS */
    private static final String PORTA = "587";

    /** Nome que aparece como remetente na caixa de entrada */
    private static final String NOME_REMETENTE = "Zera";

    /**
     * Construtor privado que proíbe a instanciação da classe utilitária.
     */
    private EnviarEmail() {
        throw new UnsupportedOperationException("Classe EnviarEmail não deve ser instanciada");
    }

    /**
     * Envia um e-mail em HTML para um destinatário.
     *
     * @param destinatario e-mail de quem vai receber
     * @param assunto assunto do e-mail
     * @param corpoHtml conteúdo do e-mail em HTML
     * @throws EmailFailedException se as variáveis não estiverem configuradas ou se o envio falhar
     */
    public static void enviar(String destinatario, String assunto, String corpoHtml) {
        String remetente = EnvConfig.getEnv("EMAIL_USER");
        String senhaApp = EnvConfig.getEnv("EMAIL_PASSWORD");

        if (remetente == null || senhaApp == null) {
            throw new EmailFailedException("Variáveis EMAIL_USER ou EMAIL_PASSWORD não foram configuradas.");
        }

        // configurações da conexão com o Gmail
        Properties propriedades = new Properties();
        propriedades.put("mail.smtp.host", HOST);
        propriedades.put("mail.smtp.port", PORTA);
        propriedades.put("mail.smtp.auth", "true");
        propriedades.put("mail.smtp.starttls.enable", "true");

        // a sessão faz o login no Gmail com o remetente e a senha de app
        Session sessao = Session.getInstance(propriedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });

        try {
            MimeMessage mensagem = new MimeMessage(sessao);
            mensagem.setFrom(new InternetAddress(remetente, NOME_REMETENTE, "UTF-8"));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensagem.setSubject(assunto, "UTF-8");
            mensagem.setContent(corpoHtml, "text/html; charset=UTF-8");

            Transport.send(mensagem);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailFailedException("Não foi possível enviar o e-mail.", e);
        }
    }
}
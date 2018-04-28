package com.novamaday.afilehost.utils;

import com.novamaday.afilehost.objects.SiteSettings;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

public class EmailHandler {
    private static EmailHandler instance;

    private Mailer mailer;

    private EmailHandler() {
    }

    public static EmailHandler getHandler() {
        if (instance == null)
            instance = new EmailHandler();

        return instance;
    }

    public void init() {

        mailer = MailerBuilder.withSMTPServer("smtp.gmail.com", 465, SiteSettings.EMAIL_USER.get(), SiteSettings.EMAIL_PASS.get()).withTransportStrategy(TransportStrategy.SMTPS).buildMailer();
    }

    public void sendEmailConfirm(String emailTo, String confirmationLink) {
        Email email = EmailBuilder.startingBlank()
                .from("Do Not Reply | aFileHost", "do-not-reply@afilehost.com")
                .to(emailTo)
                .withSubject("Confirm Your Email")
                .withHTMLText(getConfirmEmail(emailTo, confirmationLink))
                .buildEmail();

        mailer.sendMail(email);
    }

    public void sendContactEmail(String subject, String message, String name, String emailFrom) {
        Email email = EmailBuilder.startingBlank()
                .from(name, emailFrom)
                .to(SiteSettings.EMAIL_USER.get())
                .withSubject(subject)
                .withPlainText(message)
                .buildEmail();

        mailer.sendMail(email);
    }

    private String getConfirmEmail(String emailTo, String confirmationLink) {
        //TODO: CHANGE THIS TO OUR STYLES AND NAMES!!
        return "<h1 style=\"text-align: center; color: #de1a1a;\">Confirm Your Email</h1>\n" +
                "<p style=\"text-align: center;\">Thank you for signing up for an account at <a title=\"NovaMaday Site\" href=\"https://www.novamaday.com\" target=\"_blank\">https://www.novamaday.com</a></p>\n" +
                "<p style=\"text-align: center;\">Please click the button below to confirm your email.</p>\n" +
                "<p style=\"text-align: center;\">Didn't sign up? Just ignore this message.</p>\n" +
                "<p style=\"text-align: center;\"><a href=\"" + confirmationLink + "\"><button style=\"font-size: 18px; background-color: #de1a1a; color: white; padding: 10px; border: 2px black; margin: 10px; width: auto; height: auto;\">Confirm Email</button></a></p>\n" +
                "<p style=\"text-align: center;\">&nbsp;Button not working? Click the link below:</p>\n" +
                "<p style=\"text-align: center;\"><a title=\"Confirm Email\" href=\"" + confirmationLink + "\" target=\"_blank\">" + confirmationLink + "</a></p>\n" +
                "<p style=\"text-align: center;\">Email sent to " + emailTo + " in response to account creation</p>\n" +
                "<p style=\"text-align: center;\">&copy; 2018 <a title=\"Nova Maday\" href=\"https://www.novamaday.com\" target=\"_blank\">Nova Maday</a> | <a title=\"Privacy Policy\" href=\"https://novamaday.com/policy/privacy\" target=\"_blank\">Privacy Policy</a> | <a title=\"Contact\" href=\"https://www.novamaday.com/contact\" target=\"_blank\">Contact</a></p>";
    }
}
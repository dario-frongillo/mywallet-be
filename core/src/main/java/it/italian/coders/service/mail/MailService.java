package it.italian.coders.service.mail;

import java.util.HashMap;
import java.util.Map;

public interface MailService {
    void sendTextMail(String recipient, String subject, String message);
    void sendMailByTemplate(String recipient, String subject, String template, Map<String,String> params);
    void sendMailWithAttachment(String to,
                                       String subject,
                                       String text,
                                       String pathToAttachment);
}

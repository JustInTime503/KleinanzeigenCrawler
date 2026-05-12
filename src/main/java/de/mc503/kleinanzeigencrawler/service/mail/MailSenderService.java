package de.mc503.kleinanzeigencrawler.service.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    private final List<MailQueueItem> queueItems = new ArrayList<>();

    public void sendMail(String subject, String msg) {
        log.info("Adding mail to queue: {}", subject);
        queueItems.add(new MailQueueItem(subject, msg));
    }

    @Scheduled(fixedRate = 3000, timeUnit = TimeUnit.MILLISECONDS)
    private void sendNextMail() {
        if (!queueItems.isEmpty()) {
            MailQueueItem mailQueueItem = queueItems.remove(0);
            internalSendMail(mailQueueItem.getSubject(), mailQueueItem.getMessage());
        }
    }

    private void internalSendMail(String subject, String msg) {
        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", 587);
            prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("justin503.trahe@gmail.com", "yysj ueee tsgv ggja");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("justin503.trahe@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mc503.kleincrawl@gmail.com"));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            log.info("Mail sent successfully: {}", subject);
        }  catch (Exception e) {
            log.error("Error sending mail: {}", subject, e);
        }
    }

    @AllArgsConstructor
    @Getter
    private class MailQueueItem {
        private String subject;
        private String message;
    }
}

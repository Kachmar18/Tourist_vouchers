package tourist_vouchers.v17_tourist_vouchers.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final String USERNAME = "kachmares@gmail.com";
    private static final String PASSWORD = "18052005K";
    private static final String TO = "122.24kachmar.b@nltu.lviv.ua";

    public static void sendCriticalError(String subject, Exception e) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO));
            message.setSubject(subject);
            message.setText("Виникла критична помилка:\n\n" + e.getMessage());

            Transport.send(message);

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}

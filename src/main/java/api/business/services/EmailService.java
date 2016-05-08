package api.business.services;

import api.business.services.interfaces.IEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
@Stateless
public class EmailService implements IEmailService{

    @Resource(name = "java:jboss/mail/Gmail")
    private Session session;
    final Logger logger = LogManager.getLogger(getClass().getName());

    public void send(String addresses, String subject, String textMessage) throws MessagingException {

        try {
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
            message.setSubject(subject);
            message.setText(textMessage);
            Transport.send(message);
            logger.trace("Email sent to "+ addresses);

        } catch (MessagingException e) {
            logger.error(e);
            throw e;
        }
    }
    public static boolean isValidEmailAddress(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}

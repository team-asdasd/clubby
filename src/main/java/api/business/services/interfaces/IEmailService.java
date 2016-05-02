package api.business.services.interfaces;

import javax.mail.MessagingException;

public interface IEmailService {
    void send(String addresses, String Subject, String textMessage) throws MessagingException;
}

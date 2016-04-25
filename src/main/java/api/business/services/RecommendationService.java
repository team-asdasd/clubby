package api.business.services;

import api.business.entities.User;
import api.business.services.interfaces.IEmailService;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.BadRequestException;
import java.util.UUID;

@Stateless
public class RecommendationService implements IRecommendationService {

    @PersistenceContext
    EntityManager em;
    @Inject
    IEmailService emailService;
    @Inject
    IUserService userService;

    @Override
    public void recommend(String recommendationCode) {

        Subject currentUser = SecurityUtils.getSubject();
        String email = currentUser.getPrincipal().toString();
        Query q1 = em.createNativeQuery("SELECT *  FROM main.users, main.recommendations WHERE users.id" +
                " = recommendations.user_to AND recommendations.recommendation_code = :recommendationCode", User.class);
        Query q2 = em.createNativeQuery("SELECT *  FROM main.users, main.recommendations WHERE users.id" +
                " = recommendations.user_from AND recommendations.recommendation_code = :recommendationCode", User.class);
        q1.setParameter("recommendationCode", recommendationCode);
        q2.setParameter("recommendationCode", recommendationCode);
        User userTo = (User) q1.getSingleResult();
        //TODO: add recommendation points on this user
        User userFrom = (User) q2.getSingleResult();
        if (userFrom.getEmail() != email) {
            throw new BadRequestException();
        }
        //TODO: remove row
    }

    public void sendRecommendationRequest(String email) {

        User userFrom = userService.getByEmail(email);
        Subject currentUser = SecurityUtils.getSubject();
        String emailFrom = currentUser.getPrincipal().toString();
        User userTo = userService.getByEmail(emailFrom);
        Query q1 = em.createNativeQuery("INSERT INTO main.recommendations (user_from, user_to, recommendation_code)" +
                "VALUES (:userFrom, :userTo, :recommendationCode)");
        q1.setParameter("recommendationCode", UUID.randomUUID().toString());
        q1.setParameter("userFrom", userFrom.getId());
        q1.setParameter("userTo", userTo.getId());
        q1.executeUpdate();

        try {
            emailService.send(emailFrom,"Recommendation request", "You have received recommendation request from user " + userTo.getName());
        } catch (MessagingException e) {
            //throw e;//TODO: rethrow
        }
    }
}

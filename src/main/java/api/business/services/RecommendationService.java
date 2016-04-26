package api.business.services;

import api.business.entities.User;
import api.business.services.interfaces.IEmailService;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.BadRequestException;
import java.math.BigInteger;
import java.util.UUID;

@Stateless
public class RecommendationService implements IRecommendationService {

    @PersistenceContext
    EntityManager em;
    @Inject
    IEmailService emailService;
    @Inject
    IUserService userService;
    final Logger logger = LogManager.getLogger(getClass().getName());
    @Override
    public void ConfirmRecommendation(String recommendationCode) {

        Subject currentUser = SecurityUtils.getSubject();
        String email = currentUser.getPrincipal().toString();
        Query q1 = em.createNativeQuery("SELECT *  FROM main.users, main.recommendations WHERE users.id" +
                " = recommendations.user_to AND recommendations.recommendation_code = :recommendationCode " +
                "AND recommendations.status != 1", User.class);
        Query q2 = em.createNativeQuery("SELECT *  FROM main.users, main.recommendations WHERE users.id" +
                " = recommendations.user_from AND recommendations.recommendation_code = :recommendationCode " +
                "AND recommendations.status != 1", User.class);
        q1.setParameter("recommendationCode", recommendationCode);
        q2.setParameter("recommendationCode", recommendationCode);
        User userTo = (User) q1.getSingleResult();
        User userFrom = (User) q2.getSingleResult();

        if (userFrom == null || userTo == null || !userFrom.getEmail().equals(email)) {
            throw new BadRequestException();
        }

        Query q3 = em.createNativeQuery("UPDATE main.recommendations SET status = 1 WHERE recommendation_code = :recommendationCode");
        q3.setParameter("recommendationCode", recommendationCode);
        q3.executeUpdate();
        Query q4 = em.createNativeQuery("SELECT COUNT(status) FROM main.recommendations WHERE status = 1 AND user_to = :userto ");
        q4.setParameter("userto", userTo.getId());
        BigInteger value = (BigInteger) q4.getSingleResult();


        Query q5 = em.createNativeQuery("SELECT value FROM main.configurations WHERE key = 'min_recommendation_required'");
        String minReq = (String) q5.getSingleResult();

        if(Integer.parseInt(minReq)<= value.intValue())
        {
            //change role
            Query q6 = em.createNativeQuery("DELETE FROM security.logins_roles WHERE role_name = 'candidate' AND username = :username");
            q6.setParameter("username", userTo.getEmail());
            q6.executeUpdate();
            logger.trace("User " + userTo.getEmail()+ " is not candidate anymore!");
        }
        logger.trace("User " + userTo.getEmail()+ " received recommendation from " + userFrom.getEmail());
    }

    public void sendRecommendationRequest(String email) throws MessagingException {

        //TODO refactor to use username
        User userFrom = userService.getByEmail(email);
        Subject currentUser = SecurityUtils.getSubject();
        String emailFrom = currentUser.getPrincipal().toString();
        User userTo = userService.getByEmail(emailFrom);
        Query q1 = em.createNativeQuery("INSERT INTO main.recommendations (user_from, user_to, recommendation_code)" +
                "VALUES (:userFrom, :userTo, :recommendationCode)");
        q1.setParameter("recommendationCode", UUID.randomUUID().toString().replaceAll("-",""));
        q1.setParameter("userFrom", userFrom.getId());
        q1.setParameter("userTo", userTo.getId());
        q1.executeUpdate();
        emailService.send(emailFrom,"Recommendation request", "You have received recommendation request from user " + userTo.getName());
    }

}

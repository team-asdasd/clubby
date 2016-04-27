package api.business.services;

import api.contracts.dto.RecommendationData;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.BadRequestException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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

        User userTo;
        User userFrom;
        try {
            userTo = (User) q1.getSingleResult();
            userFrom = (User) q2.getSingleResult();
        } catch (NoResultException e) {
            throw new BadRequestException("Bad recommendation code");
        }

        if (!userFrom.getEmail().equals(email)) {
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

        if (Integer.parseInt(minReq) <= value.intValue()) {
            //change role
            Query q6 = em.createNativeQuery("DELETE FROM security.logins_roles WHERE role_name = 'candidate' AND username = :username");
            q6.setParameter("username", userTo.getEmail());
            q6.executeUpdate();
            logger.trace("User " + userTo.getEmail() + " is not candidate anymore");
        }
        logger.trace("User " + userTo.getEmail() + " received recommendation from " + userFrom.getEmail());
    }

    public void sendRecommendationRequest(int userId) throws MessagingException {

        User userFrom = userService.get(userId);

        if (userFrom == null) {
            logger.trace("Wrong id: " + userId);
            throw new BadRequestException("Bad request parameter");
        }
        String emailFrom = userFrom.getEmail();
        String emailTo = SecurityUtils.getSubject().getPrincipal().toString();
        User userTo = userService.getByEmail(emailTo);

        if (userTo.getId() == userFrom.getId()) {
            throw new BadRequestException("Can't send request to yourself");
        }
        //check if userTo is candidate
        Query q = em.createNativeQuery("SELECT role_name FROM security.logins_roles WHERE username = :username AND role_name = 'candidate'");
        q.setParameter("username", userTo.getLogin().getUsername());
        if (q.getResultList().size() != 1) {
            throw new BadRequestException("Already a member");
        }

        //check if userFrom is member
        Query q1 = em.createNativeQuery("SELECT role_name FROM security.logins_roles WHERE username = :username AND role_name = 'candidate'");
        q1.setParameter("username", userFrom.getEmail());
        if (q1.getResultList().size() != 0) {
            throw new BadRequestException("Can't send request to candidate");
        }

        Query q2 = em.createNativeQuery("SELECT value FROM main.configurations WHERE key = 'max_recommendation_request'");
        String maxReq = (String) q2.getSingleResult();

        Query q3 = em.createNativeQuery("SELECT COUNT(*) FROM main.recommendations WHERE user_to = :userto ");
        q3.setParameter("userto", userTo.getId());
        BigInteger reqSent = (BigInteger) q3.getSingleResult();

        if (reqSent.intValue() < Integer.parseInt(maxReq)) {
            Query q4 = em.createNativeQuery("INSERT INTO main.recommendations (user_from, user_to, recommendation_code)" +
                    "VALUES (:userFrom, :userTo, :recommendationCode)");
            q4.setParameter("recommendationCode", UUID.randomUUID().toString().replaceAll("-", ""));
            q4.setParameter("userFrom", userFrom.getId());
            q4.setParameter("userTo", userTo.getId());
            q4.executeUpdate();

            emailService.send(emailFrom, "Recommendation request", "Hello dear friend! \nYou have received recommendation request from user "
                    + userTo.getName());
        } else {
            //notify end user?
        }
        logger.trace("Recommendation request from user " + userTo.getEmail() + " to " + userFrom.getEmail() + " has been sent");
    }

    public List<RecommendationData> getAllRecommendationRequests() {

        String currentUserEmail = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.getByEmail(currentUserEmail);

        Query q = em.createNativeQuery("SELECT users.id, recommendation_code FROM main.recommendations, " +
                "main.users WHERE user_from = :user_from AND users.id = user_to" +
                " AND  status = 0");
        q.setParameter("user_from", user.getId());
        List<Object[]> list = q.getResultList();
        List<RecommendationData> result = new ArrayList<>();
        for (Object[] o : list) {
            String recCode = (String) o[1];
            int id = (int) o[0];

            RecommendationData res = new RecommendationData(id, recCode);
            result.add(res);
        }
        return result;
    }
}

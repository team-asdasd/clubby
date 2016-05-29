package api.business.services;

import api.business.entities.Configuration;
import api.business.entities.Recommendation;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.interfaces.IEmailService;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.dto.RecommendationDto;
import api.contracts.enums.NotificationAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class RecommendationService implements IRecommendationService {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private IEmailService emailService;
    @Inject
    private IUserService userService;
    @Inject
    private INotificationsService notificationsService;

    private final Logger logger = LogManager.getLogger(getClass().getName());
    private final String memberMessage = "You are member of club now";
    private final String confirmedMessage = "%s confirmed recommendation";
    private final String emailSubject = "Recommendation request";
    private final String emailMessage = "Hello dear friend! \nYou have received recommendation request from user ";
    private final String requestReceivedNotification = "Recommendation request received";

    @Override
    public void ConfirmRecommendation(String recommendationCode) {
        List<Recommendation> recommendations = em.createQuery("SELECT r FROM Recommendation r JOIN r.userTo u WHERE r.recommendationCode = :recommendationCode AND r.status <> 1", Recommendation.class)
                .setParameter("recommendationCode", recommendationCode)
                .getResultList();

        Recommendation recommendation = recommendations.get(0);

        User userTo = recommendation.getUserTo();
        User userFrom = recommendation.getUserFrom();

        recommendation.setStatus(1);
        notificationsService.create(String.format(confirmedMessage, userFrom.getName()), NotificationAction.DISMISS, userTo.getId());

        long count = em.createQuery("SELECT COUNT(r) FROM Recommendation r WHERE r.status = 1 AND r.userTo = :userTo", Long.class)
                .setParameter("userTo", userTo)
                .getSingleResult();
        Configuration minRec = em.find(Configuration.class, "min_recommendation_required");

        if (Integer.parseInt(minRec.getValue()) <= count) {
            //change role
            Role lr = em.createQuery("SELECT lr FROM Role lr WHERE lr.roleName = 'candidate' AND lr.username = :username", Role.class)
                    .setParameter("username", userTo.getLogin().getEmail())
                    .getSingleResult();
            em.remove(lr);
            Role r = new Role();
            r.setUsername(userTo.getLogin().getEmail());
            r.setRoleName("member");
            em.persist(r);
            logger.info("User " + userTo.getLogin().getEmail() + " is member");
            notificationsService.create(memberMessage, NotificationAction.DISMISS, userTo.getId());
        }
        logger.info("User " + userTo.getLogin().getEmail() + " received recommendation from " + userFrom.getLogin().getEmail());

    }

    public void sendRecommendationRequest(String userEmail) throws MessagingException {

        User userFrom = userService.getByEmail(userEmail);

        User userTo = userService.get();

        Recommendation r = new Recommendation();
        r.setRecommendationCode(UUID.randomUUID().toString().replaceAll("-", ""));
        r.setUserFrom(userFrom);
        r.setUserTo(userTo);

        em.persist(r);

        emailService.send(userEmail, emailSubject, emailMessage + userTo.getName());
        notificationsService.create(requestReceivedNotification, NotificationAction.RECOMMENDATIONS, userFrom.getId());
        logger.trace("Recommendation request from user " + userTo.getLogin().getEmail() + " to " + userFrom.getLogin().getEmail() + " has been sent");
    }

    public List<RecommendationDto> getReceivedRecommendationRequests() {

        User user = userService.get();

        List<Recommendation> recommendations = em.createQuery("SELECT r FROM Recommendation r WHERE r.userFrom = :userFrom AND r.status = 0", Recommendation.class)
                .setParameter("userFrom", user)
                .getResultList();

        List<RecommendationDto> result = new ArrayList<>();
        for (Recommendation r : recommendations) {
            RecommendationDto res = new RecommendationDto(r.getStatus(), r.getUserTo().getId(), r.getRecommendationCode());
            result.add(res);
        }
        return result;
    }

    public List<RecommendationDto> getSentRecommendationRequests() {

        User user = userService.get();

        List<Recommendation> recommendations = em.createQuery("SELECT r FROM Recommendation r WHERE r.userTo = :userTo", Recommendation.class)
                .setParameter("userTo", user)
                .getResultList();

        List<RecommendationDto> result = new ArrayList<>();
        for (Recommendation r : recommendations) {
            RecommendationDto res = new RecommendationDto(r.getStatus(), r.getUserFrom().getId(), null);
            result.add(res);
        }
        return result;
    }

    public Boolean isRequestLimitReached() {
        Configuration maxReq = em.find(Configuration.class, "max_recommendation_request");

        long requestCount = em.createQuery("SELECT COUNT(r) FROM Recommendation r WHERE r.userTo = :userTo", Long.class)
                .setParameter("userTo", userService.get())
                .getSingleResult();

        return requestCount > Integer.parseInt(maxReq.getValue());
    }
}

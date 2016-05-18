package api.business.services;

import api.business.entities.*;
import api.contracts.dto.RecommendationDto;
import api.business.services.interfaces.IEmailService;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.*;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
public class RecommendationService implements IRecommendationService {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private IEmailService emailService;
    @Inject
    private IUserService userService;

    private final Logger logger = LogManager.getLogger(getClass().getName());

    @Override
    public void ConfirmRecommendation(String recommendationCode) {
        List<Recommendation> recommendations = em.createQuery("SELECT r FROM Recommendation r JOIN r.userTo u WHERE r.recommendationCode = :recommendationCode AND r.status <> 1", Recommendation.class)
                .setParameter("recommendationCode", recommendationCode)
                .getResultList();

        if (recommendations.size() != 1) {
            throw new BadRequestException("Bad recommendation code");
        }

        Recommendation recommendation = recommendations.get(0);

        User userTo = recommendation.getUserTo();
        User userFrom = recommendation.getUserFrom();

        User currentUser = userService.get();

        if (!userFrom.getLogin().getEmail().equals(currentUser.getLogin().getEmail())) {
            throw new BadRequestException();
        }

        recommendation.setStatus(1);

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
            logger.trace("User " + userTo.getLogin().getEmail() + " is not candidate anymore");
        }
        logger.trace("User " + userTo.getLogin().getEmail() + " received recommendation from " + userFrom.getLogin().getEmail());
    }

    public void sendRecommendationRequest(String userEmail) throws MessagingException {

        User userFrom = userService.getByEmail(userEmail);

        if (userFrom == null) {
            logger.trace("Wrong email: " + userEmail);
            throw new BadRequestException("User with this email does not exist");
        }

        User userTo = userService.get();

        if (userTo.getId() == userFrom.getId()) {
            throw new BadRequestException("Can't send request to yourself");
        }
        //check if userTo is candidate
        List<Role> userToRoles = userTo.getLogin().getRoles();
        if (!userToRoles.stream().map(x -> x.getRoleName()).collect(Collectors.toList()).contains("candidate")) {
            throw new BadRequestException("Already a member");
        }

        //check if userFrom is member
        List<Role> userFromRoles = userFrom.getLogin().getRoles();
        if (userFromRoles.stream().map(x -> x.getRoleName()).collect(Collectors.toList()).contains("candidate")) {
            throw new BadRequestException("Can't send request to candidate");
        }

        //check for multiple recommendations to same member
        List<Recommendation> recList = em.createQuery("SELECT r FROM Recommendation r WHERE r.userFrom = :userFrom " +
                "AND r.userTo = :userTo", Recommendation.class)
                .setParameter("userFrom", userFrom)
                .setParameter("userTo", userTo)
                .getResultList();
        if (recList.size() != 0) {
            throw new BadRequestException("Can't send multiple request to same member");
        }

        if (!isRequestLimitReached()) {

            Recommendation r = new Recommendation();
            r.setRecommendationCode(UUID.randomUUID().toString().replaceAll("-", ""));
            r.setUserFrom(userFrom);
            r.setUserTo(userTo);

            em.persist(r);

            emailService.send(userEmail, "Recommendation request", "Hello dear friend! \nYou have received recommendation request from user "
                    + userTo.getName());
            logger.trace("Recommendation request from user " + userTo.getLogin().getEmail() + " to " + userFrom.getLogin().getEmail() + " has been sent");
        } else {
            throw new BadRequestException("Request limit reached");
        }
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

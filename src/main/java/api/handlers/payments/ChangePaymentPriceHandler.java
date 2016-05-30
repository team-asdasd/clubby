package api.handlers.payments;

import api.business.entities.LineItem;
import api.business.entities.Payment;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.payments.ChangePaymentPriceRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class ChangePaymentPriceHandler extends BaseHandler<ChangePaymentPriceRequest, BaseResponse> {
    @PersistenceContext
    private EntityManager em;
    private int yearlyMembershipPaymentId = 1;

    @Override
    public ArrayList<ErrorDto> validate(ChangePaymentPriceRequest request) {
        ArrayList<ErrorDto> errors = new Validator().isAdministrator().getErrors();
        if (!errors.isEmpty()) {
            return errors;
        }
        Payment payment = em.find(Payment.class, yearlyMembershipPaymentId);
        if (payment == null) {
            errors.add(new ErrorDto("Payment not found", ErrorCodes.NOT_FOUND));
        }
        return errors;
    }

    @Override
    public BaseResponse handleBase(ChangePaymentPriceRequest request) {

        LineItem lineItem = em.createQuery("SELECT L FROM LineItem L WHERE L.payment.paymentid = :paymentId", LineItem.class)
                .setParameter("paymentId", yearlyMembershipPaymentId)
                .getResultList().get(0);
        lineItem.setPrice(request.price);
        em.merge(lineItem);
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}

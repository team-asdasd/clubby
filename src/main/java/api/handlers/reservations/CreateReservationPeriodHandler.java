package api.handlers.reservations;

import api.business.services.interfaces.ICottageService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.CreateReservationPeriodRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class CreateReservationPeriodHandler extends BaseHandler<CreateReservationPeriodRequest, BaseResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(CreateReservationPeriodRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().isAdministrator().allFieldsSet(request).getErrors();

        DateTime to = parseDateTime(request.to);
        if (to == null) {
            errors.add(new ErrorDto("Invalid 'to' date format.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        DateTime from = parseDateTime(request.from);
        if (from == null) {
            errors.add(new ErrorDto("Invalid 'from' date format.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (from.isAfter(to)) {
            errors.add(new ErrorDto("To date must be after from date.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }


        if (cottageService.getReservationPeriods(request.from, request.to).size() != 0) {
            errors.add(new ErrorDto("Reservation period dates crosses with other reservation periods.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(CreateReservationPeriodRequest request) {
        DateTime to = parseDateTime(request.to);
        DateTime from = parseDateTime(request.from);

        cottageService.saveReservationPeriod(from, to);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }

    private DateTime parseDateTime(String dateStr) {
        try {
            return DateTime.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}

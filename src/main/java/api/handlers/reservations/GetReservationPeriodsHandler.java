package api.handlers.reservations;

import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.ReservationPeriodDto;
import api.contracts.reservations.GetReservationPeriodsRequest;
import api.contracts.reservations.GetReservationPeriodsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Stateless
public class GetReservationPeriodsHandler extends BaseHandler<GetReservationPeriodsRequest, GetReservationPeriodsResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetReservationPeriodsRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();
        errors.addAll(new Validator().isAdministrator().getErrors());
        if (!errors.isEmpty()) return errors;

        if (request.from != null){
            DateTime from = parseDateTime(request.from);
            if (from == null) {
                errors.add(new ErrorDto("Invalid 'from' date format.", ErrorCodes.VALIDATION_ERROR));
                return errors;
            }
        }

        if (request.to != null){
            DateTime to = parseDateTime(request.to);
            if (to == null) {
                errors.add(new ErrorDto("Invalid 'to' date format.", ErrorCodes.VALIDATION_ERROR));
                return errors;
            }
        }

        return errors;
    }

    @Override
    public GetReservationPeriodsResponse handleBase(GetReservationPeriodsRequest request) {
        GetReservationPeriodsResponse response = new GetReservationPeriodsResponse();

        response.reservationPeriods = cottageService.getReservationPeriods(request.from, request.to).stream().map(ReservationPeriodDto::new).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetReservationPeriodsResponse createResponse() {
        return new GetReservationPeriodsResponse();
    }

    private DateTime parseDateTime(String dateStr) {
        try {
            return DateTime.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}

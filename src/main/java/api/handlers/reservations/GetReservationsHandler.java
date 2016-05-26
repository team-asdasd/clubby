package api.handlers.reservations;

import api.business.entities.Reservation;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorDto;
import api.contracts.dto.ReservationDto;
import api.contracts.reservations.GetReservationsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Stateless
public class GetReservationsHandler extends BaseHandler<BaseRequest, GetReservationsResponse> {
    @Inject
    private ISimpleEntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetReservationsResponse handleBase(BaseRequest request) {
        GetReservationsResponse response = createResponse();

        response.reservations = em.getAll(Reservation.class).stream().map(ReservationDto::new).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetReservationsResponse createResponse() {
        return new GetReservationsResponse();
    }
}

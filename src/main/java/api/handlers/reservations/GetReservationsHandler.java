package api.handlers.reservations;

import api.business.entities.Reservation;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorDto;
import api.contracts.dto.ReservationDto;
import api.contracts.reservations.GetReservationsRequest;
import api.contracts.reservations.GetReservationsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class GetReservationsHandler extends BaseHandler<GetReservationsRequest, GetReservationsResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetReservationsRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isMember().getErrors();
    }

    @Override
    public GetReservationsResponse handleBase(GetReservationsRequest request) {
        GetReservationsResponse response = createResponse();

        List<Reservation> reservations;
        switch (request.category) {
            default:
            case all: {
                reservations = cottageService.getReservations();
                break;
            }
            case upcoming: {
                reservations = cottageService.getUpcomingReservations();
                break;
            }
        }

        response.reservations = reservations.stream().map(ReservationDto::new).collect(Collectors.toList());


        return response;
    }

    @Override
    public GetReservationsResponse createResponse() {
        return new GetReservationsResponse();
    }
}

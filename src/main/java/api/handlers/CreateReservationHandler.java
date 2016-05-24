package api.handlers;

import api.contracts.base.ErrorDto;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import java.util.ArrayList;

public class CreateReservationHandler extends BaseHandler<CreateReservationRequest, CreateReservationResponse> {
    @Override
    public ArrayList<ErrorDto> validate(CreateReservationRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public CreateReservationResponse handleBase(CreateReservationRequest request) {
        CreateReservationResponse response = createResponse();



        return response;
    }

    @Override
    public CreateReservationResponse createResponse() {
        return new CreateReservationResponse();
    }
}

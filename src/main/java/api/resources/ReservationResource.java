package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.contracts.reservations.GetReservationsResponse;
import api.handlers.reservations.CreateReservationHandler;
import api.handlers.reservations.GetReservationsHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "reservation")
@Path("/reservation")
@Produces({"application/json; charset=UTF-8"})
public class ReservationResource {
    @Inject
    private GetReservationsHandler getReservationsHandler;
    @Inject
    private CreateReservationHandler createReservationHandler;

    @GET
    @Path("")
    @ApiOperation(value = "Get reservations", response = GetReservationsResponse.class)
    public Response getReservations() {
        BaseRequest request = new BaseRequest();

        GetReservationsResponse response = getReservationsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("")
    @ApiOperation(value = "Creates new reservation and adds a payment.", response = CreateReservationResponse.class)
    public Response createReservation(CreateReservationRequest request) {
        CreateReservationResponse response = createReservationHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}


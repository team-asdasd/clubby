package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.reservations.*;
import api.handlers.reservations.*;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "reservation")
@Path("/reservation")
@Produces({"application/json; charset=UTF-8"})
public class ReservationResource {
    @Inject
    private GetReservationsHandler getReservationsHandler;
    @Inject
    private CreateReservationHandler createReservationHandler;
    @Inject
    private CreateReservationsGroupsHandler createReservationsGroupsHandler;
    @Inject
    private CancelReservationHandler cancelReservationsGroupsHandler;
    @Inject
    private CreateReservationPeriodHandler createReservationPeriodHandler;
    @Inject
    private GetReservationPeriodsHandler getReservationPeriodsHandler;
    @Inject
    private DeleteReservationPeriodHandler deleteReservationPeriodHandler;

    @GET
    @Path("")
    @ApiOperation(value = "Returns reservations. Accepts optional parameters.", response = GetReservationsResponse.class)
    public Response getReservations(@QueryParam("category") @ApiParam(value = "Returns selected category of reservations. Returns all if not provided.") ReservationCategory category) {
        GetReservationsRequest request = new GetReservationsRequest();
        request.category = category;

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

    @DELETE
    @Path("")
    @ApiOperation(value = "Deletes a reservation", response = BaseResponse.class)
    public Response cancelReservation(@QueryParam("id") @ApiParam(value = "Reservation id") int id) {
        CancelReservationRequest request = new CancelReservationRequest();
        request.id = id;

        BaseResponse response = cancelReservationsGroupsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("period")
    @ApiOperation(value = "Creates reservation period", response = BaseResponse.class)
    public Response createReservationPeriod(CreateReservationPeriodRequest request) {
        BaseResponse response = createReservationPeriodHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("period")
    @ApiOperation(value = "Gets reservations groups", response = GetReservationPeriodsResponse.class)
    public Response getReservationPeriods(@QueryParam("from") @ApiParam(value = "Reservation periods start time filter. Returns reservation period if period crosses with date") String from,
                                          @QueryParam("to") @ApiParam(value = "Reservation periods end time filter. Returns reservation period if period crosses with date") String to) {
        GetReservationPeriodsRequest request = new GetReservationPeriodsRequest();
        request.from = from;
        request.to = to;

        BaseResponse response = getReservationPeriodsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @DELETE
    @Path("period")
    @ApiOperation(value = "Deletes reservation period", response = BaseResponse.class)
    public Response deleteReservationPeriod(@QueryParam("id") @ApiParam(value = "Reservation period id") int id) {
        DeleteReservationPeriodRequest request = new DeleteReservationPeriodRequest();
        request.id = id;
        BaseResponse response = deleteReservationPeriodHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}


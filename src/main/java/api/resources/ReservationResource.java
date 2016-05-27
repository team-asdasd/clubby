package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.reservations.*;
import api.handlers.reservations.CreateReservationHandler;
import api.handlers.reservations.CreateReservationsGroupsHandler;
import api.handlers.reservations.GetReservationsHandler;
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
    CreateReservationsGroupsHandler createReservationsGroupsHandler;

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

    @POST
    @Path("groups")
    @ApiOperation(value = "Creates users groups", response = BaseResponse.class)
    public Response createUser() {
        BaseRequest request = new BaseRequest();
        BaseResponse response = createReservationsGroupsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}


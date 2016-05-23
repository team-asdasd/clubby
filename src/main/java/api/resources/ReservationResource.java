package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.reservations.GetReservationsResponse;
import api.handlers.reservations.GetReservationsHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "reservation")
@Path("/reservation")
@Produces({"application/json; charset=UTF-8"})
public class ReservationResource {
    @Inject
    private GetReservationsHandler getReservationsHandler;

    @GET
    @Path("")
    @ApiOperation(value = "", response = GetReservationsResponse.class)
    public Response get() {
        BaseRequest request = new BaseRequest();

        GetReservationsResponse response = getReservationsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}


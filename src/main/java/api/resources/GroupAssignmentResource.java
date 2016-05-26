package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.users.*;
import api.handlers.reservations.CreateReservationsGroupsHandler;
import api.handlers.users.*;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.PATCH;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "groups")
@Path("/goups")
@Produces({"application/json; charset=UTF-8"})
public class GroupAssignmentResource {
    @Inject
    CreateReservationsGroupsHandler createReservationsGroupsHandler;

    @POST
    @ApiOperation(value = "Creates users groups", response = BaseResponse.class)
    public Response createUser() {
        BaseRequest request = new BaseRequest();
        BaseResponse response = createReservationsGroupsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

}

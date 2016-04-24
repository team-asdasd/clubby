package api.resources;

import api.contracts.base.BaseResponse;
import api.contracts.cottages.CreateCottageRequest;
import api.contracts.cottages.GetCottagesRequest;
import api.contracts.cottages.GetCottagesResponse;
import api.handlers.cottages.CreateCottageHandler;
import api.handlers.cottages.GetCottagesHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "cottage")
@Path("/cottage")
@Produces({"application/json"})
@Consumes({"application/json"})
public class CottageResource {
    @EJB
    private GetCottagesHandler getCottagesHandler;

    @EJB
    private CreateCottageHandler createCottagesHandler;

    @GET
    @Path("")
    @ApiOperation(value = "Gets all Cottages.", response = GetCottagesResponse.class)
    public Response getCottages() {
        GetCottagesRequest request = new GetCottagesRequest();

        GetCottagesResponse response = getCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("")
    @ApiOperation(value = "Creates Cottage. Only users with \"administrator\" role can create Cottages. ", response = BaseResponse.class)
    public Response createCottage(CreateCottageRequest request) {
        BaseResponse response = createCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

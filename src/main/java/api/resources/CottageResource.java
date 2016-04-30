package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.cottages.CreateCottageRequest;
import api.contracts.cottages.GetCottagesRequest;
import api.contracts.cottages.GetCottagesResponse;
import api.handlers.cottages.CreateCottageHandler;
import api.handlers.cottages.GetCottagesHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
    @ApiOperation(value = "Returns all Cottages.", response = GetCottagesResponse.class)
    public Response getCottages(@QueryParam("title") @ApiParam(value = "Filters results by title") String title,
                                @QueryParam("beds") @ApiParam(value = "Filters results by number of beds") int beds) {
        GetCottagesRequest request = new GetCottagesRequest();
        request.title = title;
        request.bedcount = beds;

        GetCottagesResponse response = getCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("{cottageId}")
    @ApiOperation(value = "Returns selected Cottage.", response = GetCottagesResponse.class)
    public Response getSingleCottage() {
        GetCottagesRequest request = new GetCottagesRequest();

        GetCottagesResponse response = getCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("")
    @ApiOperation(value = "Creates Cottage", notes = "Roles: [administrator]", response = BaseResponse.class)
    public Response createCottage(CreateCottageRequest request) {
        BaseResponse response = createCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @DELETE
    @Path("{cottageId}")
    @ApiOperation(value = "Removes selected Cottage.", notes = "Roles: [administrator]", response = BaseResponse.class)
    public Response deleteCottage(@PathParam("cottageId") @ApiParam(value = "Id of cottage for deletion", required = true) String cottageId) {
        GetCottagesRequest request = new GetCottagesRequest();
        BaseResponse response = getCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

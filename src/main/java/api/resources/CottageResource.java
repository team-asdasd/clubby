package api.resources;

import api.contracts.base.BaseResponse;
import api.contracts.cottages.*;
import api.handlers.cottages.UpdateCottageHandler;
import api.handlers.cottages.CreateCottageHandler;
import api.handlers.cottages.DeleteCottageHandler;
import api.handlers.cottages.GetCottageHandler;
import api.handlers.cottages.GetCottagesHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "cottage")
@Path("/cottage")
@Produces({"application/json; charset=UTF-8"})
public class CottageResource {
    @Inject
    private GetCottageHandler getCottageHandler;
    @Inject
    private GetCottagesHandler getCottagesHandler;
    @Inject
    private CreateCottageHandler createCottagesHandler;
    @Inject
    private UpdateCottageHandler updateCottageHandler;
    @Inject
    private DeleteCottageHandler deleteCottageHandler;

    @GET
    @Path("")
    @ApiOperation(value = "Returns all cottages.", notes = "Specific roles: []</br>The results can be filtered using optional parameters.</br>See parameters for more details.", response = GetCottagesResponse.class)
    public Response getCottages(@QueryParam("title") @ApiParam(value = "Filters results by title, returns every cottage with given string in the title. Empty string means the filter is not set. Case insensitive.") String title,
                                @QueryParam("beds") @ApiParam(value = "Filters results by number of beds, returns every cottage with given bed count. 0 means the filter is not set") int beds,
                                @QueryParam("dateFrom") @ApiParam(value = "Filters results from date") String dateFrom,
                                @QueryParam("dateTo") @ApiParam(value = "Filters results to date") String dateTo,
                                @QueryParam("priceFrom") @ApiParam(value = "Filters results from price. Price in cents") int priceFrom,
                                @QueryParam("priceTo") @ApiParam(value = "Filters results to price") int priceTo)
    {
        GetCottagesRequest request = new GetCottagesRequest();
        request.title = title;
        request.bedcount = beds;
        request.dateFrom = dateFrom;
        request.dateTo = dateTo;
        request.priceFrom = priceFrom;
        request.priceTo = priceTo;

        GetCottagesResponse response = getCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("{cottageId}")
    @ApiOperation(value = "Returns selected cottage.", notes = "Specific roles: []", response = GetCottageResponse.class)
    public Response getSingleCottage(@PathParam("cottageId") @ApiParam(value = "id of selected cottage", required = true) int cottageId) {
        GetCottageRequest request = new GetCottageRequest();
        request.id = cottageId;

        GetCottageResponse response = getCottageHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("")
    @ApiOperation(value = "Creates cottage", notes = "Specific roles: [administrator]", response = BaseResponse.class)
    public Response createCottage(CreateCottageRequest request) {
        BaseResponse response = createCottagesHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @PUT
    @Path("")
    @ApiOperation(value = "Updates selected cottage.", notes = "Specific roles: [administrator]", response = BaseResponse.class)
    public Response deleteCottage(UpdateCottageRequest request) {
        UpdateCottageResponse response = updateCottageHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @DELETE
    @Path("{cottageId}")
    @ApiOperation(value = "Removes selected cottage.", notes = "Specific roles: [administrator]", response = BaseResponse.class)
    public Response deleteCottage(@PathParam("cottageId") @ApiParam(value = "id of cottage for deletion", required = true) int cottageId) {
        DeleteCottageRequest request = new DeleteCottageRequest();
        request.id = cottageId;

        BaseResponse response = deleteCottageHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

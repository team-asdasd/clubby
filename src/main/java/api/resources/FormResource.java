package api.resources;

import api.contracts.base.BaseResponse;
import api.contracts.form.AddFieldRequest;
import api.contracts.form.GetFormRequest;
import api.handlers.form.AddFieldHandler;
import api.handlers.form.GetFormHandler;
import api.handlers.form.UpdateFieldHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "form")
@Path("/form")
@Produces({"application/json"})
public class FormResource {
    @Inject
    private GetFormHandler getFormHandler;
    @Inject
    private AddFieldHandler addFieldHandler;
    @Inject
    private UpdateFieldHandler updateFieldHandler;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get form fields", response = BaseResponse.class)
    public Response getForm() {

        GetFormRequest request = new GetFormRequest();
        BaseResponse response = getFormHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("field")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create new field", response = BaseResponse.class)
    public Response addField(AddFieldRequest request) {
        BaseResponse response = addFieldHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @PUT
    @Path("field")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update existing field", response = BaseResponse.class)
    public Response updateField(AddFieldRequest request) {
        BaseResponse response = updateFieldHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

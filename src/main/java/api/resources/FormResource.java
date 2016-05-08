package api.resources;

import api.contracts.base.BaseResponse;
import api.contracts.requests.SubmitFormRequest;
import api.handlers.form.SubmitFormHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "form")
@Path("/form")
@Produces({"application/json"})
public class FormResource {
    @Inject
    private SubmitFormHandler submitFormHandler;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Submit membership form", response = BaseResponse.class)
    public Response submitForm(SubmitFormRequest request) {
        BaseResponse response = submitFormHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

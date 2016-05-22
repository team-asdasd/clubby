package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.settings.GetSettingsResponse;
import api.contracts.settings.UpdateSettingsRequest;
import api.handlers.settings.GetSettingsHandler;
import api.handlers.settings.UpdateSettingsHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/settings")
@Api(value = "settings")
@Produces({"application/json; charset=UTF-8"})
public class SettingsResource {
    @Inject
    private GetSettingsHandler getSettingsHandler;
    @Inject
    private UpdateSettingsHandler updateSettingsHandler;

    @GET
    @Path("")
    @ApiOperation(value = "Gets all settings.", response = GetSettingsResponse.class)
    public Response getSettings() {
        BaseRequest request = new BaseRequest();

        GetSettingsResponse response = getSettingsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @PUT
    @Path("")
    @ApiOperation(value = "Update settings.", response = BaseResponse.class)
    public Response putSettings(UpdateSettingsRequest request) {
        BaseResponse response = updateSettingsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}


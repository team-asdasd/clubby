package api.resources;

import api.contracts.requests.GetUserInfoRequest;
import api.contracts.requests.HasPermissionRequest;
import api.contracts.requests.HasRoleRequest;
import api.contracts.responses.GetUserInfoResponse;
import api.contracts.responses.HasPermissionResponse;
import api.contracts.responses.HasRoleResponse;
import api.handlers.GetUserInfoHandler;
import api.handlers.HasPermissionHandler;
import api.handlers.HasRoleHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "user")
@Path("/user")
@Produces({"application/json"})
public class UserResource {
    private final GetUserInfoHandler getUserInfoHandler;
    private final HasPermissionHandler hasPermissionHandler;
    private final HasRoleHandler hasRoleHandler;

    public UserResource() {
        getUserInfoHandler = new GetUserInfoHandler();
        hasPermissionHandler = new HasPermissionHandler();
        hasRoleHandler = new HasRoleHandler();
    }

    @GET
    @ApiOperation(value = "Gets user information for current user.", response = GetUserInfoResponse.class)
    public Response getUserInfo() {
        GetUserInfoRequest request = new GetUserInfoRequest();

        GetUserInfoResponse response = getUserInfoHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("/hasRole/{roleName}")
    @ApiOperation(value = "Checks if current user has specified role.", response = boolean.class)
    public Response hasRole(@PathParam("roleName") String roleName) {
        HasRoleRequest request = new HasRoleRequest();
        request.RoleName = roleName;

        HasRoleResponse response = hasRoleHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.HasRole).build();
    }

    @GET
    @Path("/hasPermission/{permissionName}")
    @ApiOperation(value = "Checks if current user has specified permission.", response = boolean.class)
    public Response hasPermission(@PathParam("permissionName") String permissionName) {
        HasPermissionRequest request = new HasPermissionRequest();
        request.PermissionName = permissionName;

        HasPermissionResponse response = hasPermissionHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.HasPermission).build();
    }
}

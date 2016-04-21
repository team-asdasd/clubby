package api.resources;

import api.contracts.requests.FreeUsernameRequest;
import api.contracts.requests.GetUserInfoRequest;
import api.contracts.requests.HasPermissionRequest;
import api.contracts.requests.HasRoleRequest;
import api.contracts.responses.FreeUsernameResponse;
import api.contracts.responses.GetUserInfoResponse;
import api.contracts.responses.HasPermissionResponse;
import api.contracts.responses.HasRoleResponse;
import api.handlers.users.FreeUsernameHandler;
import api.handlers.users.GetUserInfoHandler;
import api.handlers.users.HasPermissionHandler;
import api.handlers.users.HasRoleHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "user")
@Path("/user")
@Produces({"application/json"})
public class UserResource {
    @EJB
    private GetUserInfoHandler getUserInfoHandler;

    private final HasPermissionHandler hasPermissionHandler;
    private final HasRoleHandler hasRoleHandler;
    private final FreeUsernameHandler freeUsernameHandler;

    public UserResource() {

        hasPermissionHandler = new HasPermissionHandler();
        hasRoleHandler = new HasRoleHandler();
        freeUsernameHandler = new FreeUsernameHandler();
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

    @GET
    @Path("/freeUsername/{username}")
    @ApiOperation(value = "Checks if provided user name is free", response = boolean.class)
    public Response freeUsername(@PathParam("username") String username){
        FreeUsernameRequest request = new FreeUsernameRequest();
        request.UserName = username;

        FreeUsernameResponse response = freeUsernameHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.FreeUserName).build();
    }
}

package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.users.GetUserByIdRequest;
import api.contracts.users.GetUserInfoResponse;
import api.contracts.users.*;
import api.contracts.base.BaseResponse;
import api.handlers.users.*;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.PATCH;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "user")
@Path("/user")
@Produces({"application/json; charset=UTF-8"})
public class UserResource {
    @Inject
    private GetCurrentUserHandler getCurrentUserHandler;
    @Inject
    private CreateUserHandler createUserHandler;
    @Inject
    private UpdateUserHandler updateUserHandler;
    @Inject
    private GetUserByIdHandler getUserByIdHandler;
    @Inject
    private GetAllUsersHandler getAllUsersHandler;
    @Inject
    private HasPermissionHandler hasPermissionHandler;
    @Inject
    private HasRoleHandler hasRoleHandler;
    @Inject
    private SendInvitationEmailHandler sendInvitationEmailHandler;

    @GET
    @ApiOperation(value = "Gets user information for all users.", response = GetAllUsersResponse.class)
    public Response getAllUsers() {
        GetAllUsersRequest request = new GetAllUsersRequest();

        GetAllUsersResponse response = getAllUsersHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("/me")
    @ApiOperation(value = "Gets user information for current user.", response = GetUserInfoResponse.class)
    public Response getCurrentUser() {
        BaseRequest request = new BaseRequest();

        GetUserInfoResponse response = getCurrentUserHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets user information by id.", response = GetUserInfoResponse.class)
    public Response getUserById(@PathParam("id") int id) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.id = id;
        GetUserInfoResponse response = getUserByIdHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }


    @GET
    @Path("/me/hasRole/{roleName}")
    @ApiOperation(value = "Checks if current user has specified role.", response = boolean.class)
    public Response hasRole(@PathParam("roleName") String roleName) {
        HasRoleRequest request = new HasRoleRequest();
        request.roleName = roleName;

        HasRoleResponse response = hasRoleHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.hasRole).build();
    }

    @GET
    @Path("/me/hasPermission/{permissionName}")
    @ApiOperation(value = "Checks if current user has specified permission.", response = boolean.class)
    public Response hasPermission(@PathParam("permissionName") String permissionName) {
        HasPermissionRequest request = new HasPermissionRequest();
        request.permissionName = permissionName;

        HasPermissionResponse response = hasPermissionHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.hasPermission).build();
    }

    @POST
    @ApiOperation(value = "Creates user", response = BaseResponse.class)
    public Response createUser(CreateUserRequest request) {
        BaseResponse response = createUserHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @PATCH
    @ApiOperation(value = "Updates selected user", response = UpdateUserResponse.class)
    public Response updateUser(UpdateUserRequest request) {
        UpdateUserResponse response = updateUserHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("/invite")
    @ApiOperation(value = "Sends invitation email", response = BaseResponse.class)
    public Response sendInvitationEmail(SendInvitationEmailRequest request) {
        BaseResponse response = sendInvitationEmailHandler.handle(request);
        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }


}

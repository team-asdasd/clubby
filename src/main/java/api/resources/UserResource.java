package api.resources;

import api.contracts.requests.GetUserByIdRequest;
import api.contracts.responses.GetUserByIdResponse;
import api.contracts.users.*;
import api.contracts.base.BaseResponse;
import api.handlers.users.*;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "user")
@Path("/user")
@Produces({"application/json"})
public class UserResource {
    @Inject
    private GetUserInfoHandler getUserInfoHandler;
    @Inject
    private CreateUserHandler createUserHandler;
    @Inject
    private GetUserByIdHandler getUserByIdHandler;
    @Inject
    private GetAllUsersHandler getAllUsersHandler;

    private final HasPermissionHandler hasPermissionHandler;
    private final HasRoleHandler hasRoleHandler;

    public UserResource() {
        hasPermissionHandler = new HasPermissionHandler();
        hasRoleHandler = new HasRoleHandler();
    }

    @GET
    @ApiOperation(value = "Gets user information for all users.", response = GetUserInfoResponse.class)
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
        GetUserInfoRequest request = new GetUserInfoRequest();

        GetUserInfoResponse response = getUserInfoHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Gets user information by id.", response = GetUserByIdResponse.class)
    public Response getUserById(@PathParam("id") int id) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.Id = id;
        GetUserByIdResponse response = getUserByIdHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }


    @GET
    @Path("/me/hasRole/{roleName}")
    @ApiOperation(value = "Checks if current user has specified role.", response = boolean.class)
    public Response hasRole(@PathParam("roleName") String roleName) {
        HasRoleRequest request = new HasRoleRequest();
        request.RoleName = roleName;

        HasRoleResponse response = hasRoleHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.HasRole).build();
    }

    @GET
    @Path("/me/hasPermission/{permissionName}")
    @ApiOperation(value = "Checks if current user has specified permission.", response = boolean.class)
    public Response hasPermission(@PathParam("permissionName") String permissionName) {
        HasPermissionRequest request = new HasPermissionRequest();
        request.PermissionName = permissionName;

        HasPermissionResponse response = hasPermissionHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response.HasPermission).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates user", response = boolean.class)
    public Response createUser(CreateUserRequest request) {
        BaseResponse response = createUserHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}

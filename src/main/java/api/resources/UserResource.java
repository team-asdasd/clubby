package api.resources;

import api.contracts.requests.*;
import api.contracts.responses.*;
import api.contracts.responses.base.BaseResponse;
import api.handlers.Recommendation.ConfirmRecommendationHandler;
import api.handlers.Recommendation.SendRecommendationHandler;
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
    @EJB
    private GetUserInfoHandler getUserInfoHandler;
    @EJB
    private CreateUserHandler createUserHandler;
    @Inject
    private ConfirmRecommendationHandler confirmRecommendationHandler;
    @Inject
    private SendRecommendationHandler sendRecommendationrequestHandler;

    private final HasPermissionHandler hasPermissionHandler;
    private final HasRoleHandler hasRoleHandler;


    public UserResource() {

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

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates user", response = boolean.class)
    public Response createUser(CreateUserRequest request){
        BaseResponse response = createUserHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Path("/confirmRec/{recommendationCode}")
    @ApiOperation(value = "Recommends user by recommendation code", response = boolean.class)
    public Response recommend(@PathParam("recommendationCode") String recommendationCode) {
        ConfirmRecommendationRequest request = new ConfirmRecommendationRequest();
        request.recommendationCode = recommendationCode;

        ConfirmRecommendationResponse response = confirmRecommendationHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).build();
    }

    @GET
    @Path("/sendrec/{email}")
    @ApiOperation(value = "Sends recomendation request", response = boolean.class)
    public Response sendRecommendRequest(@PathParam("email") String email) {
        SendRecommendationRequest request = new SendRecommendationRequest();
        request.email = email;

        SendRecommendationResponse response = sendRecommendationrequestHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).build();
    }
}

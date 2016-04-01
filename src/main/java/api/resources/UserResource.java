package api.resources;

import api.contracts.requests.GetUserInfoRequest;
import api.contracts.responses.GetUserInfoResponse;
import api.handlers.GetUserInfoHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "user")
@Path("/user")
@Produces({"application/json"})
public class UserResource {
    private final GetUserInfoHandler _handler;

    public UserResource() {
        _handler = new GetUserInfoHandler();
    }

    @GET
    @ApiOperation(value = "Gets user information for current user.", response = GetUserInfoResponse.class, code = 200)
    public Response getUserInfo() {
        GetUserInfoRequest request = new GetUserInfoRequest();

        GetUserInfoResponse response = _handler.Handle(request);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/hasRole/{roleName}")
    @ApiOperation(value = "Checks if current user has specified role.", response = boolean.class, code = 200)
    public Response hasRole(@PathParam("roleName") String roleName) {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {

            boolean hasRole = currentUser.hasRole(roleName);

            return Response.status(200).entity(hasRole).build();
        }

        return Response.status(401).build();
    }

    @GET
    @Path("/hasPermission/{permissionName}")
    @ApiOperation(value = "Checks if current user has specified permission.", response = boolean.class, code = 200)
    public Response hasPermission(@PathParam("permissionName") String permissionName) {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            boolean hasPermission = currentUser.isPermitted(permissionName);

            return Response.status(200).entity(hasPermission).build();
        }

        return Response.status(401).build();
    }
}

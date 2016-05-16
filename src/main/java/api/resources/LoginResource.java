package api.resources;

import api.contracts.login.FacebookLoginRequest;
import api.contracts.login.FacebookLoginResponse;
import api.handlers.login.FacebookLoginHandler;
import api.handlers.utilities.StatusResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path("/login")
@Produces({"application/json; charset=UTF-8"})
public class LoginResource {
    private FacebookLoginHandler facebookLoginHandler;

    public LoginResource() {
        this.facebookLoginHandler = new FacebookLoginHandler();
    }

    @GET
    @Path("facebook")
    public void facebook(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        FacebookLoginRequest req = new FacebookLoginRequest();
        req.Code = code;

        FacebookLoginResponse resp = facebookLoginHandler.handle(req);

        int statusCode = StatusResolver.getStatusCode(resp);
        if (statusCode == 200) {
            response.sendRedirect(response.encodeRedirectURL("/app"));
        } else {
            response.sendRedirect(response.encodeRedirectURL("/error/401"));
        }
    }
}


package api.resources;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import security.shiro.facebook.FacebookToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/login")
public class LoginController {
    @GET
    @Path("facebook")
    @Produces("application/json")
    public void facebook(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        try {
            String code = request.getParameter("code");
            FacebookToken facebookToken = new FacebookToken(code);
            try {
                SecurityUtils.getSubject().login(facebookToken);
                response.sendRedirect(response.encodeRedirectURL("/"));
            } catch (AuthenticationException ae) {
                throw new Exception(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


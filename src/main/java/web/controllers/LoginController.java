package web.controllers;

import clients.facebook.FacebookSettings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.context.WebContext;
import security.shiro.facebook.FacebookToken;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller("Login")
public class LoginController {
    @PathMapping("")
    public void login(WebContext ctx) throws Exception {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated()) {
            redirectToApp(ctx);
        } else {
            setVars(ctx);
            HttpServletRequest request = ctx.getRequest();
            String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
            if (shiroLoginFailure != null) {
                if (shiroLoginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
                    ctx.setVariable("errorMessage", "Your account has been disabled, contact administrator");
                } else {
                    ctx.setVariable("errorMessage", "Incorrect email or password");
                }
            }
        }
        Sender.sendView(ctx, "auth/login");
    }

    private void redirectToApp(WebContext ctx) throws IOException {
        ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/app"));
    }

    @PathMapping("facebook")
    public void facebook(WebContext ctx) throws Exception {

        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated()) {
            redirectToApp(ctx);
        } else {
            setVars(ctx);
            String code = ctx.getRequest().getParameter("code");
            FacebookToken facebookToken = new FacebookToken(code);
            try {
                SecurityUtils.getSubject().login(facebookToken);
                redirectToApp(ctx);
            } catch (LockedAccountException l) {
                ctx.setVariable("errorMessage", "Your account has been disabled, contact administrator");
            } catch (Exception e) {
                ctx.setVariable("errorMessage", "Login failed");
            }
            Sender.sendView(ctx, "auth/login");
        }

    }

    private void setVars(WebContext ctx) {
        ctx.setVariable("fbAppId", FacebookSettings.getAppId());
        ctx.setVariable("fbRedirect", FacebookSettings.getRedirectUrl());
        ctx.setVariable("pageTitle", "Login");
    }
}

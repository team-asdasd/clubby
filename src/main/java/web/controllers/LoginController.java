package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.context.WebContext;
import clients.facebook.FacebookSettings;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

@Controller("Login")
public class LoginController {
    @PathMapping("")
    public void login(WebContext ctx) throws Exception {
        Subject sub = SecurityUtils.getSubject();

        if (sub.isAuthenticated()) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/app"));
        } else {
            ctx.setVariable("fbAppId", FacebookSettings.getAppId());
            ctx.setVariable("fbRedirect", FacebookSettings.getRedirectUrl());
            ctx.setVariable("pageTitle", "Prisijungti");

            Sender.sendView(ctx, "auth/login");
        }
    }
}

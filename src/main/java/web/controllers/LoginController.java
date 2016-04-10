package web.controllers;

import org.thymeleaf.context.WebContext;
import security.shiro.facebook.FacebookSettings;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

/**
 * Created by Mindaugas on 03/04/2016.
 */
@Controller("Login")
public class LoginController {
    @PathMapping("")
    public void login(WebContext ctx) throws Exception {
        ctx.setVariable("fbAppId", FacebookSettings.getAppId());
        ctx.setVariable("fbRedirect", FacebookSettings.getRedirectUrl());
        ctx.setVariable("pageTitle", "Prisijungti");

        Sender.sendView(ctx, "auth/login");
    }
}

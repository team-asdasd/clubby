package web.controllers;

import org.thymeleaf.context.WebContext;
import security.shiro.facebook.FacebookSettings;

/**
 * Created by Mindaugas on 03/04/2016.
 */
public class LoginController extends ControllerBase {
    @Override
    protected String processCore(WebContext ctx) {
        ctx.setVariable("fbAppId", FacebookSettings.getAppId());
        ctx.setVariable("fbRedirect", FacebookSettings.getRedirectUrl());
        ctx.setVariable("pageTitle", "Prisijungti");

        return "auth/login";
    }
}

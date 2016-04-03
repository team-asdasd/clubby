package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.thymeleaf.context.WebContext;

import java.util.Date;

/**
 * Created by Mindaugas on 03/04/2016.
 */
public class AppController extends ControllerBase {
    @Override
    protected String processCore(WebContext ctx) {
        ctx.setVariable("pageTitle", "Super App");
        ctx.setVariable("today", new Date().toString());
        ctx.setVariable("userName", SecurityUtils.getSubject().getPrincipal().toString());
        ctx.setVariable("layout", "_baseLayout");

        return "app/index";
    }
}

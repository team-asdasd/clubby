package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

@Controller
public class HomeController {
    @PathMapping("")
    public void index(WebContext ctx) throws Exception {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated()) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/app"));
        }
        ctx.setVariable("pageTitle", "Welcome");
        ctx.setVariable("layout", "_baseLayout");

        Sender.sendView(ctx, "index");
    }
}
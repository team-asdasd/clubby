package web.controllers;

import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

@Controller
public class HomeController{
    @PathMapping("")
    public void index(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Pagrindinis");
        ctx.setVariable("layout","_baseLayout");

        Sender.sendView(ctx, "index");
    }
}

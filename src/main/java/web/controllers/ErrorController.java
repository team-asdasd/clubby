package web.controllers;

import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

/**
 * Created by Mindaugas on 07/04/2016.
 */
@Controller("Error")
public class ErrorController {
    @PathMapping("500")
    public void Error500(WebContext ctx) throws Exception {
        ctx.setVariable("layout",null);
        String exception = ctx.getRequest().getAttribute("exception").toString();
        ctx.setVariable("exception", exception);

        Sender.sendView(ctx, "errors/500");
    }

    @PathMapping("404")
    public void Error404(WebContext ctx) throws Exception {
        ctx.setVariable("layout",null);
        String path = ctx.getRequest().getAttribute("path").toString();
        ctx.setVariable("path", path);
        ctx.setVariable("host", ctx.getRequest().getRemoteHost());

        Sender.sendView(ctx, "errors/404");
    }
}

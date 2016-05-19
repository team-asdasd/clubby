package web.controllers;

import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

@Controller("Error")
public class ErrorController {
    @PathMapping("500")
    public void Error500(WebContext ctx) throws Exception {
        ctx.setVariable("layout", null);

        setAttribute(ctx, "exception");

        Sender.sendView(ctx, "errors/500");
    }

    @PathMapping("404")
    public void Error404(WebContext ctx) throws Exception {
        ctx.setVariable("layout", null);
        setAttribute(ctx, "path");
        
        ctx.setVariable("host", ctx.getRequest().getRemoteHost());

        Sender.sendView(ctx, "errors/404");
    }

    @PathMapping("401")
    public void Error401(WebContext ctx) throws Exception {
        ctx.setVariable("layout", null);
        setAttribute(ctx, "path");

        ctx.setVariable("host", ctx.getRequest().getRemoteHost());

        Sender.sendView(ctx, "errors/401");
    }

    private void setAttribute(WebContext ctx, String attribute) {
        Object pathO = ctx.getRequest().getAttribute(attribute);
        if (pathO != null) {
            String path = pathO.toString();
            ctx.setVariable(attribute, path);
        }
    }
}

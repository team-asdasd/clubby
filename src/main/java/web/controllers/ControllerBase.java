package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mindaugas on 03/04/2016.
 */
public abstract class ControllerBase {

    public void process(
            HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext, TemplateEngine templateEngine)
            throws Exception{

        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        Subject sub = SecurityUtils.getSubject();

        String view = processCore(ctx);

        ctx.setVariable("authenticated", sub.isAuthenticated());
        ctx.setVariable("administrator", sub.isPermitted("administrator"));

        Object layout = ctx.getVariable("layout");
        if(layout != null){
            ctx.setVariable("view", view);

            view = layout.toString();
        }

        templateEngine.process(view, ctx, response.getWriter());
    }

    protected abstract String processCore(WebContext ctx);
}

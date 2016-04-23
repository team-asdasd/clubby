package web.helpers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.context.WebContext;

/**
 * Created by Mindaugas on 06/04/2016.
 */
public class Sender {
    public static void sendView(WebContext ctx, String view) throws Exception {

        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        ctx.getResponse().setHeader("Pragma", "no-cache");
        ctx.getResponse().setHeader("Cache-Control", "no-cache");
        ctx.getResponse().setDateHeader("Expires", 0);

        Subject sub = SecurityUtils.getSubject();

        try{
            ctx.setVariable("authenticated", sub.isAuthenticated());
            ctx.setVariable("administrator", sub.isPermitted("administrator"));
        }catch (Exception e){
            //do nothing in case db error
            ctx.setVariable("administrator", false);
        }

        Object layout = ctx.getVariable("layout");
        if(layout != null){
            ctx.setVariable("view", view);

            view = layout.toString();
        }

        TemplateEngineFactory.getTemplateEngine().process(view, ctx, ctx.getResponse().getWriter());
    }

    public static void sendResponse(WebContext ctx, Object content, int code, String contetnType) throws Exception {
        ctx.getResponse().setContentType(contetnType);
        ctx.getResponse().setHeader("Pragma", "no-cache");
        ctx.getResponse().setHeader("Cache-Control", "no-cache");
        ctx.getResponse().setDateHeader("Expires", 0);

        ctx.getResponse().setStatus(code);
        ctx.getResponse().getWriter().write(content.toString());
        ctx.getResponse().getWriter().flush();
        ctx.getResponse().getWriter().close();
    }
}

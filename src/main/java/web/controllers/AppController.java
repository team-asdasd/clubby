package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

@Controller("App")
public class AppController {
    @PathMapping("")
    public void test(WebContext ctx) throws Exception {

        if(SecurityUtils.getSubject().hasRole("potentialCandidate")) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/register"));
            return;
        }
        Sender.sendView(ctx, "app/index");
    }

    @PathMapping("Data")
    public void testJson(WebContext ctx) throws Exception {
        String json = "{name : \"Chuck Norris\", " +
                "img: \"http://icndb.com/wp-content/uploads/2011/01/icndb_logo2.png\"," +
                "web:\"http://www.icndb.com/api/\"}";

        Sender.sendResponse(ctx, json, 1,"application/JSON");
    }
}

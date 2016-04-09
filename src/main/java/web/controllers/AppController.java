package web.controllers;

import org.apache.shiro.SecurityUtils;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

import java.util.Date;

/**
 * Created by Mindaugas on 03/04/2016.
 */
@Controller("App")
public class AppController {
    @PathMapping("")
    public void test(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Super App");
        ctx.setVariable("today", new Date().toString());
        ctx.setVariable("userName", SecurityUtils.getSubject().getPrincipal().toString());
        ctx.setVariable("layout", "_baseLayout");

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

package web.controllers;

import org.thymeleaf.context.WebContext;

/**
 * Created by Mindaugas on 03/04/2016.
 */

public class HomeController extends ControllerBase {

    public HomeController() {
        super();
    }

    @Override
    protected String processCore(WebContext ctx) {
        ctx.setVariable("pageTitle", "Pagrindinis");
        ctx.setVariable("layout","_baseLayout");

        return "index";
    }
}

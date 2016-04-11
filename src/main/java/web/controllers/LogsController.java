package web.controllers;

import org.thymeleaf.context.WebContext;
import sun.misc.Regexp;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mindaugas on 10/04/2016.
 */
@Controller("Logs")
public class LogsController {

    @PathMapping("")
    public void index(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Logai");
        ctx.setVariable("layout","shared/_noFooterLayout");

        File file = new File("logs/app.log");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        ArrayList<String[]> logs = new ArrayList<>();

        String str = new String(data, "UTF-8");
        String[] lines = str.split("\\r?\\n");

        for(String line : lines){
            logs.add(line.split("\\$sep\\$"));
        }

        ctx.setVariable("logs",logs);

        Sender.sendView(ctx, "logs/logs");
    }

    @PathMapping("clear")
    public void clearLogs(WebContext ctx) throws IOException {
        File file = new File("logs/app.log");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/logs"));
    }
}

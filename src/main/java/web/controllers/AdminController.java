package web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.PathMapping;
import web.helpers.Sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@Controller("Admin")
public class AdminController {
    final Logger logger = LogManager.getLogger(getClass().getName());

    @PathMapping("logs")
    public void logsIndex(WebContext ctx) throws Exception {
        ArrayList<String[]> logs = new ArrayList<>();

        try {
            File file = new File("logs/app.log");

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            String str = new String(data, "UTF-8");
            String[] lines = str.split("\\r?\\n");

            for (String line : lines) {
                logs.add(line.split("\\$sep\\$"));
            }
        }catch (Exception e){
            logger.error(e);
        }

        ctx.setVariable("logs",logs);
        ctx.setVariable("pageTitle", "Logai");
        ctx.setVariable("layout","shared/_noFooterLayout");

        Sender.sendView(ctx, "admin/logs");
    }

    @PathMapping("clearlogs")
    public void clearLogs(WebContext ctx) throws IOException {
        File file = new File("logs/app.log");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("logs"));
    }

    @PathMapping("swagger")
    public void swaggerIndex(WebContext ctx) throws Exception {

        Sender.sendView(ctx, "admin/swagger");
    }

}

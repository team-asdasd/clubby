package web.controllers;

import api.contracts.cottages.GetCottagesResponse;
import api.contracts.users.GetAllUsersResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.HttpClient;
import web.helpers.PathMapping;
import web.helpers.Sender;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

@Controller("Admin")
public class AdminController {
    final Logger logger = LogManager.getLogger(getClass().getName());

    @PathMapping("")
    public void adminIndex(WebContext ctx) throws Exception {
        ctx.getResponse().sendRedirect("/admin/dashboard");
    }

    @PathMapping("dashboard")
    public void dashboardIndex(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Admin");
        ctx.setVariable("navbarSearch", false);
        ctx.setVariable("layout", "admin/shared/_adminLayout");

        Sender.sendView(ctx, "admin/dashboard");
    }

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
        } catch (Exception e) {
            logger.error(e);
        }

        Collections.reverse(logs); // Reverse so the latest entries are on top

        ctx.setVariable("logs", logs);
        ctx.setVariable("pageTitle", "Logs");
        ctx.setVariable("navbarSearch", true);
        ctx.setVariable("layout", "admin/shared/_adminLayout");

        Sender.sendView(ctx, "admin/logs");
    }

    @PathMapping("users")
    public void usersIndex(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Users");
        ctx.setVariable("navbarSearch", true);
        ctx.setVariable("layout", "admin/shared/_adminLayout");

        Sender.sendView(ctx, "admin/users");
    }

    @PathMapping("clearlogs")
    public void clearLogs(WebContext ctx) throws IOException {
        File file = new File("logs/app.log");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/admin/logs"));
    }

    @PathMapping("swagger")
    public void swaggerIndex(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "API Docs");
        ctx.setVariable("navbarSearch", false);
        ctx.setVariable("layout", "admin/shared/_adminLayout");

        Sender.sendView(ctx, "admin/swagger");
    }

    @PathMapping("cottages")
    public void cottages(WebContext ctx) throws Exception {
        String cookie = ctx.getRequest().getHeader("Cookie");
        GetCottagesResponse response = HttpClient.sendGetRequest("/api/cottage", GetCottagesResponse.class, null, cookie);

        if (response.Errors == null || response.Errors.size() == 0) {
            ctx.setVariable("cottages", response.Cottages);
            ctx.setVariable("pageTitle", "Cottages");
            ctx.setVariable("navbarSearch", true);
            ctx.setVariable("layout", "admin/shared/_adminLayout");

            Sender.sendView(ctx, "admin/cottages");
        } else {
            ctx.getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PathMapping("settings")
    public void settings(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Settings");
        ctx.setVariable("navbarSearch", false);
        ctx.setVariable("layout", "admin/shared/_adminLayout");

        Sender.sendView(ctx, "admin/settings");
    }

}

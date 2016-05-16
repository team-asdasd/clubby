package web.controllers;

import api.contracts.payments.GetPaymentInfoResponse;
import api.contracts.payments.GetPayseraParamsResponse;
import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.HttpClient;
import web.helpers.PathMapping;
import web.helpers.Sender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mindaugas on 03/04/2016.
 */

@Controller("pay")
public class PaymentController {

    final Logger logger = LogManager.getLogger(getClass().getName());

    @PathMapping("")
    public void index(WebContext ctx) throws Exception {
        //todo check if user can pay (ex: membership payment only ones in year)
        String paymentId = ctx.getRequest().getParameter("payment");

        if (paymentId == null || paymentId.isEmpty()) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/"));
        }
        String cookie = ctx.getRequest().getHeader("Cookie");
        GetPaymentInfoResponse response = HttpClient.sendGetRequest("/api/payments/" + paymentId, GetPaymentInfoResponse.class, null, cookie);

        if (response.Errors == null || response.Errors.size() == 0) {
            ctx.setVariable("payment", response.paymentInfoDto);
            ctx.setVariable("pageTitle", "Pay");
            ctx.setVariable("layout", "_baseLayout");

            Sender.sendView(ctx, "payment/index");
        } else {
            ctx.getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    @PathMapping("paysera")
    public void paysera(WebContext ctx) throws Exception {
        //todo check if user can pay (ex: membership payment only ones in year)
        String paymentId = ctx.getRequest().getParameter("payment");

        if (paymentId == null || paymentId.isEmpty()) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/"));
        }

        String cookie = ctx.getRequest().getHeader("Cookie");
        GetPayseraParamsResponse response = HttpClient.sendGetRequest("/api/paysera/parameters/" + paymentId, GetPayseraParamsResponse.class, null, cookie);

        HttpServletResponse httpResp = ctx.getResponse();

        String body = String.format("data=%s&sign=%s", response.Data, response.Sign);

        httpResp.sendRedirect("https://www.paysera.com/pay?" + body);
    }

    @PathMapping("accepted")
    public void accepted(WebContext ctx) throws Exception {
        sendInfoToCallback(ctx);
        ctx.setVariable("pageTitle", "Success");
        ctx.setVariable("layout", "_baseLayout");

        Sender.sendView(ctx, "payment/accept");
    }

    @PathMapping("cancelled")
    public void cancelled(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Failed");
        ctx.setVariable("layout", "_baseLayout");

        Sender.sendView(ctx, "payment/cancel");
    }

    private void sendInfoToCallback(WebContext ctx) {
        try {
            HttpServletRequest request = ctx.getRequest();
            Map<String, String> params = new HashMap<>();
            //to list :)
            for (String name : Collections.list(request.getParameterNames())) {
                params.put(name, request.getParameter(name));
            }

            HttpClient.sendGetRequest("/api/paysera/callback", String.class, params, null);

        } catch (Exception e) {
            //do nothing just log
            logger.info(e);
        }
    }
}

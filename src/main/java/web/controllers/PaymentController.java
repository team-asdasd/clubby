package web.controllers;

import api.contracts.payments.GetPaymentInfoResponse;
import api.contracts.payments.GetPayseraParamsResponse;
import api.contracts.responses.base.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.context.WebContext;
import web.helpers.Controller;
import web.helpers.HttpClient;
import web.helpers.PathMapping;
import web.helpers.Sender;

import javax.servlet.http.HttpServletResponse;

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

        if(paymentId == null || paymentId.isEmpty()){
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/"));
        }
        String cookie = ctx.getRequest().getHeader("Cookie");
        GetPaymentInfoResponse response = HttpClient.sendGetRequest("/api/payments/payment/"+paymentId, GetPaymentInfoResponse.class, cookie);

        ctx.setVariable("payment", response.paymentInfoDto);
        ctx.setVariable("pageTitle", "Pay");
        ctx.setVariable("layout","_baseLayout");

        Sender.sendView(ctx, "payment/index");
    }


    @PathMapping("paysera")
    public void paysera(WebContext ctx) throws Exception {
        //todo check if user can pay (ex: membership payment only ones in year)
        String paymentId = ctx.getRequest().getParameter("payment");

        if(paymentId == null || paymentId.isEmpty()){
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/"));
        }

        String cookie = ctx.getRequest().getHeader("Cookie");
        GetPayseraParamsResponse response = HttpClient.sendGetRequest("/api/payments/parameters/"+paymentId, GetPayseraParamsResponse.class, cookie);

        HttpServletResponse httpResp = ctx.getResponse();

        String body = String.format("data=%s&sign=%s",response.Data, response.Sign);

        httpResp.sendRedirect("https://www.paysera.com/pay?"+body);
    }

    @PathMapping("accepted")
    public void accepted(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Success");
        ctx.setVariable("layout","_baseLayout");

        Sender.sendView(ctx, "payment/accept");
    }

    @PathMapping("cancelled")
    public void cancelled(WebContext ctx) throws Exception {
        ctx.setVariable("pageTitle", "Failed");
        ctx.setVariable("layout","_baseLayout");

        Sender.sendView(ctx, "payment/cancel");
    }
}

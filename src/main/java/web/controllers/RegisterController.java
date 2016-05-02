package web.controllers;

import api.contracts.responses.base.BaseResponse;
import api.contracts.responses.base.ErrorCodes;
import api.helpers.Parser;
import org.thymeleaf.context.WebContext;
import web.contracts.RegistrationRequest;
import web.helpers.*;

/**
 * Created by Mindaugas on 22/04/2016.
 */
@Controller("Register")
public class RegisterController {
    @PathMapping("")
    public void register(WebContext ctx) throws Exception {

        RegistrationRequest request = Parser.fromQueryString(ctx.getRequest().getReader().readLine(), RegistrationRequest.class);

        if(validate(request)){
            BaseResponse response = HttpClient.postJson("/api/user/create", request, null, BaseResponse.class);

            if(response.Errors == null || response.Errors.size() == 0){
                ctx.setVariable("pageTitle", "Register");
                ctx.setVariable("layout","shared/_noFooterLayout");
                ctx.setVariable("registred", true);

                Sender.sendView(ctx, "auth/register");
            }else{
                setVariables(request,ctx);
                ctx.setVariable("pageTitle", "Register");
                ctx.setVariable("layout","shared/_noFooterLayout");

                if(response.Errors.stream().anyMatch(errorDto -> errorDto.Code == ErrorCodes.DUPLICATE_USERNAME)){
                    ctx.setVariable("duplicateUserName", true);
                }
                if(response.Errors.stream().anyMatch(errorDto -> errorDto.Code == ErrorCodes.DUPLICATE_EMAIL)){
                    ctx.setVariable("duplicateEmail", true);
                }

                Sender.sendView(ctx, "auth/register");
            }
        }
        else if(org.apache.shiro.SecurityUtils.getSubject().isAuthenticated() == true){
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/"));
        }
        else{
            ctx.setVariable("pageTitle", "Register");
            ctx.setVariable("layout","shared/_noFooterLayout");

            Sender.sendView(ctx, "auth/register");
        }
    }

    private void setVariables(RegistrationRequest request, WebContext ctx){
        ctx.setVariable("firstName",request.firstName);
        ctx.setVariable("lastName",request.lastName);
        ctx.setVariable("email",request.email);
        ctx.setVariable("userName",request.userName);
    }

    private boolean validate(RegistrationRequest request){
        boolean valid =
                request != null
                && request.password !=null
                && request.passwordConfirm != null
                && request.userName != null
                && request.email != null
                && request.firstName != null
                && request.lastName != null
                && request.password.length() > 5
                && request.password.equals(request.passwordConfirm);

        return valid;
    }
}

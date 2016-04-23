package web.controllers;

import com.google.api.client.http.HttpResponse;
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
            HttpResponse response = HttpClient.postJson("/api/user/create", request, null);
        }

        Sender.sendView(ctx, "auth/register");

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

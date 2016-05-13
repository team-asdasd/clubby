package web.controllers;

import api.business.entities.User;
import api.business.services.UserService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.dto.FormState;
import api.contracts.form.SubmitFormRequest;
import api.helpers.Parser;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.thymeleaf.context.WebContext;
import web.helpers.*;

@Controller("Form")
public class FormController {
    FormStateHelper formState;
    FormState state;
    User user;

    @PathMapping("")
    public void submit(WebContext ctx) throws Exception {

        Subject sub = SecurityUtils.getSubject();
        if (!sub.hasRole("potentialCandidate")) {
            ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/app"));
            return;
        }
        user = BeanProvider.getContextualReference(IUserService.class, true).getByUsername(sub.getPrincipal().toString());
        SubmitFormRequest request = Parser.fromQueryString(ctx.getRequest().getReader().readLine(), SubmitFormRequest.class);
        formState = BeanProvider.getDependent(FormStateHelper.class).get();
        state = formState.getFormState();
        if (validate(request)) {
            BaseResponse response = HttpClient.postJson("/api/form/", request, ctx.getRequest().getHeader("Cookie"), BaseResponse.class);

            if (response.Errors == null || response.Errors.size() == 0) {
                ctx.getResponse().sendRedirect(ctx.getResponse().encodeRedirectURL("/app"));
                return;
            }
        }
        setVariables(request, ctx);
        ctx.setVariable("pageTitle", "Membership form");
        ctx.setVariable("layout", "shared/_noFooterLayout");
        Sender.sendView(ctx, "auth/form");
    }

    private void setVariables(SubmitFormRequest request, WebContext ctx) {

        ctx.setVariable("isPhoneNumber", state.PhoneNumber);
        ctx.setVariable("isAddress", state.Address);
        ctx.setVariable("isBirthDate", state.BirthDate);
        ctx.setVariable("isAbout", state.About);
        if (!user.isFacebookUser())
            ctx.setVariable("isPhoto", state.Photo);
        else  ctx.setVariable("isPhoto", false);
//        ctx.setVariable("phoneNumber", request.PhoneNumber);
//        ctx.setVariable("address", request.Address);
//        ctx.setVariable("birthDate", request.Birthdate);
//        ctx.setVariable("photoUrl", request.Photo);
//        ctx.setVariable("about", request.About);
    }

    private boolean validate(SubmitFormRequest request) {
//        if ((state.PhoneNumber && request.PhoneNumber == null) ||
//                (state.Address && request.Address == null) ||
//                (state.BirthDate && request.Birthdate == null))
            return false;

    }
}

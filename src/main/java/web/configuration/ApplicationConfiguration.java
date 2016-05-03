package web.configuration;

import javax.servlet.ServletContext;

import web.controllers.*;
import web.helpers.RequestsForwarder;
import web.helpers.TemplateEngineFactory;

import java.util.HashSet;

/**
 * Created by Mindaugas on 03/04/2016.
 */
public class ApplicationConfiguration {
    private RequestsForwarder requestsForwarder;

    public ApplicationConfiguration(final ServletContext servletContext)  {
        super();

        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(AppController.class);
        classes.add(HomeController.class);
        classes.add(LoginController.class);
        classes.add(ErrorController.class);
        classes.add(AdminController.class);
        classes.add(RegisterController.class);
        classes.add(PaymentController.class);

        try {
            requestsForwarder = new RequestsForwarder(classes);
        } catch (Exception e) {
            //because java
            e.printStackTrace();
        }
        TemplateEngineFactory.setupTemplateEngineFactory(servletContext);
    }

    public RequestsForwarder getForwarder(){
        return requestsForwarder;
    }
}
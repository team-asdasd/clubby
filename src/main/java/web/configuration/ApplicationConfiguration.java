package web.configuration;

import javax.servlet.ServletContext;

import web.controllers.AppController;
import web.controllers.ErrorController;
import web.controllers.HomeController;
import web.controllers.LoginController;
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
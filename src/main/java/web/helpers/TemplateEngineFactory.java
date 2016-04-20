package web.helpers;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

/**
 * Created by Mindaugas on 06/04/2016.
 */
public class TemplateEngineFactory {
    private static TemplateEngine _templateEngine;

    public static void setupTemplateEngineFactory(final ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode("HTML");
        templateResolver.setPrefix("/public/");
        templateResolver.setSuffix(".html");

        String url = System.getenv("OPENSHIFT_APP_DNS");
        if (url == null || url.isEmpty()) {
            templateResolver.setCacheable(false);
        } else {
            templateResolver.setCacheTTLMs(3600000L);
            templateResolver.setCacheable(true);
        }
        
        _templateEngine = new TemplateEngine();
        _templateEngine.setTemplateResolver(templateResolver);
    }

    public static TemplateEngine getTemplateEngine() {
        return _templateEngine;
    }
}

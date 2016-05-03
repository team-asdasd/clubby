package api.configuration;

import api.resources.*;
import io.swagger.jaxrs.config.BeanConfig;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

public class ApplicationConfiguration extends Application {

    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public ApplicationConfiguration() {

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});

        String url = System.getenv("OPENSHIFT_APP_DNS");
        if(url == null || url.isEmpty()){
            url = "localhost:8080";
        }

        beanConfig.setHost(url);
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("api.resources");
        beanConfig.setScan(true);

        classes.add(UserResource.class);
        classes.add(LoginResource.class);
        classes.add(PaymentsResource.class);
        classes.add(RecommendationResource.class);
        classes.add(CottageResource.class);
        classes.add(PayseraResource.class);

        classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}

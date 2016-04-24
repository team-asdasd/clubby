package web.helpers;

import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by Mindaugas on 06/04/2016.
 */
public class RequestsForwarder {
    private Set<Class<?>> classes;
    private HashMap<Integer,Function<WebContext,String>> routesMap = new HashMap<>();

    public RequestsForwarder(Set<Class<?>> classes) throws Exception {
        this.classes = classes;
        setMap();
    }

    public Boolean forward(final HttpServletRequest request,final HttpServletResponse response,
                           final ServletContext servletContext) throws IOException {

        Boolean found = false;
        String path = getRequestPath(request).substring(1);
        if(path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        int hash = path.hashCode();
        if(routesMap.containsKey(hash)){
            forward(request,response,servletContext,hash);
            found = true;
        }

        return found;
    }

    public Boolean forward(String path, final HttpServletRequest request,final HttpServletResponse response,
                           final ServletContext servletContext){
        Boolean found = false;
        path = path.substring(1);
        if(path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        int hash = path.hashCode();
        if(routesMap.containsKey(hash)){
            forward(request,response,servletContext,hash);
            found = true;
        }

        return found;
    }

    private void forward(final HttpServletRequest request,final HttpServletResponse response,
                         final ServletContext servletContext, int hash){

        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        String exceptionMessage = routesMap.get(hash).apply(ctx);

        if(exceptionMessage != null){
            throw new RuntimeException(exceptionMessage);
        }
    }

    private void setMap() throws Exception{
        for (Class<?> controllerClass : classes) {
            String ctrName = controllerClass.getAnnotation(Controller.class).value().toLowerCase();
            Object controller = controllerClass.newInstance();

            Method[] allMethods = controllerClass.getDeclaredMethods();
            for (Method method : allMethods) {
                if(method.getModifiers() == 1) {
                    String methodPath = method.getAnnotation(PathMapping.class).value().toLowerCase();

                    Function<WebContext,String> consumer = CreateConsumer(controller,method);
                    String stringKey = methodPath.isEmpty() ? ctrName : ctrName + "/" + methodPath;

                    routesMap.put(stringKey.hashCode(),consumer);
                }
            }
        }
    }

    private Function<WebContext,String> CreateConsumer(Object controller, Method method){
        Function<WebContext,String> consumer = webContext -> {
            String exception = null;

            try {
                method.invoke(controller,webContext);
            } catch (IllegalAccessException e) {
                //todo log
                exception = ExceptionHelper.exceptionStacktraceToString(e);
            } catch (InvocationTargetException e) {
                //todo log
                exception = ExceptionHelper.exceptionStacktraceToString(e);
            }
            return exception;
        };

        return consumer;
    }

    private static String getRequestPath(final HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        final String contextPath = request.getContextPath();

        final int fragmentIndex = requestURI.indexOf(';');
        if (fragmentIndex != -1) {
            requestURI = requestURI.substring(0, fragmentIndex);
        }

        if (requestURI.startsWith(contextPath)) {
            return requestURI.substring(contextPath.length());
        }
        return requestURI;
    }
}

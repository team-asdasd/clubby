package web.filter;

import org.thymeleaf.TemplateEngine;
import web.configuration.ApplicationConfiguration;
import web.controllers.ControllerBase;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mindaugas on 03/04/2016.
 */

public class ViewsFilter implements Filter {


    private ServletContext servletContext;
    private ApplicationConfiguration application;


    public ViewsFilter() {
        super();
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.application = new ApplicationConfiguration(this.servletContext);
    }




    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        if (!process((HttpServletRequest)request, (HttpServletResponse)response)) {
            chain.doFilter(request, response);
        }
    }




    public void destroy() {
        // nothing to do
    }


    private boolean process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {

            // This prevents triggering engine executions for resource URLs
            if (request.getRequestURI().startsWith("/css") ||
                    request.getRequestURI().startsWith("/images") ||
                    request.getRequestURI().startsWith("/favicon")) {
                return false;
            }


            /*
             * Query controller/URL mapping and obtain the controller
             * that will process the request. If no controller is available,
             * return false and let other filters/servlets process the request.
             */
            ControllerBase controller = this.application.resolveControllerForRequest(request);
            if (controller == null) {
                return false;
            }

            /*
             * Obtain the TemplateEngine instance.
             */
            TemplateEngine templateEngine = this.application.getTemplateEngine();

            /*
             * Write the response headers
             */
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            /*
             * Execute the controller and process view template,
             * writing the results to the response writer.
             */
            controller.process(
                    request, response, this.servletContext, templateEngine);

            return true;

        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException ignored) {
                // Just ignore this
            }
            throw new ServletException(e);
        }

    }



}

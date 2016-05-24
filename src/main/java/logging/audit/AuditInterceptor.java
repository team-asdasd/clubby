package logging.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Audit
public class AuditInterceptor {
    private final Logger logger = LogManager.getLogger("auditLogger");

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated())
            logger.info(String.format("%s administrator: %s userId: %s", ctx.getMethod(), sub.hasRole("administrator")
                    , sub.getPrincipal().toString()));
        else logger.info(String.format("%s unauthenticated", ctx.getMethod()));
        return ctx.proceed();
    }
}

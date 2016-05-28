package security.shiro.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomAuthenticationListener implements AuthenticationListener {
    final Logger logger = LogManager.getLogger(getClass().getName());
    @Override
    public void onSuccess(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        logger.info(String.format("User %s successfully logged in", authenticationInfo.getPrincipals()));
}

    @Override
    public void onFailure(AuthenticationToken authenticationToken, AuthenticationException e) {
        logger.info(String.format("User %s failed to login", authenticationToken.getPrincipal()));
    }

    @Override
    public void onLogout(PrincipalCollection principalCollection) {
        logger.info(String.format("User %s  logged out", principalCollection.getPrimaryPrincipal()));
    }
}

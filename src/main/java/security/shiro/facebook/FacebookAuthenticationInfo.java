package security.shiro.facebook;

import clients.facebook.responses.FacebookUserDetails;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.ArrayList;
import java.util.Collection;

public class FacebookAuthenticationInfo implements AuthenticationInfo {

    private final SimplePrincipalCollection principalCollection;

    public FacebookAuthenticationInfo(int id, String realmName) {
        Collection<String> principals = new ArrayList<>();
        principals.add(Integer.toString(id));

        this.principalCollection = new SimplePrincipalCollection(principals, realmName);
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return principalCollection;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}

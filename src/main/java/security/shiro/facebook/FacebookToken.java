package security.shiro.facebook;

import org.apache.shiro.authc.AuthenticationToken;

public class FacebookToken implements AuthenticationToken {
    private String code;

    public FacebookToken(String code) {
        setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Object getPrincipal() {
        return null; // Facebook does the job.
    }

    @Override
    public Object getCredentials() {
        return null; // Facebook does the job.
    }
}

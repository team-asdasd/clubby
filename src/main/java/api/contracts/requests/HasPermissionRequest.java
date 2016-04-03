package api.contracts.requests;

import api.contracts.requests.base.BaseRequest;
import org.apache.shiro.authz.Permission;

public class HasPermissionRequest extends BaseRequest {
    public String PermissionName;
}

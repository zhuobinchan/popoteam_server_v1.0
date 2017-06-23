package com.geetion.generic.permission.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Created by mac on 15/4/24.
 */
public class PermissionOrFilter extends AuthorizationFilter {
    public PermissionOrFilter() {
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = this.getSubject(request, response);
        String[] perms = (String[]) ((String[]) mappedValue);
        boolean isPermitted = false;
        if (perms != null) {
            for (int i = 0; i < perms.length; i++) {
                String permission = perms[i];
                System.out.println(permission);
                if (subject.isPermitted(permission)) {
                    isPermitted = true;
                    break;
                }
            }
        }
        System.out.println(isPermitted);
        return isPermitted;
    }
}

package com.geetion.generic.permission.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.geetion.generic.permission.utils.ReturnKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by virgilyan on 1/21/15.
 */
public class ShiroLogoutFilter  extends AdviceFilter {
    private static final Logger log = LoggerFactory.getLogger(ShiroLogoutFilter.class);
    public static final String DEFAULT_REDIRECT_URL = "/";
    private String redirectUrl = "/";
    private Map<String, String> redirectUrlMap;

    public ShiroLogoutFilter() {
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        String redirectUrl = this.getRealRedirectUrl(request, subject);

        try {
            if(subject != null && subject.getPrincipal() != null) {
                subject.logout();
            }
        } catch (SessionException var15) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", var15);
        }

        if(ReturnKit.isJson((HttpServletRequest) request)) {
            HashMap parameterMap = new HashMap();
            Enumeration attrNames = request.getAttributeNames();
            String name = "";

            while(attrNames.hasMoreElements()) {
                name = (String)attrNames.nextElement();
                parameterMap.put(name, request.getAttribute(name));
            }

            PrintWriter writer = null;

            try {
                response.setCharacterEncoding("UTF-8");
                writer = response.getWriter();
                writer.write(JSON.toJSONString(parameterMap));
                writer.flush();
            } catch (IOException var14) {
                throw new IOException(var14);
            } finally {
                if(writer != null) {
                    writer.close();
                }

            }
        } else {
            this.issueRedirect(request, response, redirectUrl);
        }

        return false;
    }

    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
        WebUtils.issueRedirect(request, response, redirectUrl);
    }

    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        return this.getRedirectUrl();
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Map<String, String> getRedirectUrlMap() {
        return this.redirectUrlMap;
    }

    public void setRedirectUrlMap(Map<String, String> redirectUrlMap) {
        this.redirectUrlMap = redirectUrlMap;
    }

    public String getRealRedirectUrl(ServletRequest request, Subject subject) {
        String redirectUrl = "";
        if(this.redirectUrlMap != null) {
            Iterator i$ = this.redirectUrlMap.keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                if(subject.hasRole(key)) {
                    redirectUrl = (String)this.redirectUrlMap.get(key);
                    break;
                }
            }
        }

        if(redirectUrl.isEmpty()) {
            redirectUrl = this.getRedirectUrl();
        }

        return redirectUrl;
    }
}

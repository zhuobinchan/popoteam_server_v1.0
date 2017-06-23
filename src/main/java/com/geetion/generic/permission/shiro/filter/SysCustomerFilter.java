package com.geetion.generic.permission.shiro.filter;

import org.apache.shiro.web.filter.PathMatchingFilter;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysCustomerFilter extends PathMatchingFilter {
//    @Resource
//    private CustomerService customerService;
//    @Autowired
//    private HttpServletRequest mRequest;
//
//    @Override
//    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        String token = mRequest.getHeader("Authorization");
//        Custom user = null;
//        if (token != null) {
//            user = customerService.getByToken(token);
//        }
//        if (user != null) {
//            request.setAttribute(Constants.CURRENT_USER, user);
//            return true;
//        } else {
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            Map params = new HashMap();
//            params.put("code", ResultCode.CODE_402.code);
//            params.put("message", ResultCode.CODE_402.msg);
//            response.getWriter().append(JSON.toJSONString(params));
//            return false;
//        }
//    }
}

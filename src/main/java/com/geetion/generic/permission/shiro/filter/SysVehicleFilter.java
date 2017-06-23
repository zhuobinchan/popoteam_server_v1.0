package com.geetion.generic.permission.shiro.filter;

import org.apache.shiro.web.filter.PathMatchingFilter;

/**
 *
 */
public class SysVehicleFilter extends PathMatchingFilter {
//    @Resource
//    private VehicleService vehicleService;
//    @Resource
//    private ManualTaskService manualTaskService;
//    @Autowired
//    private HttpServletRequest mRequest;
//
//    @Override
//    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        String token = mRequest.getHeader("Authorization");
//        Vehicle vehicle;
//        if (token != null && !token.equals("")) {
//            vehicle = vehicleService.getByToken(token);
//            if (vehicle != null) {
//                PlanTask planTask = manualTaskService.getExecuteSolution();
//                if (planTask != null && manualTaskService.determineVehicleLock(planTask.getId().intValue(), vehicle.getId().intValue())) {
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    Map params = new HashMap();
//                    params.put("code", ResultCode.CODE_8505.code);
//                    params.put("message", ResultCode.CODE_8505.msg);
//                    response.getWriter().append(JSON.toJSONString(params));
//                    return false;
//                }
//                request.setAttribute(Constants.CURRENT_USER, vehicle);
//                return true;
//            }
//        }
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        Map params = new HashMap();
//        params.put("code", ResultCode.CODE_402.code);
//        params.put("message", ResultCode.CODE_402.msg);
//        response.getWriter().append(JSON.toJSONString(params));
//        return false;
//    }
}

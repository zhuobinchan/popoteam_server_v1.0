/**
 * Created by chenzhuobin on 2017/5/8.
 */
define(['app'], function (app) {

    app.factory('SimpleServ', SimpleServ);

});
var SimpleServ =['data_host', '$http', function (data_host, $http){
    return {
        getObjectInPages:function (daoKey,page, size, params) {
            return $http.post(data_host + '/ctrl/simple/search',{
                methodType:3,
                daoKey:daoKey,
                page:page,
                size:size,
                paramsJson:params
            });
        },
        getComplexObjectInPages:function (daoKey,methodKey,page, size, params) {
            return $http.post(data_host + '/ctrl/simple/complexSearch',{
                methodType:3,
                daoKey:daoKey,
                methodKey:methodKey,
                page:page,
                size:size,
                paramsJson:params
            });
        },
        getObjects:function (daoKey,params) {
            return $http.post(data_host + '/ctrl/simple/search',{
                methodType:1,
                daoKey:daoKey,
                paramsJson:params
            });
        },
        getComplexObjects:function (daoKey,methodKey, params) {
            return $http.post(data_host + '/ctrl/simple/complexSearch',{
                methodType:1,
                daoKey:daoKey,
                methodKey:methodKey,
                paramsJson:params
            });
        },
        setObjectStatusByIds:function (daoKey,ids,status) {
            return $http.post(data_host + '/ctrl/simple/setStatus',{
                daoKey:daoKey,
                ids:ids,
                status:status,
            });
        },
        initTable:function(){

        },

    }
}];


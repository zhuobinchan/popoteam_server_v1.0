/**
 * Created by chenzhuobin on 2017/3/28.
 */
define(['app'], function (app) {

    app.factory('LivingStatisDataServ',LivingStatisDataServ)

});

var LivingStatisDataServ = ['data_host','$http',function(data_host,$http){
    return {
        getDailyLivingStatisData:function (params,addParams) {
            return $http.get(data_host + '/ctrl/daily/living/search?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        exportDailyLivingStatisData:function (params,addParams) {
            return $http.get(data_host + '/ctrl/daily/living/newExportExcel?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        }
    }
}]
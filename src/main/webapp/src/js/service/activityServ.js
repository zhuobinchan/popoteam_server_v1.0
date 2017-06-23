/**
 * Created by gdk on 2016/10/28.
 */
define(['app'], function (app) {

    app.factory('ActivityServ',ActivityServ)

});

var ActivityServ = ['data_host','$http',function(data_host,$http){
    return {
        getByUserId: function (params) {
            return $http.get(data_host + '/ctrl/activity/search?methodType=3&'  + $.param(params));
        },
        getByActivityDetailByParams: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/search?methodType=2&'  + $.param(params) + '&' + $.param(addParams));
        },
        getActivityStatisDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/searchActivityStatisData?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        getActivityMatchStatisDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/searchActivityStatisData?methodType=3&' + $.param(params) + '&' + $.param(addParams));
        },
        getActivityAreaStatisDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/searchActivityStatisData?methodType=4&' + $.param(params) + '&' + $.param(addParams));
        },
        exportBasicExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/export?exportType=1&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        exportTypeExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/export?exportType=2&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        exportAreaExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/export?exportType=3&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        setActivityExpire: function (params) {
            return $http.post(data_host + '/ctrl/activity/updateActivity?' + $.param(params));
        },
        dissolution: function (activityIds) {
            return $http.post(
                data_host + '/ctrl/activity/dissolution',
                {
                    activityIds: activityIds
                }
            );
        },
        getActivitySumStatisData: function () {
            return $http.get(data_host + '/ctrl/activity/searchActivitySumStatisData');
        },
        getActivitySumForBar: function (params) {
            return $http.get(data_host + '/ctrl/activity/searcnActivitySumForBar?' + $.param(params));
        },
        exportActivity: function (params, addParams) {
            return $http.get(data_host + '/ctrl/activity/exportActivity?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        }
    }
}];
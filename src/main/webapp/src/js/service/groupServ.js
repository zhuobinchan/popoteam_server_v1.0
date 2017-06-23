/**
 * Created by gdk on 2016/10/28.
 */
define(['app'], function (app) {

    app.factory('GroupServ',GroupServ)

});

var GroupServ = ['data_host','$http',function(data_host,$http){
    return {
        getByGroupMemberId: function (params) {
            return $http.get(data_host + '/ctrl/group/search?methodType=3&'  + $.param(params));
        },

        getByGroupDetailByParams: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/search?methodType=2&'  + $.param(params) + '&' + $.param(addParams));
        },
        dissolution: function (groupIds) {
            return $http.post(
                data_host + '/ctrl/group/dissolution',
                {
                    groupIds: groupIds
                }
            );
        },
        getGroupStaticDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/searchGroupStatisData?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        getGroupBarStaticDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/searchGroupStatisData?methodType=3&' + $.param(params) + '&' + $.param(addParams));
        },
        getGroupRegionStaticDataPage: function () {
            return $http.get(data_host + '/ctrl/group/searchGroupStatisData?methodType=4');
        },
        exportBasicExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/export?exportType=1&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        exportTypeExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/export?exportType=2&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        exportRegionExcel: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/export?exportType=3&' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        getGroupSumStatisData: function () {
            return $http.get(data_host + '/ctrl/group/searchGroupSumStatisData');
        },
        exportGroup: function (params, addParams) {
            return $http.get(data_host + '/ctrl/group/exportGroup?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        getGroupMemberByParams: function (params) {
            return $http.get(data_host + '/ctrl/group/searchGroupMemberByGroupId?' + $.param(params));
        },
        getGroupSumForCharts: function (type) {
            return $http.get(data_host + '/ctrl/group/searchGroupSumForCharts?type=' + type);
        },
        getGroupSumForBar: function (params) {
            return $http.get(data_host + '/ctrl/group/searcnGroupSumForBar?' + $.param(params));
        },
        getGroupMemberByIds:function (ids) {
            return $http.post(data_host + '/ctrl/group/searchGroupMemberHistoryByGroupIds' ,{ids:ids});
        }



    }
}];
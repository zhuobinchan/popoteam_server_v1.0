/**
 * Created by chenzhuobin on 2017/3/27.
 */
define(['app'], function (app) {

    app.factory('BarServ', BarServ);

});

var BarServ =['data_host', '$http', function (data_host, $http){
    return {
        getBarInPages:function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/bar/search?methodType=3&size=' + size + '&page=' + page
                + p);
        },
        getAllOtherBars:function (ids,status) {
            return $http.post(data_host + '/ctrl/bar/searchOtherBars',{
                ids:ids,
                status:status
            });
        },

        setBarStatusByIds:function (ids,status) {
            return $http.post(
                data_host + '/ctrl/bar/setStatus',
                {
                    ids: ids,
                    status:status
                }
            );
        },
        setBarStatusScreen:function (ids,status, groupHandle,activityHandle) {
            return $http.post(
                data_host + '/ctrl/bar/setStatusScreen',
                {
                    ids: ids,
                    status:status,
                    groupHandle:groupHandle,
                    activityHandle:activityHandle
                }
            );
        },
        setBarStatusScreenAndGroupChgOtherActivity:function (ids,otherBarId,activityDissolve) {
            return $http.post(
                data_host + '/ctrl/bar/setStatusScreen/GroupChgOtherBar',
                {
                    barIds: ids,
                    otherBarId:otherBarId,
                    activityDissolve:activityDissolve
                }
            );
        },
        setBarStatusScreenAndGroupWithActivityDissolve:function (ids,activityDissolve) {
            return $http.post(
                data_host + '/ctrl/bar/setBarStatusScreen/GroupWithActivityDissolve',
                {
                    barIds: ids,
                    activityDissolve:activityDissolve,
                }
            );
        }
    }
}];
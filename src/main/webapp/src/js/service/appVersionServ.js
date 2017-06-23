/**
 * Created by chenzhuobin on 2017/3/29.
 */
define(['app'], function (app) {

    app.factory('AppVersionServ', AppVersionServ);

});

var AppVersionServ = ['data_host', '$http', function (data_host, $http){
    return {
        getAppVersionInPages : function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/app/version/search?methodType=3&size=' + size + '&page=' + page
                + p);
        },
        deleteAppVersionByIds:function (ids) {
            return $http.post(
                data_host + '/ctrl/app/version/delete',
                {
                    ids: ids
                }
            );
        }
    }
}]
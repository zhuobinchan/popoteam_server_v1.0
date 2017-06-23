/**
 * Created by sherry on 2016/6/12.
 */
define(['app'], function (app) {

    app.factory('PermissionServ',PermissionServ)

});

var PermissionServ = ['data_host','$http',function(data_host,$http){
    return {
        getPermissionList: function () {
            return $http.get(data_host + '/ctrl/permission/search?methodType=1');
        }
    }
}];
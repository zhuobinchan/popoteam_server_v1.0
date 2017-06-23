/**
 * Created by sherry on 2016/6/12.
 */
define(['app'], function (app) {

    app.factory('AdminServ',AdminServ)

});

var AdminServ = ['data_host','$http',function(data_host,$http){
    return {
        getAdminLoginHis: function (params, addParams) {
            return $http.get(data_host + '/ctrl/admin/searchLoginHis?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        getAdminList: function (params, addParams) {
            return $http.get(data_host + '/ctrl/admin/search?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        getRoleList: function (params, addParams) {
            return $http.get(data_host + '/ctrl/admin/search?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },
        registerAdmin: function(params){
            return $http.post(
                data_host + '/ctrl/admin/register',
                {
                    account: params.account,
                    password: params.password,
                    roles: params.roles
                }
            );
        },
        deleteAdminByUserId: function (userId) {
            return $http.post(
                data_host + '/ctrl/admin/delete',
                {
                    userId: userId
                }
            );
        },
        updateAdminRole: function (userId, roles) {
            return $http.post(
                data_host + '/ctrl/admin/updateRole',
                {
                    userId: userId,
                    roles: roles
                }
            );
        },
        updateAdminPsw: function (oldPsw, newPsw) {
            return $http.post(
                data_host + '/ctrl/admin/updatePsw',
                {
                    oldPsw: oldPsw,
                    newPsw: newPsw
                }
            );
        }

    }
}];
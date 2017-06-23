/**
 * Created by sherry on 2016/6/12.
 */
define(['app'], function (app) {

    app.factory('RoleServ',RoleServ)

});

var RoleServ = ['data_host','$http',function(data_host,$http){
    return {
        getRoleList: function () {
            return $http.get(data_host + '/ctrl/role/search?methodType=1');
        },
        getRolePermissionList: function(params){
            return $http.get(data_host + '/ctrl/role/searchRolePermissionList?methodType=2&' + $.param(params));
        },
        addRolePermission: function(params){
            return $http.post(
                data_host + '/ctrl/role/addRolePermission',
                {
                    roleName: params.roleName,
                    extra: params.extra,
                    permissionList: params.permissionList
                }
            );
        },
        deleteRoleById: function (roleId) {
            return $http.post(
                data_host + '/ctrl/role/delete',
                {
                    roleId: roleId
                }
            );
        },
        updateRolePermission : function(params){
            return $http.post(
                data_host + '/ctrl/role/updateRolePermission',
                {
                    roleId: params.roleId,
                    roleName: params.roleName,
                    extra: params.extra,
                    permissionIds: params.permissionList
                }
            );
        }
    }
}];
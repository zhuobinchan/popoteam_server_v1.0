/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/adminServ', 'service/commonServ'], function (app) {
    app.controller('PswConfCtrl', PswConfCtrl);
});

PswConfCtrl.$inject = ['$scope', 'AdminServ', 'CommonServ'];

function PswConfCtrl($scope, AdminServ, CommonServ) {
    var moment = require('moment');
    $scope.admin = {
        oldPsw: '',
        newPsw:''
    };

    $scope.updatePsw = function(){
        AdminServ.updateAdminPsw($scope.admin.oldPsw, $scope.admin.newPsw).success(function (data) {
            swal(data.message);
        }).error(function (r) {
            console.log(r);
        });
    }
}
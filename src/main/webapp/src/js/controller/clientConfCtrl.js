/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/confServ'], function (app) {

    app.controller('ClientConfCtrl', ClientConfCtrl);

});

ClientConfCtrl.$inject = ['$scope', 'ConfServ'];

function ClientConfCtrl($scope, ConfServ) {

    $scope.updateObj = {
        carouselInterval: '',
        advertisementPages: ''
    };
    $scope.oldSetting = {};

    $scope.getSetting = function () {
        ConfServ.getAppSetting().success(function (data) {
            if (data.code == 200) {
                $scope.oldSetting = data.object;
            }
        }).error(function (err) {
            console.log(err);
        });
    };

    $scope.getSetting();

    $scope.changeSetting = function () {
        console.log($scope.updateObj);
        ConfServ.updateAppSetting($scope.updateObj).success(function (data) {
            if (data.code == 200) {
                swal('修改成功');
            }
        }).error(function (err) {
            swal(GCONSTANTS.MSG.ERR500);
            console.log(err);
        })['finally'](function () {
            $scope.updateObj = {};
            $scope.getSetting();
        });
    }

}
/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/confServ'], function (app) {

    app.controller('DatingConfCtrl', DatingConfCtrl);

});

DatingConfCtrl.$inject = ['$scope', 'ConfServ'];

function DatingConfCtrl($scope, ConfServ) {
    $scope.dateSetting = {};
    $scope.updateDateObj = {};

    $scope.matchSetting = {};
    $scope.updateMatchObj = {};

    $scope.getMatchSetting = function () {
        ConfServ.getMatchSetting().success(function (data) {
            if (data.code == 200) {
                $scope.matchSetting = data.object;
            }
        }).error(function (err) {
            console.log(err);
        });
    };

    $scope.getMatchSetting();

    $scope.changeMatchSetting = function () {
        ConfServ.updateMatchSetting($scope.updateMatchObj).success(function (data) {
            if (data.code == 200) {
                swal('修改成功');
            }
        }).error(function (err) {
            console.log(err);
        })['finally'](function () {
            $scope.updateMatchObj = {};
            $scope.getMatchSetting();
        });
    };

    $scope.getDateSetting = function () {
        ConfServ.getDateSetting().success(function (data) {
            if (data.code == 200) {
                $scope.dateSetting = data.object;
            }
        }).error(function (err) {
            console.log(err);
        });
    };

    $scope.getDateSetting();

    $scope.changeDateSetting = function () {
        ConfServ.updateDateSetting($scope.updateDateObj).success(function (data) {
            if (data.code == 200) {
                swal('修改成功');
            }
        }).error(function (err) {
            console.log(err);
        })['finally'](function () {
            $scope.updateDateObj = {};
            $scope.getDateSetting();
        });
    }
}
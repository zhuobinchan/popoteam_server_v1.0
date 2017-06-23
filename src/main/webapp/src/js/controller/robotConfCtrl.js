/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'directive/locationSelector.directive','service/commonServ'], function (app) {

    app.controller('RobotConfCtrl', RobotConfCtrl);

});

RobotConfCtrl.$inject = ['$scope', 'CommonServ', 'data_host', 'Upload'];

function RobotConfCtrl($scope, CommonServ, data_host, Upload) {

    $scope.uploading = false;

    $scope.data = {
        file: null,
        friendFile: null,
        groupFile: null
    };
    $scope.location = '';
    $scope.userType = 1;
    $scope.actionType = 1;
    $scope.sex = '0';
    $scope.barList = [];

    CommonServ.getBarList().success(function (data) {
        if(200 == data.code) {
            $scope.barList = data.list;
        }
    });


    // 上传单个文件
    $scope.upload = function (fileObj) {

        if (!$scope.data.file) {
            return;
        }

        $scope.uploading = true;

        Upload.upload({
            url: data_host + '/ctrl/common/uploadRobotConf',
            method: 'POST',
            transformRequest: angular.identity,
            data: {uploadfiles: $scope.data.file, sizes: 0}
        }).then(function (resp) {
            if (resp.data.code == 200) {
                swal("数据导入成功！");
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    };

    $scope.uploadFriend = function(){
        if (!$scope.data.friendFile) {
            swal("请选择要上传的文件！");
        }

        $scope.uploading = true;

        Upload.upload({
            url: data_host + '/ctrl/common/uploadFriendFile',
            method: 'POST',
            transformRequest: angular.identity,
            data: {friendFile: $scope.data.friendFile, sizes: 0, userType: $scope.userType, actionType:$scope.actionType}
        }).then(function (resp) {
            if (resp.data.code == 200) {
                $scope.data.friendFile = null;
                swal("数据导入成功！");
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    }

    $scope.uploadGroup = function(){
        if (!$scope.data.groupFile) {
            swal("请选择要上传的文件！");
            return;
        }

        if($scope.location.province == undefined || $scope.location.city == undefined){
            swal("请选择对应的地区类型");
            return;
        }

        if($scope.bar == undefined){
            swal("请选择对应的活动类型");
            return;
        }

        $scope.uploading = true;

        Upload.upload({
            url: data_host + '/ctrl/common/uploadGroupFile',
            method: 'POST',
            transformRequest: angular.identity,
            data: {
                groupFile: $scope.data.groupFile,
                sizes: 0,
                userType: $scope.userType,
                provinceId : $scope.location.province.code,
                province: $scope.location.province.name,
                cityId: $scope.location.city.code,
                city: $scope.location.city.name,
                areaId: $scope.location.area.code ? $scope.location.area.code : '0',
                area: $scope.location.area.name ? $scope.location.area.name : $scope.location.city.name,
                barId: $scope.bar.id,
                sex: $scope.sex
            }
        }).then(function (resp) {
            if (resp.data.code == 200) {
                $scope.data.groupFile = null;
                swal("数据导入成功！");
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    }

}
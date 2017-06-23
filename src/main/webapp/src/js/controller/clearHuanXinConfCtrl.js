/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/commonServ'], function (app) {
    app.controller('ClearHuanXinConfCtrl', ClearHuanXinConfCtrl);
});

ClearHuanXinConfCtrl.$inject = ['$scope', 'CommonServ', 'data_host', 'Upload'];

function ClearHuanXinConfCtrl($scope, CommonServ, data_host, Upload) {
    var moment = require('moment');
    $scope.data={
        groupFile:'',
        userFile:'',
        cursor:''
    };

    $scope.clear = function(){

        if (!$scope.data.groupFile) {
            swal("请选择要上传的文件");
            return;
        }

        $scope.uploading = true;

        Upload.upload({
            url: data_host + '/ctrl/common/clearGroupsHuanXin',
            method: 'POST',
            transformRequest: angular.identity,
            data: {uploadfiles: $scope.data.groupFile, sizes: 0}
        }).then(function (resp) {
            if (resp.data.code == 200) {
                swal("数据导入成功！");
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    }

    $scope.clearUsers = function(){

        if (!$scope.data.userFile) {
            swal("请选择要上传的文件");
            return;
        }

        $scope.uploading = true;

        Upload.upload({
            url: data_host + '/ctrl/common/clearUsersHuanXin',
            method: 'POST',
            transformRequest: angular.identity,
            data: {
                uploadfiles: $scope.data.userFile,
                cursor: $scope.data.cursor,
                sizes: 0}
        }).then(function (resp) {

            $scope.data.cursor = resp.data.cursor;
            if (resp.data.code == 200) {

                swal("数据导入成功！");
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    }
}
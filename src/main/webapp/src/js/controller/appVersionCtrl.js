/**
 * Created by chenzhuobin on 2017/3/29.
 */
define(['app', 'service/appVersionServ', 'service/commonServ'], function (app) {

    app.controller('AppVersionCtrl', AppVersionCtrl);

});
AppVersionCtrl.$inject = ['$scope', 'AppVersionServ', 'Upload', 'data_host', 'CommonServ'];

function AppVersionCtrl($scope, AppVersionServ, Upload, data_host, CommonServ) {
    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'id',
        val: '',
    };
    $scope.appVersion = {
        version: '',
        type: 1
    };
    $scope.trueOrFalseOpts = [
        {txt: '是', val: 1},
        {txt: '否', val: 0}
    ];

    $scope.appVersionGridOpts = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        enableRowSelection: true,
        enableSelectAll: true,
        selectionRowHeaderWidth: 35,
        useExternalPagination: true,
        useExternalFiltering: false,
        multiSelect: true,
        enableFiltering: false,
        rowHeight: 40,
        columnDefs: [
            {
                name: 'appID',
                field: 'id',
                minWidth: 60
            },
            {
                name: 'app版本',
                field: 'version',
                minWidth: 80
            },
            {
                name: 'app标识',
                field: 'identify',
                minWidth: 80
            },
            {
                name: 'app描述',
                field: 'content',
                minWidth: 80
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-danger btn-console" ' +
                'ng-click="grid.appScope.downloadApkFile(row.entity)">下载</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {
             });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getAppVersions(newPage, pageSize);
            });
        }
    };
    getAppVersions(1, $scope.appVersionGridOpts.paginationPageSize);

    function getAppVersions(page, size) {
        $scope.isLoading = true;
        AppVersionServ.getAppVersionInPages(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.appVersionGridOpts.totalItems = data.totalSize;
                $scope.appVersionGridOpts.data = data.list;
                $scope.isSuccessed = true;
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200)
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function () {
            $scope.isLoading = false;
        });
    }

    $scope.searchAppVersionsData = function () {
        if (!$scope.searchParams.val) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {};
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getAppVersions(1, $scope.appVersionGridOpts.paginationPageSize);

    }

    $scope.resetAppVersionsData = function () {
        $scope.params = '';
        $scope.searchParams = {
            key: 'id',
            val: '',
        };
        getAppVersions(1, $scope.appVersionGridOpts.paginationPageSize);
    }

    $scope.deleteAppVersion = function () {
        $scope.appVersionIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.appVersionIds.length) {
            AppVersionServ.deleteAppVersionByIds($scope.appVersionIds).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.appVersionIds = [];
                    getAppVersions(1, $scope.appVersionGridOpts.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    }

    $scope.saveAppVersion = function () {
        var submitObj = angular.copy($scope.appVersion);
        Upload.upload({
            url: data_host + '/ctrl/app/version/add?' + $.param({
                version: submitObj.version,
                identify: submitObj.identify,
                content: submitObj.content,
                type: submitObj.type==1?true:false,
                url: submitObj.url,
            }),
            method: 'POST',
            dataType: "json",
            //transformRequest: angular.identity,
            file: $scope.apkFile,
        }).then(function (resp) {
            if (resp.data.code == 200) {
                swal({
                    title: '新建成功',
                    type: 'success',
                    comfirmButtonText: '确定'
                }, function () {
                    $('#bar-detail').modal('hide');
                    $scope.$apply(function () {
                        $scope.appVersion = {
                            version: '',
                            type: 1
                        };
                    });
                });
                getAppVersions(1, $scope.appVersionGridOpts.paginationPageSize);
            }
        }, function (resp) {
            swal('发布失败，请稍后再试');
        })['finally'](function () {
            $scope.uploading = false;
            $scope.imageFile = null;
        });
    }

    $scope.downloadApkFile = function (appVersion) {
        $scope.appVersion = angular.copy(appVersion);
        CommonServ.downloadFileByUrl($scope.appVersion.url);
    }

}
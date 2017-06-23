/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/adminServ', 'service/commonServ'], function (app) {
    app.controller('AdminLoginHisCtrl', AdminLoginHisCtrl);
});

AdminLoginHisCtrl.$inject = ['$scope', 'AdminServ', 'CommonServ'];

function AdminLoginHisCtrl($scope, AdminServ, CommonServ) {
    var moment = require('moment');
    $scope.gridOptions = {
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
                name: '用户Id',
                field: 'userId',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '昵称',
                field: 'nickName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '登录时间',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 40
            }

        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setGridByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    $scope.params = {};
    $scope.searchType = 'userId';
    $scope.searchContent = '';
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;
        if ($scope.params.loginTimeBegin) {
            $scope.params.loginTimeBegin = moment($scope.params.loginTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.params.loginTimeEnd) {
            $scope.params.loginTimeEnd = moment($scope.params.loginTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        }
        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }

        AdminServ.getAdminLoginHis($scope.params, addParams).success(function (data) {
            if (data.code == 200) {
                $scope.gridOptions.totalItems = data.totalSize;
                $scope.gridOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    setGridByparam(1, $scope.gridOptions.paginationPageSize);

    $scope.searchParam = function () {
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    $scope.reset = function () {
        $scope.params = {};
        $scope.searchType = 'userId';
        $scope.searchContent = '';
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

}
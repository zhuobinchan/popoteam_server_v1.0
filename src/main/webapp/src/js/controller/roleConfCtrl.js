/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/adminServ', 'service/commonServ'], function (app) {
    app.controller('RoleConfCtrl', RoleConfCtrl);
});

RoleConfCtrl.$inject = ['$scope', 'AdminServ', 'CommonServ'];

function RoleConfCtrl($scope, AdminServ, CommonServ) {
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
                name: 'ID',
                field: 'userId',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '名称',
                field: 'nickName',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '角色',
                field: 'roleName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '创建时间',
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
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }

        AdminServ.getAdminList($scope.params, addParams).success(function (data) {
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

}
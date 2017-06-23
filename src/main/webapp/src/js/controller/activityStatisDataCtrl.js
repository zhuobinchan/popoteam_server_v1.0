/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/activityServ', 'service/commonServ'], function (app) {
    app.controller('ActivityStatisDataCtrl', ActivityStatisDataCtrl);
});

ActivityStatisDataCtrl.$inject = ['$scope', 'ActivityServ', 'CommonServ'];

function ActivityStatisDataCtrl($scope, ActivityServ, CommonServ) {
    var moment = require('moment');
    $scope.gridBasicOptions = {
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
                name: '日期',
                field: 'date',
                minWidth: 80
            },
            {
                name: '约会总量',
                field: 'activityTotal',
                minWidth: 60
            },
            {
                name: 'superLike数',
                field: 'activitySuperLike',
                minWidth: 60
            },
            {
                name: '约会平均人数',
                field: 'activityMemberAvg',
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
    $scope.type = '1';
    $scope.isLoading = false;

    function setBasicGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        addParams['type'] =  $scope.type;
        ActivityServ.getActivityStatisDataPage($scope.params, addParams).success(function (data) {
            if (data.code == 200) {
                $scope.gridBasicOptions.totalItems = data.totalSize;
                $scope.gridBasicOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    setBasicGridByparam(1, $scope.gridBasicOptions.paginationPageSize);

    $scope.gridTypeOptions = {
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
                name: '日期',
                field: 'date',
                minWidth: 80
            },
            {
                name: '约会总量',
                field: 'activityTotal',
                minWidth: 60
            },
            {
                name: '同地区同类型',
                field: 'activitySasb',
                minWidth: 60
            },
            {
                name: '同地区不同类型',
                field: 'activitySadb',
                minWidth: 60
            },
            {
                name: '同市同类型',
                field: 'activityScsb',
                minWidth: 60
            },
            {
                name: '同市不同类型',
                field: 'activityScdb',
                minWidth: 60
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setTypeGridByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    function setTypeGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        addParams['type'] =  $scope.type;
        ActivityServ.getActivityMatchStatisDataPage($scope.params, addParams).success(function (data) {
            if (data.code == 200) {
                $scope.gridTypeOptions.totalItems = data.totalSize;
                $scope.gridTypeOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }


    $scope.gridAreaOptions = {
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
                name: '日期',
                field: 'date',
                minWidth: 80
            },
            {
                name: '约会总量',
                field: 'activityTotal',
                minWidth: 60
            },
            {
                name: '市',
                field: 'activityCity',
                minWidth: 60
            },
            {
                name: '同地区同类型',
                field: 'activitySasb',
                minWidth: 60
            },
            {
                name: '同地区不同类型',
                field: 'activitySadb',
                minWidth: 60
            },
            {
                name: '同市同类型',
                field: 'activityScsb',
                minWidth: 60
            },
            {
                name: '同市不同类型',
                field: 'activityScdb',
                minWidth: 60
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setAreaGridByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    function setAreaGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        addParams['type'] =  $scope.type;
        ActivityServ.getActivityAreaStatisDataPage($scope.params, addParams).success(function (data) {
            if (data.code == 200) {
                $scope.gridAreaOptions.totalItems = data.totalSize;
                $scope.gridAreaOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    /**
     * 查询按钮事件
     */
    $scope.searchParam = function (type) {
        if (type == 1){
            setBasicGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
        } else if(type == 2){
            setTypeGridByparam(1, $scope.gridTypeOptions.paginationPageSize);
        } else{
            setAreaGridByparam(1, $scope.gridAreaOptions.paginationPageSize)
        }

    };

    /**
     * tab页事件
     * @param type
     */
    $scope.search = function(type){
        $scope.type = '1';
        if (type == 1){
            setBasicGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
        } else if(type == 2){
            setTypeGridByparam(1, $scope.gridTypeOptions.paginationPageSize);
        } else{
            setAreaGridByparam(1, $scope.gridAreaOptions.paginationPageSize)
        }
    }

    $scope.export = function(exportType){
        $scope.isLoading = true;

        var addParams = {};
        addParams['type'] =  $scope.type;
        if(exportType == 1){
            ActivityServ.exportBasicExcel($scope.params, addParams).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "约会统计_基本统计.xls";
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();
                $scope.isSuccessed = true;
                swal("导出成功");
            });
        }else if(exportType == 2){
            ActivityServ.exportTypeExcel($scope.params, addParams).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "约会统计_按约会类型统计.xls";
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();

                $scope.isSuccessed = true;
                swal("导出成功");
            });
        }else{
            ActivityServ.exportAreaExcel($scope.params, addParams).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "约会统计_按约会地点统计.xls";
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();

                $scope.isSuccessed = true;
                swal("导出成功");
            });
        }

        $scope.isLoading = false;
    }
}
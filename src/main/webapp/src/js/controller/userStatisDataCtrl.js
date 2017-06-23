/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/userServ', 'service/commonServ'], function (app) {
    app.controller('UserStatisDataCtrl', UserStatisDataCtrl);
});

UserStatisDataCtrl.$inject = ['$scope', 'UserServ', 'CommonServ','i18nService','$http'];

function UserStatisDataCtrl($scope, UserServ, CommonServ,i18nService,$http) {
    var moment = require('moment');


    $scope.gridBasicOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        enableRowSelection: true,
        enableSelectAll: true,
        selectionRowHeaderWidth: 35,
        useExternalPagination: false,
        useExternalSorting: false,
        useExternalFiltering: false,
        multiSelect: true,
        enableFiltering: false,
        rowHeight: 40,
        enableGridMenu: true,

        columnDefs: [
            {
                name: '日期',
                field: 'date',
                minWidth: 80,
                groupable :true
            },
            {
                name: '用户量',
                field: 'userTotal',
                minWidth: 60
            },
            {
                name: '微信绑定量',
                field: 'wechatTotal',
                minWidth: 40
            },
            {
                name: '男性用户量',
                field: 'maleTotal',
                minWidth: 40
            },
            {
                name: '男性占比',
                field: 'maleRatio',
                minWidth: 40
            },
            {
                name: '女性用户量',
                field: 'femaleTotal',
                minWidth: 40
            },
            {
                name: '女性占比',
                field: 'femaleRatio',
                minWidth: 40
            },
            {
                name: '18岁以下',
                field: 'ageOneTotal',
                minWidth: 40
            },
            {
                name: '18-29岁',
                field: 'ageTwoTotal',
                minWidth: 40
            },
            {
                name: '30-45岁',
                field: 'ageThreeTotal',
                minWidth: 40
            },
            {
                name: '45岁以上',
                field: 'ageFourTotal',
                minWidth: 80
            }

        ],
        //--------------导出----------------------------------
        exporterAllDataFn: function(){
            return setGridByparam(1,$scope.gridBasicOptions.totalItems).then(function() {
                $scope.gridBasicOptions.useExternalPagination = false;
                $scope.gridBasicOptions.useExternalSorting = false;
                setGridByparam = null;
            });
        },
        exporterCsvColumnSeparator: ',',
        exporterCsvFilename:'download.csv',
        exporterHeaderFilterUseName : true,
        exporterMenuCsv : true,
        exporterMenuPdf : false,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });

            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                if(setGridByparam) {
                    setGridByparam(newPage, pageSize);
                }
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    $scope.params = {};
    $scope.type = '1';
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        addParams['type'] =  $scope.type;
        UserServ.getUserStaticDataPage($scope.params, addParams).success(function (data) {
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

    setGridByparam(1, $scope.gridBasicOptions.paginationPageSize);

    $scope.searchParam = function () {
        setGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
    };

    $scope.reset = function () {
        $scope.params = {};
        $scope.searchType = '1';
        $scope.searchContent = '';
        $scope.groupName = '';
        setGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
    };

    /* 导出 */
    $scope.exportExcel = function(){
        $scope.isLoading = true;

        var addParams = {};
        addParams['type'] =  $scope.type;

        UserServ.exportUserStaticData($scope.params, addParams).success(function (data, status, headers, config) {

            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var  fileName = "用户量统计_基本统计.xls";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();

            $scope.isSuccessed = true;
            swal("导出成功");
        }).error(function (r) {
            console.log(r);
            swal("导出失败");
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }


    $scope.gridJobOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        enableRowSelection: true,
        enableSelectAll: true,
        selectionRowHeaderWidth: 35,
        multiSelect: true,
        enableFiltering: false,
        rowHeight: 40,

        columnDefs: [
            {
                name: '职业',
                field: 'name',
                minWidth: 100
            },
            {
                name: '总量',
                field: 'total',
                minWidth: 100
            },
            {
                name: '占用户总量比例',
                field: 'ratio',
                minWidth: 100
            },
            {
                name: '男性总量',
                field: 'maleTotal',
                minWidth: 100
            },
            {
                name: '女性总量',
                field: 'femaleTotal',
                minWidth: 100
            }

        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                setGridJobByparam(grid.gridJobOptions.paginationCurrentPage, grid.gridJobOptions.paginationPageSize)
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setGridJobByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    function setGridJobByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        UserServ.getUserJobStaticDataPage($scope.params).success(function (data) {
            if (data.code == 200) {
                $scope.gridJobOptions.totalItems = data.totalSize;
                $scope.gridJobOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    //setGridJobByparam(1, $scope.gridJobOptions.paginationPageSize);

    $scope.gridInterestOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        enableRowSelection: true,
        enableSelectAll: true,
        selectionRowHeaderWidth: 35,
        multiSelect: true,
        enableFiltering: false,
        rowHeight: 40,

        columnDefs: [
            {
                name: '兴趣',
                field: 'name',
                minWidth: 100
            },
            {
                name: '总量',
                field: 'total',
                minWidth: 100
            },
            {
                name: '占用户总量比例',
                field: 'ratio',
                minWidth: 100
            },
            {
                name: '男性总量',
                field: 'maleTotal',
                minWidth: 100
            },
            {
                name: '女性总量',
                field: 'femaleTotal',
                minWidth: 100
            }

        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                setGridInterestByparam(grid.gridInterestOptions.paginationCurrentPage, grid.gridInterestOptions.paginationPageSize)
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setGridInterestByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    function setGridInterestByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        UserServ.getUserInterestStatisData($scope.params).success(function (data) {
            if (data.code == 200) {
                $scope.gridInterestOptions.totalItems = data.totalSize;
                $scope.gridInterestOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    //setGridInterestByparam(1, $scope.gridInterestOptions.paginationPageSize);

    /* 导出 */
    $scope.exportMonthData = function(exportType){
        $scope.isLoading = true;

        var  fileName ;
        if(exportType == 1){
            fileName = "用户量统计_兴趣统计.xls"
        }else {
            fileName = "用户量统计_职业统计.xls"
        }

        var addParams = {};
        addParams['exportType'] =  exportType;

        UserServ.exportMonthData($scope.params, addParams).success(function (data, status, headers, config) {

            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});

            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();

            $scope.isSuccessed = true;
            swal("导出成功");
        }).error(function (r) {
            console.log(r);
            swal("导出失败");
        })['finally'](function () {
            $scope.isLoading = false;
        });


    }

    /**
     * tab页事件
     * @param type
     */
    $scope.search = function(type){
        $scope.type = '1';
        if (type == 1){
            setGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
        } else if (type == 2){
            setGridInterestByparam(1, $scope.gridInterestOptions.paginationPageSize);
        } else {
            setGridJobByparam(1, $scope.gridJobOptions.paginationPageSize);
        }
    }
}
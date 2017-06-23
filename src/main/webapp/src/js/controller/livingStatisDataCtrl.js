/**
 * Created by chenzhuobin on 2017/3/28.
 */
define(['app', 'service/livingStatisDataServ'], function (app) {
    app.controller('LivingStatisDataCtrl', LivingStatisDataCtrl);
});

LivingStatisDataCtrl.$inject = ['$scope', 'LivingStatisDataServ'];

function LivingStatisDataCtrl($scope, LivingStatisDataServ) {
    var moment = require('moment');
    $scope.getDailyLivingDetails = getDailyLivingDetails;

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
                groupable: true
            },
            {
                name: '用户活跃量',
                field: 'userDailyLivingTotal',
                minWidth: 60
            },
            {
                name: '队伍活跃量',
                field: 'groupDailyLivingTotal',
                minWidth: 40
            }
            ,
            {
                name: '查看详情',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getDailyLivingDetails(row.entity)">查看详情</button>',
                minWidth: 90
            }

        ],
        //--------------导出----------------------------------
        exporterAllDataFn: function () {
            return setGridByparam(1, $scope.gridBasicOptions.totalItems).then(function () {
                $scope.gridBasicOptions.useExternalPagination = false;
                $scope.gridBasicOptions.useExternalSorting = false;
                setGridByparam = null;
            });
        },
        exporterCsvColumnSeparator: ',',
        exporterCsvFilename: 'download.csv',
        exporterHeaderFilterUseName: true,
        exporterMenuCsv: true,
        exporterMenuPdf: false,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });

            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                if (setGridByparam) {
                    setGridByparam(newPage, pageSize);
                }
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    $scope.params = {};
    $scope.type = '0';
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        addParams['type'] = $scope.type;

        addParams['dailyLivingTimeBegin'] = moment($scope.dailyLivingTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        addParams['dailyLivingTimeEnd'] = moment($scope.dailyLivingTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        LivingStatisDataServ.getDailyLivingStatisData($scope.params, addParams).success(function (data) {
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
        setGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
    };

    function getDailyLivingDetails(dailyLivingStatisData) {
        $scope.dailyLivingStatisData = angular.copy(dailyLivingStatisData);

        $('#dailyLiving-detail').modal('show');
    }




    /* 导出 */
    $scope.exportExcel = function () {

        var addParams = {};
        addParams['type'] = $scope.type;
        addParams['dailyLivingTimeBegin'] = moment($scope.dailyLivingTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        addParams['dailyLivingTimeEnd'] = moment($scope.dailyLivingTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        $scope.isLoading = true;
        LivingStatisDataServ.exportDailyLivingStatisData($scope.params, addParams).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var now = new Date();
            var year = now.getFullYear()+'年';
            var month = now.getMonth()+1+'月';
            var date = now.getDate()+'日';
            var fileName = [year,month,date].join('-')+"-活跃量统计_基本统计.xls";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
            $scope.isSuccessed = true;
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });

    }


    $scope.isHideDailyLivingTime = true;
    $scope.isShowTime = function () {
        if($scope.type==2){
            $scope.isHideDailyLivingTime = false;
        }else {
            $scope.isHideDailyLivingTime = true;
        }

    }
}
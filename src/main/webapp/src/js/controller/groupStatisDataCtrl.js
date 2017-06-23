/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/groupServ', 'service/commonServ'], function (app) {
    app.controller('GroupStatisDataCtrl', GroupStatisDataCtrl);
});

GroupStatisDataCtrl.$inject = ['$scope', 'GroupServ', 'CommonServ','$http'];

function GroupStatisDataCtrl($scope, GroupServ, CommonServ, $http) {
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
                name: '队伍总量',
                field: 'groupTotal',
                minWidth: 60
            },
            {
                name: '活跃队伍数',
                field: 'groupValidTotal',
                minWidth: 60
            },
            {
                name: '男性队伍量',
                field: 'maleTotal',
                minWidth: 40
            },
            {
                name: '男性占比',
                field: 'maleRatio',
                minWidth: 40
            },
            {
                name: '女性队伍量',
                field: 'femaleTotal',
                minWidth: 40
            },
            {
                name: '女性占比',
                field: 'femaleRatio',
                minWidth: 40
            },
            {
                name: '组成约会数',
                field: 'activityGroupTotal',
                minWidth: 40
            },
            {
                name: '平均队伍人数',
                field: 'groupMemberAvg',
                minWidth: 40
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setBasicGridByparam(newPage, pageSize);
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
        GroupServ.getGroupStaticDataPage($scope.params, addParams).success(function (data) {
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
                minWidth: 100
            },
            {
                name: '队伍总量',
                field: 'groupTotal',
                minWidth: 100
            },
            {
                name: '市',
                field: 'groupCity',
                minWidth: 120
            },
            {
                name: '区',
                field: 'groupArea',
                minWidth: 120
            },
            {
                name: '一起出去唱k',
                field: 'groupBarOne',
                minWidth: 100
            },
            {
                name: '今晚去蹦迪',
                field: 'groupBarTwo',
                minWidth: 100
            },
            {
                name: '出去喝一杯',
                field: 'groupBarThree',
                minWidth: 100
            },
            {
                name: '我们出去吧',
                field: 'groupBarFour',
                minWidth: 100
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
        GroupServ.getGroupBarStaticDataPage($scope.params, addParams).success(function (data) {
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
    /**
     * 查询按钮事件
     */
    $scope.searchParam = function (type) {
        if (type == 1){
            setBasicGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
        } else{
            setTypeGridByparam(1, $scope.gridTypeOptions.paginationPageSize);
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
            setTypeGridByparam(1, $scope.gridBasicOptions.paginationPageSize);
        } else{
            setRegionGridByparam();
        }
    }



    $scope.gridRegionOptions = {

        enableSorting: true,
        enableFiltering: false,
        showTreeExpandNoChildren: true,
        columnDefs: [
            {
                name: '市',
                field: 'groupCity',
                minWidth: 150
            },
            {
                name: '区',
                field: 'groupArea',
                minWidth: 150
            },
            {
                name: '队伍总量',
                field: 'groupTotal',
                minWidth: 150
            },{
                name: '操作',
                cellTemplate: '<div ng-if="row.entity.regionLevel == 0"><button type="button" class="btn btn-default" style="height: 25px" ' +
                'ng-click="grid.appScope.exportByCity(row.entity.groupCity)">导出</button><div>',
                enableSorting: false,
                minWidth: 150
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {
            });
            $scope.gridApi.treeBase.on.rowExpanded($scope, function(row) {
            });
        }
    };

    function setRegionGridByparam() {
        $scope.isLoading = true;
        GroupServ.getGroupRegionStaticDataPage().success(function (data) {
            if (data.code == 200) {
                var cityData = [];
                for(var i = 0; i < data.list.length; i++){

                    if(data.list[i].regionLevel == 0){
                        data.list[i].$$treeLevel = 0
                        cityData.push(data.list[i]);
                    } else{
                        //data.list[i].$$treeLevel = 1;
                    }
                }
                var gridList = [];
                for(var i = 0; i < cityData.length; i++){
                    gridList.push(cityData[i]);
                    for(var j = 0; j < data.list.length; j++){
                        if(data.list[j].groupCity == cityData[i].groupCity && data.list[j].$$treeLevel != 0){
                            data.list[j].groupCity == '';
                            gridList.push(data.list[j]);
                        }
                    }
                }

                $scope.gridRegionOptions.data = gridList;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    };



    $scope.export = function(exportType){
        $scope.isLoading = true;

        var addParams = {};
        addParams['type'] =  $scope.type;

        if(exportType == 1){
            GroupServ.exportBasicExcel($scope.params, addParams).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "队伍统计_基本统计.xls";
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();

                $scope.isSuccessed = true;
                swal("导出成功");
            });
        }else{
            GroupServ.exportTypeExcel($scope.params, addParams).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "队伍统计_按类型统计.xls";
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

    $scope.exportByCity = function(city){
        $scope.isLoading = true;

        var addParams = {};
        addParams['city'] =  city;
        GroupServ.exportRegionExcel($scope.params, addParams).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var  fileName = "队伍统计_按地区统计.xls";
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

}
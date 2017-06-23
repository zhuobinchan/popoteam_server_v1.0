/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/activityServ', 'service/commonServ'], function (app) {
    app.controller('ActivityCtrl', ActivityCtrl);
});

ActivityCtrl.$inject = ['$scope', 'ActivityServ', 'CommonServ','$interval'];

function ActivityCtrl($scope, ActivityServ, CommonServ, $interval) {
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
                field: 'id',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '约会名',
                field: 'name',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '队伍A',
                field: 'groupAName',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '队伍B',
                field: 'groupBName',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '聊天室Id',
                field: 'roomId',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '状态',
                field: 'type',
                cellFilter: 'activityStatusFilter',
                minWidth: 40
            },
            {
                name: '解散方式',
                field: 'expireType',
                cellTemplate: '<div ng-if="row.entity.expireType == 0 && row.entity.type == 1">系统解散</div>' +
                                '<div ng-if="row.entity.expireType == 1 && row.entity.type == 1">主动解散</div>'+
                                '<div ng-if="row.entity.expireType == 2 && row.entity.type == 1">强制解散</div>'+
                                '<div ng-if="row.entity.type == 0">--</div>',
                minWidth: 40
            },
            {
                name: '是否superlike',
                field: 'superLike',
                cellFilter: 'trueOrFalseFilter',
                minWidth: 40
            },
            {
                name: '是否永久有效',
                field: 'isExpire',
                cellTemplate: '<div ng-if="row.entity.isExpire == 1">是</div><div ng-if="row.entity.isExpire == 0">否</div>',
                minWidth: 40
            },
            {
                name: '约会人数',
                field: 'activityMemberCount',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '创建日期',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '存在时长(hour)',
                field: 'duration',
                minWidth: 80
            },
            {
                name: '创建日期',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '操作',
                enableSorting: false,
                cellTemplate:'<button ng-if="row.entity.type == 0 && row.entity.isExpire == 0" ' +
                'type="button" class="btn btn-default btn-console" ng-click="grid.appScope.setExpire(row.entity.id, 1)">设置永久有效</button>'+
                '<button ng-if="row.entity.type == 0 && row.entity.isExpire == 1" ' +
                'type="button" class="btn btn-default btn-console" ng-click="grid.appScope.setExpire(row.entity.id, 0)">取消永久有效</button>',
                minWidth: 90
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
            $scope.gridApi.core.on.sortChanged( $scope, function(grid, sortColumns){
                if(sortColumns.length > 0){
                    $scope.params.orderColumn = sortColumns[0].colDef.field;
                    $scope.params.orderTurn =  sortColumns[0].sort.direction;
                }else{
                    delete $scope.params.orderColumn;
                    delete $scope.params.orderTurn;
                }
                setGridByparam(grid.api.grid.options.paginationCurrentPage, grid.api.grid.options.paginationPageSize);
            });
        }
    };

    $scope.params = {};
    $scope.searchType = 'id';
    $scope.searchContent = '';
    $scope.groupName = '';
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;
        if ($scope.params.registerTimeBegin) {
            $scope.params.registerTimeBegin = moment($scope.params.registerTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.params.registerTimeEnd) {
            $scope.params.registerTimeEnd = moment($scope.params.registerTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        }
        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }
        if($scope.groupName){
            addParams['groupName'] = $scope.groupName;
        }
        ActivityServ.getByActivityDetailByParams($scope.params, addParams).success(function (data) {
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
        $scope.searchType = 'id';
        $scope.searchContent = '';
        $scope.groupName = '';
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    /*设置、取消约会永久性*/
    $scope.setExpire = function(activityId, type) {
        var addParams = {};
        addParams['id'] = activityId;
        addParams['isExpire'] = type;
        ActivityServ.setActivityExpire(addParams).success(function(data){
            setGridByparam(1, $scope.gridOptions.paginationPageSize);
        });

    }

    $scope.activityIds = [];
    $scope.dissolution = function () {
        $scope.activityIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.activityIds.length) {
            ActivityServ.dissolution($scope.activityIds).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.activityIds = [];
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    };


    $scope.activitySumStatisData = {};

    ActivityServ.getActivitySumStatisData().success(function(data){
        $scope.activitySumStatisData = data.activitySumStatisData;
    })


    $scope.export = function(){
        $scope.isLoading = true;

        if ($scope.params.registerTimeBegin) {
            $scope.params.registerTimeBegin = moment($scope.params.registerTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.params.registerTimeEnd) {
            $scope.params.registerTimeEnd = moment($scope.params.registerTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        }
        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }
        ActivityServ.exportActivity($scope.params, addParams).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var  fileName = "约会数据导出.xls";
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

    //var timer = $interval(function(){
    //    ActivityServ.getActivitySumStatisData().success(function(data){
    //        $scope.activitySumStatisData = data.activitySumStatisData;
    //    })
    //},10000);
    //timer.then(success);
    //function success(){
    //    console.log("done");
    //}
}
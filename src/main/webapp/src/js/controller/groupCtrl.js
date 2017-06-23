/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/groupServ', 'service/commonServ'], function (app) {
    app.controller('GroupCtrl', GroupCtrl);
});

GroupCtrl.$inject = ['$scope', 'GroupServ', 'CommonServ', '$interval', 'data_host','Upload'];

function GroupCtrl($scope, GroupServ, CommonServ, $interval, data_host, Upload) {
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
            //{
            //    name: 'ID',
            //    field: 'id',
            //    cellFilter: 'emptyFilter',
            //    minWidth: 80
            //},
            {
                name: '队名',
                field: 'name',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '状态',
                field: 'status',
                cellFilter: 'groupStatusFilter',
                minWidth: 40
            },
            {
                name: '性别',
                field: 'type',
                cellFilter: 'groupTypeFilter',
                minWidth: 40
            },
            {
                name: '省',
                field: 'province',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '市',
                field: 'city',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '地区',
                field: 'area',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '类型',
                field: 'bar.name',
                minWidth: 40
            },
            {
                name: '创建日期',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '修改日期',
                field: 'modifyTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },

            {
                name: '推荐队伍数',
                field: 'recommendCount',
                minWidth: 40
            },
            {
                name: '组成约会数',
                field: 'activityCount',
                minWidth: 40
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getGroup(row.entity)">查看</button>'
                +'<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getUserHeadImgByGroup(row.entity)" data-toggle="modal" data-target="#showCarouselDiaLog">查看头像</button>',
                enableSorting: false,
                minWidth: 150
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
    $scope.searchType = 'groupIds';
    $scope.searchContent = '';
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
        GroupServ.getByGroupDetailByParams($scope.params, addParams).success(function (data) {
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
        $scope.searchType = 'groupIds';
        $scope.searchContent = '';
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    $scope.groupIds = [];
    $scope.dissolution = function () {
        $scope.groupIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.groupIds.length) {
            GroupServ.dissolution($scope.groupIds).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.groupIds = [];
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    };

    $scope.groupSumStatisData = {};

    GroupServ.getGroupSumStatisData().success(function(data){
        $scope.groupSumStatisData = data.groupSumStatisData;
    })

    //var timer = $interval(function(){
    //    GroupServ.getGroupSumStatisData().success(function(data){
    //        $scope.groupSumStatisData = data.groupSumStatisData;
    //    })
    //},10000);
    //timer.then(success);
    //function success(){
    //    console.log("done");
    //}

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
        GroupServ.exportGroup($scope.params, addParams).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var  fileName = "队伍数据导出.xls";
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

    $scope.groupGrid = {
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
                name: '头像',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.user.head.url}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '昵称',
                field: 'user.nickName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '成员类型',
                field: 'type',
                cellTemplate: '<div ng-if="row.entity.type == 0">队长</div>' +
                '<div ng-if="row.entity.type == 1">队员</div>',
                minWidth: 100
            },
            {
                name: '性别',
                field: 'user.sex',
                cellFilter: 'gendeFilter',
                minWidth: 100
            },
            {
                name: 'ID',
                // field: 'user.identify',
                cellFilter: 'emptyFilter',
                cellTemplate:'<a data-dismiss="modal" ui-sref="userDetails({userId:row.entity.user.userId})">{{row.entity.user.identify}}</a>',
                minWidth: 100
            },
            {
                name: '省',
                field: 'user.province',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '市',
                field: 'user.city',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '注册日期',
                field: 'user.createTime',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '状态',
                field: 'user.userBase.freeze',
                cellFilter: 'freezeTypeFilter',
                minWidth: 100
            }
        ],
        onRegisterApi: function (gridApi) {
            //$scope.gridApi = gridApi;
        }
    };

    function setGroupGridByparam(group) {
        $scope.isLoading = true;

        var addParams = {};
        addParams['id'] = group.id;
        addParams['status'] = group.status;
        GroupServ.getGroupMemberByParams(addParams).success(function (data) {
            if (data.code == 200) {
                $scope.groupGrid.data = data.list;
            }
            $scope.isSuccessed = true;
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    $scope.userDatasFromGroup = null;
    $scope.getUserHeadImgByGroup = function (group) {
        var addParams = {};
        addParams['id'] = group.id;
        addParams['status'] = group.status;
        GroupServ.getGroupMemberByParams(addParams).success(function (data) {
            if (data.code == 200) {
                $scope.userDatasFromGroup = data.list;
            }
            $scope.isSuccessed = true;
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

   $scope.getUserHeadImgByGroupIds = function(){
       $scope.groupIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');
       if ($scope.groupIds.length) {
           GroupServ.getGroupMemberByIds($scope.groupIds).success(function (data) {
               if (data.code == 200) {
                   $scope.groupIds = [];
                   $scope.userDatasFromGroup = data.list;
               } else {
                   swal("操作失败，请稍后再试");
               }
           }).error(function (r) {
               console.log(r)
           });
       }else {
           swal("请选择队伍");
       }
    }

    $scope.getGroup = function(group){
        setGroupGridByparam(group);
        $('#group-detail').modal('show');
    }

    $scope.exportByFile = function () {
        if($scope.groupFile != null){
            $scope.isLoading = true;
            if ($scope.downloadType==1){var urlString = data_host + '/ctrl/group/exportGroupByFile'}
            if ($scope.downloadType==2){var urlString = data_host + '/ctrl/group/exportGroupWithUserByFile'}
            Upload.upload({
                url: urlString,
                method: 'POST',
                transformRequest: angular.identity,
                data: {
                    groupFile: $scope.groupFile,
                },
                responseType: "blob"
            }).success(function (data, status, headers, config)  {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var  fileName = "数据导出.xls"
                if ($scope.downloadType==1){fileName = "队伍筛选后数据导出.xls";}
                if ($scope.downloadType==2){fileName = "队伍筛选后数据导出(带用户).xls";}
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.download = fileName;
                a.href = URL.createObjectURL(blob);
                a.click();
                $scope.groupFile = null;

                $scope.isSuccessed = true;
                swal("操作成功");
                $('#exportGroupByFile').modal('hide');
            }, function (resp) {
                swal("上传失败，请稍后尝试");
            })['finally'](function () {
                $scope.isLoading = false;
            });
        }else {
            swal("没有上传文件，请上传");
        }

    }

}
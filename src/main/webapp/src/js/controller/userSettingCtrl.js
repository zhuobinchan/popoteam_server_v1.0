/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/userServ', 'service/commonServ'], function (app) {
    app.controller('UserSettingCtrl', UserSettingCtrl);
});

UserSettingCtrl.$inject = ['$scope', 'UserServ', 'CommonServ','$interval'];

function UserSettingCtrl($scope, UserServ, CommonServ, $interval) {
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
                name: '头像',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.head.url}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '昵称',
                field: 'nickName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '性别',
                field: 'sex',
                cellFilter: 'gendeFilter',
                minWidth: 60
            },
            {
                name: '年龄',
                field: 'age',
                minWidth: 60
            },
            {
                name: 'ID',
                field: 'identify',
                cellFilter: 'emptyFilter',
                minWidth: 80
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
                name: '上次登录地点',
                field: 'city',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '上次登录时间',
                field: 'city',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '注册日期',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '状态',
                field: 'userBase.freeze',
                cellFilter: 'freezeTypeFilter',
                minWidth: 60
            },
            {
                name: '邀请数',
                field: 'invitedTimes',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '7天内邀请次数',
                field: 'invitedTimesInWeek',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '最近一次发起邀请时间',
                field: 'invitedLatelyDate',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '被邀请数',
                field: 'beInvitedTimes',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '7天内被邀请次数',
                field: 'beInvitedTimesInWeek',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '最近一次被邀请时间',
                field: 'beInvitedLatelyDate',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '好友数',
                field: 'friendsNumber',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '7天内好友新增量',
                field: 'friendsNumberInWeek',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '最近一次新增好友时间',
                field: 'friendsNewestDate',
                cellFilter: 'emptyFilter',
                minWidth: 150
            },
            {
                name: '操作',
                cellTemplate: '<a ui-sref="userDetails({userId:row.entity.userId})">' +
                '<button type="button" class="btn btn-default btn-console">查看详情</button></a>'
             +'<button type="button" class="btn btn-default btn-console"  ="open()">修改</button>',
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

    /*
     * 省市区列表
     */
    $scope.provinceList = [];
    $scope.cityList = [];
    $scope.areaList = [];

    /*
     * 获取省市区
     */
    $scope.getDistrict = function (type, code) {

        CommonServ.getDistricts(type, code).success(function (data) {
            if (type == 1) {
                $scope.provinceList = data.list;
            } else if (type == 2) {
                $scope.cityList = data.list;
            }
        }).error(function (r) {
            console.log(r);
        });
    };

    /*  一开始先拿到所有的省的列表 */
    $scope.getDistrict(1);

    $scope.params = {};
    $scope.searchType = 'nickName';
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
        UserServ.getByParamsInPages($scope.params, addParams).success(function (data) {
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
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    $scope.userIds = [];
    $scope.freeze = function (status) {
        $scope.userIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'userId');

        if ($scope.userIds.length) {
            UserServ.freeze($scope.userIds, status).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.userIds = [];
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }

    };

    $scope.userSumStatisData = {};
    UserServ.getUserSumStatisData().success(function(data){
        $scope.userSumStatisData = data.userSumStatisData;
    });
    //var timer = $interval(function(){
    //    UserServ.getUserSumStatisData().success(function(data){
    //        $scope.userSumStatisData = data.userSumStatisData;
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
        UserServ.export($scope.params, addParams).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var  fileName = "用户数据导出.xls";
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
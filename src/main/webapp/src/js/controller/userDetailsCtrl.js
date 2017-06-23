/**
 * Created by sherry on 2016/6/13.
 */
/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/userServ', 'service/commonServ', 'service/groupServ', 'service/activityServ'], function (app) {
    app.controller('UserDetailsCtrl', UserDetailsCtrl);
});

UserDetailsCtrl.$inject = ['$scope', 'UserServ', 'CommonServ', 'GroupServ', 'ActivityServ', '$stateParams'];

function UserDetailsCtrl($scope, UserServ, CommonServ, GroupServ, ActivityServ, $stateParams) {
    $scope.userId = $stateParams.userId;
    var moment = require('moment');

    /* 获取用户资料详情 */
    $scope.getUserDetails = function (userId) {
        UserServ.getById(userId).success(function (data) {
            $scope.userObj = data.object;
        })
    }($scope.userId);

    /* 好友列表 */
    $scope.gridOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        selectionRowHeaderWidth: 35,
        useExternalPagination: true,
        useExternalFiltering: false,
        enableFiltering: false,
        rowHeight: 40,
        columnDefs: [
            {
                name: '头像',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.head.url}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 40
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
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '年龄',
                field: 'age',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: 'ID',
                // field: 'identify',
                cellFilter: 'emptyFilter',
                cellTemplate:'<a ui-sref="userDetails({userId:row.entity.userId})">{{row.entity.identify}}</a>',
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
                name: '注册日期',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '状态',
                field: 'type',
                cellFilter: 'freezeTypeFilter',
                minWidth: 40
            },
            {
                name: '邀请数',
                field: 'invitedTimes',
                cellFilter: 'emptyFilter', minWidth: 40
            },
            {
                name: '被邀请数',
                field: 'beInvitedTimes',
                cellFilter: 'emptyFilter', minWidth: 40
            },
            {
                name: '好友数',
                field: 'friendsNumber',
                cellFilter: 'emptyFilter', minWidth: 40
            },
            {
                name: '点赞',
                field: 'photoLiked',
                cellFilter: 'emptyFilter', minWidth: 40
            },
            {
                name: '被赞',
                field: 'photoBeLiked',
                cellFilter: 'emptyFilter', minWidth: 40
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getInfoDetails(row.entity)">编辑</button>',
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
        }
    };


    $scope.params = {};


    /* 好友列表 */
    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;
        $scope.params.userId = $scope.userId;
        UserServ.getFriendsById($scope.params).success(function (data) {
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

    $scope.photoList = [];
    $scope.searchPhoto = function (page, size) {
        $scope.isLoadingPhoto = true;
        UserServ.searchPhoto(page, size, $scope.userId).success(function (data) {
            if (data.code == 200) {
                $scope.photoList = data.list;
                $scope.isSuccessedPhoto = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoadingPhoto = false;
        });
    };

    /*$scope.showDetail = function (entity) {
        $scope.curObj = entity;
        console.log($scope.curObj);
    }*/
    /* 队伍列表 */
    $scope.groupGridOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        selectionRowHeaderWidth: 35,
        useExternalPagination: true,
        useExternalFiltering: false,
        enableFiltering: false,
        rowHeight: 40,
        columnDefs: [
            {
                name: '队名',
                field: 'name',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '省',
                field: 'province',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '市',
                field: 'city',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '地区',
                field: 'area',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '类型',
                field: 'bar.name',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '创建时间',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '修改时间',
                field: 'modifyTime',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '是否有效',
                cellTemplate : '<div ng-if="row.entity.status == 0">创建中</div><div ng-if="row.entity.status == 1">投票中</div>' +
                '<div ng-if="row.entity.status == 3">解散</div>',
                cellFilter: 'emptyFilter',
                minWidth: 100
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setGroupGridByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    function setGroupGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;
        $scope.params.userId = $scope.userId;
        GroupServ.getByGroupMemberId($scope.params).success(function (data) {
            if (data.code == 200) {
                $scope.groupGridOptions.totalItems = data.totalSize;
                $scope.groupGridOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }



    /* 约会列表 */
    $scope.activityGridOptions = {
        paginationPageSizes: [15, 30, 100],
        paginationPageSize: 15,
        enableSorting: true,
        selectionRowHeaderWidth: 35,
        useExternalPagination: true,
        useExternalFiltering: false,
        enableFiltering: false,
        rowHeight: 40,
        columnDefs: [
            {
                name: '约会名',
                field: 'name',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '男性队伍',
                field: 'maleGroupName',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '女性队伍',
                field: 'femaleGroupName',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '类型',
                field: 'type',
                cellTemplate : '<div ng-if="row.entity.type == 0">约会开始</div><div ng-if="row.entity.type == 1">约会解散</div>' +
                '<div ng-if="row.entity.type == 2">约会成功</div>',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '创建时间',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '是否superLike',
                field: 'modifyTime',
                cellTemplate : '<div ng-if="row.entity.superLike == 0">否</div><div ng-if="row.entity.superLike == 1">是</div>',
                cellFilter: 'emptyFilter',
                minWidth: 100
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setActivityGridOptions(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    /* 队伍列表 */
    function setActivityGridOptions(page, size) {
        $scope.isLoading = true;

        $scope.params.page = page;
        $scope.params.size = size;
        $scope.params.userId = $scope.userId;
        ActivityServ.getByUserId($scope.params).success(function (data) {
            if (data.code == 200) {
                $scope.activityGridOptions.totalItems = data.totalSize;
                $scope.activityGridOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    $scope.getGroup = function (page, size) {
        setGroupGridByparam(page, $scope.groupGridOptions.paginationPageSize);
    };

    $scope.getActivity = function(page, size){
        setActivityGridOptions(page, $scope.activityGridOptions.paginationPageSize);
    }
}
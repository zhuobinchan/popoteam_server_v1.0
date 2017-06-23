/**
 * Created by sherry on 2016/6/8.
 */
define(['app', 'service/announceServ'], function (app) {
    app.controller('AnnounceHistoryCtrl', AnnounceHistoryCtrl);
});

AnnounceHistoryCtrl.$inject = ['$scope', 'AnnounceServ'];

function AnnounceHistoryCtrl($scope, AnnounceServ) {
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
                name: '标题',
                field: 'title',
                minWidth: 120
            },
            {
                name: '内容',
                field: 'content',
                minWidth: 100
            },
            {
                name: '状态',
                field: 'type',
                cellFilter: 'infoTypeFilter',
                minWidth: 80
            },
            {
                name: '发布者',
                field: 'userId',
                minWidth: 80
            },
            {
                name: '创建日期',
                field: 'createTime',
                minWidth: 80
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" data-toggle="modal"' +
                ' data-target="#news-detail" ng-click="grid.appScope.showDetail(row.entity)">查看详情</button>'
                +'<button type="button" class="btn btn-default btn-console"' +
                ' ng-click="grid.appScope.exportUser(row.entity.id)">被推用户</button>'
                ,
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
    $scope.searchType = 'title';
    $scope.searchContent = '';

    function setGridByparam(page, size) {
        $scope.params.page = page;
        $scope.params.size = size;
        if ($scope.params.sendTimeBegin) {
            $scope.params.sendTimeBegin = moment($scope.params.sendTimeBegin).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.params.sendTimeEnd) {
            $scope.params.sendTimeEnd = moment($scope.params.sendTimeEnd).format('YYYY-MM-DD hh:mm:ss');
        }
        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }
        $scope.isLoading = true;
        AnnounceServ.getByParamsInPages($scope.params, addParams).success(function (data) {
            if (data.code == 200) {
                $scope.gridOptions.totalItems = data.totalSize;
                $scope.gridOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
            $scope.queryParam = '';
        });
    }

    setGridByparam(1, $scope.gridOptions.paginationPageSize);

    $scope.searchParam = function () {
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    $scope.reset = function () {
        $scope.params = {};
        $scope.searchType = 'title';
        $scope.searchContent = '';
        setGridByparam(1, $scope.gridOptions.paginationPageSize);
    };

    $scope.announcementIds = [];
    $scope.block = function (status) {
        $scope.announcementIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.announcementIds.length) {
            AnnounceServ.block($scope.announcementIds, status).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.announcementIds = [];
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r);
            });
        }else {
            swal("请选择公告");
        }

    };

    $scope.exportUser = function (announcementId) {
        $scope.isLoading = true;
        AnnounceServ.exportUserById(announcementId).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            var fileName = announcementId+"的公告所发的用户信息.xls";
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

    $scope.showDetail = function(entity) {
        $scope.curObj = entity;
        console.log($scope.curObj);
    }
}
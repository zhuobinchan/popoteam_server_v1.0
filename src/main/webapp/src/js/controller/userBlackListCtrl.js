/**
 * Created by chenzhuobin on 2017/4/10.
 */
define(['app', 'service/userServ'], function (app) {

    app.controller('UserBlackListCtrl', UserBlackListCtrl);

});
UserBlackListCtrl.$inject = ['$scope','UserServ','Upload','data_host'];

function UserBlackListCtrl($scope,UserServ,Upload,data_host) {
    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'identify',
        val: ''
    };

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
                name: '昵称',
                field: 'user.nickName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '黑名单状态',
                field: 'status',
                cellFilter: 'trueOrFalseFilter',
                minWidth: 60
            },
            {
                name: 'ID',
                field: 'user.identify',
                cellFilter: 'emptyFilter',
                cellTemplate:'<a ui-sref="userDetails({userId:row.entity.user.userId})">{{row.entity.user.identify}}</a>',
                minWidth: 80
            },
            {
                name: '手机号码',
                field: 'user.phone',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.chgUserBlackListStatusById(row.entity.id,0)">解除</button>'
                +'<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.chgUserBlackListStatusById(row.entity.id,1)">拉黑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {
             });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getUserBlackLists(newPage, pageSize);
            });
        }
    };

    getUserBlackLists(1, $scope.gridOptions.paginationPageSize);

    function getUserBlackLists(page, size) {
        $scope.isLoading = true;
        UserServ.getUserBlackListsInPage(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.gridOptions.totalItems = data.totalSize;
                $scope.gridOptions.data = data.list;
                $scope.isSuccessed = true;
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200)
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function () {
            $scope.isLoading = false;
        });
    }


    $scope.searchUserBlackList = function () {
        if (!$scope.searchParams.val ) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getUserBlackLists(1, $scope.gridOptions.paginationPageSize);

    }

    $scope.resetUserBlackList = function () {
        $scope.params = '';
        $scope.searchParams = {
            key: 'identify',
            val: '',
        };
        getUserBlackLists(1, $scope.gridOptions.paginationPageSize);
    }

    $scope.chgUserBlackListStatus =  function (status) {
        $scope.userBlackListIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.userBlackListIds.length) {
            UserServ.setUserBlackListStatusByIds($scope.userBlackListIds,status).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.userBlackListIds = [];
                    getUserBlackLists(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    }

    $scope.chgUserBlackListStatusById =  function(id,status){
        $scope.userBlackListIds = [];
        $scope.userBlackListIds.push(id);
        UserServ.setUserBlackListStatusByIds($scope.userBlackListIds,status).success(function (data) {
            if (data.code == 200) {
                swal("操作成功");
                $scope.userBlackListIds = [];
                getUserBlackLists(1, $scope.gridOptions.paginationPageSize);
            } else {
                swal("操作失败，请稍后再试");
            }
        }).error(function (r) {
            console.log(r)
        });
    }

    $scope.userType = 1;
    $scope.addUserBlackLists = function () {
        $scope.userBlackListIdentifiesOrPhones = [];
        $scope.userBlackListIdentifiesOrPhones =$("#identifiesOrPhones").val().split(",");
        UserServ.addUserBlackListsByIdsOrPhones($scope.userBlackListIdentifiesOrPhones , $scope.userType).success(function (data) {
            if (data.code == 200) {
                swal("操作成功");
                $scope.userBlackListIdentifiesOrPhones = [];
                $scope.identifiesOrPhones = '';
                getUserBlackLists(1, $scope.gridOptions.paginationPageSize);
                $('#addUserBlackList-dialog').modal("hide")
            } else {
                swal("操作失败，请稍后再试");
            }
        }).error(function (r) {
            console.log(r)
        });
    }


    // $('#advertisement-detail').on('hidden.bs.modal', function () {
    //     $scope.$apply(function () {
    //         $scope.advertisement = {
    //             title: ''
    //         };
    //     });
    // });
}

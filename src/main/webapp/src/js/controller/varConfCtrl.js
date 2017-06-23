/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/confServ'], function (app) {

    app.controller('VarConfCtrl', VarConfCtrl);

});

VarConfCtrl.$inject = ['$scope', 'ConfServ'];

function VarConfCtrl($scope, ConfServ) {

    $scope.updateObj = {
        superlike: '',
        groupNum: ''
    };
    $scope.oldSetting = {};

    $scope.getSetting = function () {
        ConfServ.getVarSetting().success(function (data) {
            if (data.code == 200) {
                $scope.oldSetting = data;
            }
        }).error(function (err) {
            console.log(err);
        });
    };

    $scope.getSetting();

    $scope.changeSetting = function () {
        console.log($scope.updateObj);
        ConfServ.updateVarSetting($scope.updateObj).success(function (data) {
            if (data.code == 200) {
                swal('修改成功');
            }
        }).error(function (err) {
            swal(GCONSTANTS.MSG.ERR500);
            console.log(err);
        })['finally'](function () {
            $scope.updateObj = {};
            $scope.getSetting();
        });
    }


    //用户superlike次数管理部分start
    $scope.userSuperLikeGridOptions = {
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
                name: 'superLike次数',
                field: 'times',
                cellFilter: 'emptyFilter',
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
                'ng-click="grid.appScope.getUserSuperLikeDetails(row.entity)">编辑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {
             });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getUserSuperLikeConfig(newPage, pageSize);
            });
        }
    };

    getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
    
    function getUserSuperLikeConfig(page, size) {
        $scope.isLoading = true;
        ConfServ.searchSuperLikeConfigInPage(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.userSuperLikeGridOptions.totalItems = data.totalSize;
                $scope.userSuperLikeGridOptions.data = data.list;
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

    $scope.searchUserSuperLikeConfig = function () {
        if (!$scope.searchParams.val ) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
    }

    $scope.searchParams = {
        key: 'identify',
        val: '',
    };
    $scope.resetUserSuperLikeConfig = function () {
        $scope.params = '';
        $scope.searchParams = {
            key: 'identify',
            val: '',
        };
        getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
    }

    $scope.getUserSuperLikeDetails = function(UserSuperLike) {
        $scope.userSuperLikeConfig = angular.copy(UserSuperLike);
        $('#UserSuperLike-dialog').modal('show');
    }

    $scope.saveUserSuperLikeConfig = function () {
        var submitObj = angular.copy($scope.userSuperLikeConfig);
        if(submitObj.id){//编辑
            ConfServ.updateUserSuperLikeConfig(submitObj.id,submitObj.times).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
                }else {
                    swal("操作失败，请稍后再试");
                }
                $scope.userSuperLikeConfig = {
                    id: ''
                };
                $('#UserSuperLike-dialog').modal('hide');
            }).error(function (r) {
                console.log(r)
            });
        }else {//新建
            if((submitObj.user.identify!=null&&submitObj.user.phone!=null)||(submitObj.user.identify==null&&submitObj.user.phone==null)){
                swal("用户Id和电话，两项填写其中一项即可");
                return;
            }
            if(submitObj.user.phone!=null && !(submitObj.user.phone.indexOf("+86")>=0)){
                submitObj.user.phone="+86"+submitObj.user.phone
            }
            ConfServ.addUserSuperLikeConfig(submitObj.user.identify,submitObj.user.phone,submitObj.times).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
                }else if (data.code==501){
                    swal("操作失败，新建的用户已经存在该列表，请直接查出，进行修改！");
                }else if (data.code==719){
                    swal("没有该用户！");
                } else {
                    swal("操作失败，请稍后再试");
                }
                $scope.userSuperLikeConfig = {
                    id: ''
                };
                $('#UserSuperLike-dialog').modal('hide');
            }).error(function (r) {
                console.log(r)
            });
        }
    }

    $scope.deleteUserSuperLikeConfig = function () {
        var userSuperLikeConfigIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');
        if (userSuperLikeConfigIds.length) {
            ConfServ.deleteUserSuperLikeConfigByIds(userSuperLikeConfigIds).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    getUserSuperLikeConfig(1, $scope.userSuperLikeGridOptions.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }else {
            swal("没有选择");
        }
    }

    $('#UserSuperLike-dialog').on('hidden.bs.modal', function () {
        $scope.$apply(function () {
            $scope.userSuperLikeConfig = {
                id: ''
            };
        });
    });
    //用户superlike次数管理部分end

}
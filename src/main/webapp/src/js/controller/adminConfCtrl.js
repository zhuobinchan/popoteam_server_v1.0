/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/adminServ', 'service/roleServ', 'service/permissionServ'], function (app) {
    app.controller('AdminConfCtrl', AdminConfCtrl);
});

AdminConfCtrl.$inject = ['$scope', 'AdminServ', 'RoleServ', 'PermissionServ'];

function AdminConfCtrl($scope, AdminServ, RoleServ, PermissionServ) {
    var moment = require('moment');
    // 管理员列表
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
                field: 'userId',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '名称',
                field: 'nickName',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '角色',
                field: 'roleName',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '创建时间',
                field: 'createTime',
                cellFilter: 'emptyFilter',
                minWidth: 40
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getAdminDetails(row.entity)">编辑</button>' +
                '<button type="button" class="btn btn-danger btn-console" ' +
                'ng-click="grid.appScope.deleteAdmin(row.entity.userId)">删除</button>',
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

    // 角色列表
    $scope.roleGridOptions = {
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
                field: 'roleId',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '角色名',
                field: 'roleName',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '描述',
                field: 'extra',
                cellFilter: 'emptyFilter',
                minWidth: 100
            },
            {
                name: '菜单权限',
                field: 'permissionList',
                cellFilter: 'emptyFilter',
                minWidth: 120
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getRoleDetails(row.entity)">编辑</button>' +
                '<button type="button" class="btn btn-danger btn-console" ' +
                'ng-click="grid.appScope.deleteRole(row.entity.roleId)">删除</button>',
                minWidth: 90
            }

        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                setRoleGridByparam(newPage, pageSize);
            });
            //外置搜索
            $scope.gridApi.core.on.filterChanged($scope, function () {

            });
        }
    };

    $scope.params = {};
    $scope.isLoading = false;

    function setGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        var addParams = {};
        if($scope.searchContent){
            addParams[$scope.searchType] = $scope.searchContent;
        }

        AdminServ.getAdminList($scope.params, addParams).success(function (data) {
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

    function setRoleGridByparam(page, size) {
        $scope.isLoading = true;
        $scope.params.page = page;
        $scope.params.size = size;

        RoleServ.getRolePermissionList($scope.params).success(function (data) {
            if (data.code == 200) {
                $scope.roleGridOptions.totalItems = data.totalSize;
                $scope.roleGridOptions.data = data.list;

                $scope.isSuccessed = true;
            }
        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
            $scope.isLoading = false;
        });
    }

    setGridByparam(1, $scope.gridOptions.paginationPageSize);

    /** 初始化系统角色 **/
    function initRoleList(){
        RoleServ.getRoleList().success(function (data) {
            $scope.roleList = data.list;
        }).error(function (r) {
            console.log(r);
        })
    }

    /** 初始化系统菜单权限 **/
    function initPermissionList(){
        PermissionServ.getPermissionList().success(function (data) {
            $scope.permissionList = data.list;
        }).error(function (r) {
            console.log(r);
        })
    }



    $scope.isSelected = function(id){
        var isSelected = false;
        $scope.admin.roles.forEach(function(p){
            if (p == id){
                isSelected = true;
                return true;
            }
        });
        return isSelected;
    };

    $scope.isRoleSelected = function(id){
        var isSelected = false;
        $scope.role.permissionList.forEach(function(p){
            if (p == id){
                isSelected = true;
                return true;
            }
        });
        return isSelected;
    }

    /** 管理员 **/
    $scope.admin = {
        userId : '',
        account : '',
        password : '',
        roles : new Array()
    };

    /** 角色 **/
    $scope.role = {
        roleId : '',
        roleName : '',
        extra: '',
        permissionList : new Array()
    };

    /** 角色checkbox点击事件 **/
    $scope.toggleSelection = function toggleSelection(id) {
        var idx = $scope.admin.roles.indexOf(id);
        if (idx > -1) {
            $scope.admin.roles.splice(idx, 1);
        } else {
            $scope.admin.roles.push(id);
        }
    };
    $scope.toggleRoleSelection = function toggleSelection(id) {
        var idx = $scope.role.permissionList.indexOf(id);
        if (idx > -1) {
            $scope.role.permissionList.splice(idx, 1);
        } else {
            $scope.role.permissionList.push(id);
        }

    };

    /** 保存管理员 **/
    $scope.saveAdmin = function(){

        if($scope.admin.userId){
            //修改
            AdminServ.updateAdminRole($scope.admin.userId,$scope.admin.roles).success(function (data) {
                swal(data.message);
                if (200 == data.code){
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                    $('#admin-detail').modal('hide');
                }
            }).error(function (r) {
                console.log(r);
            })

        }else{
            //新增

            AdminServ.registerAdmin($scope.admin).success(function (data) {
                swal(data.message);
                if (200 == data.code){
                    setGridByparam(1, $scope.gridOptions.paginationPageSize);
                    $('#admin-detail').modal('hide');
                }
            }).error(function (r) {
                console.log(r);
            })
        }

    }

    /** 保存角色 **/
    $scope.saveRole = function(){
        if($scope.role.roleId){
            //修改
            RoleServ.updateRolePermission($scope.role).success(function (data) {
                swal(data.message);
                if (200 == data.code){
                    setRoleGridByparam(1, $scope.roleGridOptions.paginationPageSize);
                    $('#role-detail').modal('hide');
                }
            }).error(function (r) {
                console.log(r);
            })

        }else{
            //新增
            RoleServ.addRolePermission($scope.role).success(function (data) {
                swal(data.message);
                if (200 == data.code){
                    setRoleGridByparam(1, $scope.roleGridOptions.paginationPageSize);
                    $('#role-detail').modal('hide');
                }
            }).error(function (r) {
                console.log(r);
            })

        }
    }
    /** modal窗口关闭事件，清除输入值 **/
    $('#admin-detail').on('hidden.bs.modal', function () {
        $scope.admin = {
            userId : '',
            account : '',
            password : '',
            roles : new Array()
        };
        $scope.roleList ={};
        initRoleList();
    });

    $('#role-detail').on('hidden.bs.modal', function () {
        $scope.role = {
            roleId : '',
            roleName : '',
            extra: '',
            permissionList : new Array()
        };
        $scope.permissionList ={};
        initPermissionList();
    });

    /** 删除管理员 **/
    $scope.deleteAdmin = function(userId){
        AdminServ.deleteAdminByUserId(userId).success(function (data) {
            swal(data.message);
            setGridByparam(1, $scope.gridOptions.paginationPageSize);
        }).error(function (r) {
            console.log(r);
        })
    };

    /** 删除角色 **/
    $scope.deleteRole = function(roleId){
        RoleServ.deleteRoleById(roleId).success(function (data) {
            swal(data.message);
            setRoleGridByparam(1, $scope.roleGridOptions.paginationPageSize);
        }).error(function (r) {
            console.log(r);
        })
    };

    /** 初始化详情 **/
    $scope.getAdminDetails = function(entity) {
        $scope.admin.account = entity.account;
        if(entity.roleIds !== null){
            $scope.admin.roles = $.map(entity.roleIds.split(','), function(value){
                return parseInt(value, 10);
            });
        }
        $scope.admin.userId = entity.userId;

        $scope.permissionList ={};
        $('#admin-detail').modal('show');
    }

    $scope.getRoleDetails = function(entity) {
        $scope.role.roleId = entity.roleId;
        $scope.role.roleName = entity.roleName;
        $scope.role.extra = entity.extra;
        if(entity.permissionIds !== null){
            $scope.role.permissionList = $.map(entity.permissionIds.split(','), function(value){
                return parseInt(value, 10);
            });
        };
        $scope.roleList ={};
        $('#role-detail').modal('show');
    }

    /** tab页事件 **/
    $scope.search = function(type) {
        if (type == 1) {
            setGridByparam(1, $scope.gridOptions.paginationPageSize);
        } else if (type == 2) {
            setRoleGridByparam(1, $scope.roleGridOptions.paginationPageSize);
        }
    }
    initRoleList();
    initPermissionList();
}
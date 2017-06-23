/**
 * Created by chenzhuobin on 2017/3/27.
 */
define(['app', 'service/barServ'], function (app) {

    app.controller('BarCtrl', BarCtrl);

});
BarCtrl.$inject = ['$scope','BarServ','Upload','data_host'];

function BarCtrl($scope,BarServ,Upload,data_host) {
    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'name',
        val: ''
    };

    $scope.bar = {
        name: '',
        status: 1,
        isExpire:0,
        actionType:0
    };
    $scope.barStatusOpts = [
        {txt:'公开',val:1},
        {txt:'屏蔽中',val:0}
    ];
    $scope.trueOrFalseOpts = [
        {txt:'是',val:1},
        {txt:'否',val:0}
    ];

    $scope.isLoading = false;
    $scope.getBarDetails = getBarDetails;
    $scope.searchBarsData = searchBarsData;
    $scope.resetBarsData = resetBarsData;
    $scope.chgBarsStatus = chgBarsStatus;
    $scope.saveBar = saveBar;
    $scope.file = null;
    $scope.imageFile = null;
    $scope.imageSmallFile = null;

    $scope.barGridOpts = {
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
                name: '栏位ID',
                field: 'id',
                minWidth: 60
            },
            {
                name: '名称',
                field: 'name',
                minWidth: 80
            },
            {
                name: '栏位图片',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.url}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '栏位缩小图',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.url | barImgFilter}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },
            {
                name: '内容',
                field: 'content',
                minWidth: 80
            },
            {
                name: '是否屏蔽',
                field: 'status',
                cellFilter:'barStatusFilter',
                minWidth: 80
            },
            {
                name: '优先级別',
                field: 'priority',
                minWidth: 80
            },
            {
                name: '是否永久有效',
                field: 'isExpire',
                cellFilter:'trueOrFalseFilter',
                minWidth: 80
            },
            {
                name: '是否进入商城',
                field: 'actionType',
                cellFilter:'trueOrFalseFilter',
                minWidth: 80
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getBarDetails(row.entity)">编辑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {
             });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getBars(newPage, pageSize);
            });
        }
    };
    getBars(1, $scope.barGridOpts.paginationPageSize);

    function getBars(page, size) {
        $scope.isLoading = true;
        BarServ.getBarInPages(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.barGridOpts.totalItems = data.totalSize;
                $scope.barGridOpts.data = data.list;
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
    function getBarDetails(bar) {
        $scope.bar = angular.copy(bar);
        $('#bar-detail').modal('show');
    }

    function searchBarsData() {
        if (!$scope.searchParams.val ) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getBars(1, $scope.barGridOpts.paginationPageSize);
    }

    function resetBarsData() {
        $scope.params = '';
        $scope.searchParams = {
            key: 'name',
            val: '',
        };
        getBars(1, $scope.barGridOpts.paginationPageSize);
    }

    function chgBarsStatus(status) {
        $scope.barIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.barIds.length) {
            BarServ.setBarStatusByIds($scope.barIds,status).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.barIds = [];
                    getBars(1, $scope.barGridOpts.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    }

    function saveBar() {
        var submitObj = angular.copy($scope.bar);
        // var files = [];
        // files.push($scope.imageFile);
        // files.push($scope.imageSmallFile);


        $scope.inputDate = {
            // smallFile:$scope.imageSmallFile,
            //     file: $scope.imageFile,
            // files:files,
            id:submitObj.id,
            name: submitObj.name,
            content: submitObj.content,
            priority: submitObj.priority,
            isExpire: submitObj.isExpire,
            actionType: submitObj.actionType,
        };
        if ($scope.imageFile!=null){$scope.inputDate.file = $scope.imageFile }
        if ($scope.imageSmallFile!=null){$scope.inputDate.smallFile = $scope.imageSmallFile}
        if (submitObj.status!=null){$scope.inputDate.status = submitObj.status}

        if (submitObj.id) {//编辑
            Upload.upload({
                url: data_host + '/ctrl/bar/update',
                // + $.param({
                //     id:submitObj.id,
                //     name: submitObj.name,
                //     content: submitObj.content,
                //     status: submitObj.status,
                //     priority: submitObj.priority,
                //     isExpire: submitObj.isExpire,
                //     actionType: submitObj.actionType,
                // }),
                method: 'POST',
                // dataType: "json",
                transformRequest: angular.identity,
                // files:$scope.files,
                data: $scope.inputDate,
            }).then(function (resp) {
                if (resp.data.code == 200) {
                    swal({
                        title: '修改成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#bar-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.bar = {
                                name: '',
                                status: 1,
                                isExpire:0,
                                actionType:0
                            };
                        });
                    });
                    getBars(1, $scope.barGridOpts.paginationPageSize);
                }
            }, function (resp) {
                swal('发布失败，请稍后再试');
            })['finally'](function () {
                $scope.uploading = false;
                $scope.imageFile=null;
            });

        } else {//新建

            Upload.upload({
                url: data_host + '/ctrl/bar/add',
                // + $.param({
                //     id:submitObj.id,
                //     name: submitObj.name,
                //     content: submitObj.content,
                //     status: submitObj.status,
                //     priority: submitObj.priority,
                //     isExpire: submitObj.isExpire,
                //     actionType: submitObj.actionType,
                // }),
                method: 'POST',
                // dataType: "json",
                // files:$scope.files,
                transformRequest: angular.identity,
                data:  $scope.inputDate
            }).then(function (resp) {
                if (resp.data.code == 200) {
                    swal({
                        title: '新建成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#bar-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.bar = {
                                name: '',
                                isExpire:0,
                                actionType:0
                            };
                        });
                    });
                    getBars(1, $scope.barGridOpts.paginationPageSize);
                }
            }, function (resp) {
                swal('发布失败，请稍后再试');
            })['finally'](function () {
                $scope.uploading = false;
                $scope.imageFile=null;
            });

        }
    }

    $scope.groupDissolve = 1;
    $scope.activityDissolve = 1;

    $scope.chgBarStatusAndDissolve = function(){
        $scope.barIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.barIds.length) {
            if($scope.groupDissolve==2){
                BarServ.setBarStatusScreenAndGroupChgOtherActivity($scope.barIds,$scope.otherBarId,$scope.activityDissolve).success(function (data) {
                        if (data.code == 200) {
                            swal("操作成功");
                            $scope.barIds = [];
                            getBars(1, $scope.barGridOpts.paginationPageSize);
                            $('#status-Dialog').modal('hide');
                        } else {
                            swal("操作失败，请稍后再试");
                        }
                    }).error(function (r) {
                        console.log(r)
                    });
            }

            if($scope.groupDissolve==1){
                BarServ.setBarStatusScreenAndGroupWithActivityDissolve($scope.barIds,$scope.activityDissolve).success(function (data) {
                    if (data.code == 200) {
                        swal("操作成功");
                        $scope.barIds = [];
                        getBars(1, $scope.barGridOpts.paginationPageSize);
                        $('#status-Dialog').modal('hide');
                    } else {
                        swal("操作失败，请稍后再试");
                    }
                }).error(function (r) {
                    console.log(r)
                });
            }
        }else {
            swal("没有选择活动类型");
        }
    }

    $scope.otherBars=[];
    $scope.showOtherBarSelect = function(){
        $scope.selectBarIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.selectBarIds.length) {
            BarServ.getAllOtherBars($scope.selectBarIds,1).success(function (data) {
                if (data.code == 200) {
                    $scope.barIds = [];
                    $scope.otherBars = data.otherBars;
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }else {
            swal("没有选择活动类型");
        }
    }


    $('#bar-detail').on('hidden.bs.modal', function () {
        $scope.$apply(function () {
            $scope.bar = {
                name: '',
                isExpire:0,
                actionType:0
            };
        });
    });

    $("input[name=groupDissolveRadios]").click(function () {
        if($(this).val()==1){
            $('#otherBarsSelect').css('display','none');
        }
        if($(this).val()==2){
            $('#otherBarsSelect').css('display','block');
        }
    });

    $scope.showStatusDialog = function(){
        $scope.barIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');
        if ($scope.barIds.length) {
            $('#status-Dialog').modal('show');
        }else {
            swal("没有选择活动类型");
        }
    }
    
}


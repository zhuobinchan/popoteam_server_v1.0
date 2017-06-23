/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/interestLabelServ'], function (app) {

    app.controller('InterestLabelCtrl', InterestLabelCtrl);

});

InterestLabelCtrl.$inject = ['$scope', 'InterestLabelServ'];

function InterestLabelCtrl($scope, InterestLabelServ) {

    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'name',
        val: '',
        type: '',
        status: ''
    };
    $scope.label = {
        name: '',
        status: 1
    };
    $scope.labelStutasOpts = [
        {txt:'正常',val:1},
        {txt:'屏蔽',val:0}
    ];
    $scope.isLoading = false;
    $scope.chgLabelsStuta = chgLabelsStuta;
    $scope.searchLabelsData = searchLabelsData;
    $scope.resetLabelsData = resetLabelsData;
    $scope.saveLabel = saveLabel;
    $scope.getLabelDetails = getLabelDetails;

    $scope.interestGridOpts = {
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
                name: '标签ID',
                field: 'id',
                minWidth: 60
            },
            {
                name: '名称',
                field: 'name',
                minWidth: 80
            },
            {
                name: '已标记人数',
                field: 'identify',
                minWidth: 80
            },
            {
                name: '类型',
                field: 'type',
                cellFilter: 'labelTypeFilter',
                minWidth: 80
            },
            {
                name: '状态',
                field: 'status',
                cellFilter: 'labelStatusFilter',
                minWidth: 80
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getLabelDetails(row.entity)">编辑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.interestGridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {

            });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getLabels(newPage, pageSize);
            });
        }
    };

    getLabels(1, $scope.interestGridOpts.paginationPageSize);

    function getLabels(page, size) {
        $scope.isLoading = true;
        InterestLabelServ.getInterestLabelsInPages(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.interestGridOpts.totalItems = data.totalSize;
                $scope.interestGridOpts.data = data.list;
                $scope.isSuccessed = true;
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200)
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function(){
            $scope.isLoading = false;
        });
    }

    function chgLabelsStuta(isOpen) {
        var selectedItems = $scope.interestGridApi.selection.getSelectedRows();
        if (selectedItems.length <= 0) {
            swal(GCONSTANTS.MSG.NO_SELECTED);
            return;
        }
        var ids = _(selectedItems).pluck('id');
        InterestLabelServ.setInterestLabelStatus(ids, isOpen).success(function (data) {
            if (200 == data.code) {
                swal((isOpen ? '恢复' : '屏蔽') + '成功');
                angular.forEach(selectedItems, function (item) {
                    item.status = isOpen ? 1 : 0;
                });
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        });
    }

    function searchLabelsData() {
        if (!$scope.searchParams.val && !$scope.searchParams.type && !$scope.searchParams.status) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
            type: $scope.searchParams.type,
            status: $scope.searchParams.status
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getLabels(1, $scope.interestGridOpts.paginationPageSize);

    }

    function resetLabelsData() {
        $scope.params = '';
        $scope.searchParams = {
            key: 'name',
            val: '',
            type: '',
            status: ''
        };
        getLabels(1, $scope.interestGridOpts.paginationPageSize);
    }

    function getLabelDetails(label) {
        $scope.label = angular.copy(label);
        $('#label-detail').modal('show');
    }

    function saveLabel() {
        var submitObj = angular.copy($scope.label);
        if (submitObj.id) {//编辑
            InterestLabelServ.updateInterestLabel(submitObj).success(function (data) {
                if (200 == data.code) {
                    swal({
                        title: '编辑成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#label-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.label = {
                                name: '',
                                status: 1
                            };
                        });
                    });
                    getLabels(1, $scope.interestGridOpts.paginationPageSize);
                } else {
                    swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
                }
            }).error(function (err) {
                console.log(err);
                swal(GCONSTANTS.MSG.ERR500);
            });

        } else {//新建
            InterestLabelServ.addInterestLabel(submitObj).success(function (data) {
                if (200 == data.code) {
                    swal({
                        title: '新建成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#label-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.label = {
                                name: '',
                                status: 1
                            };
                        });
                    });
                    getLabels(1, $scope.interestGridOpts.paginationPageSize);
                } else {
                    swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
                }
            }).error(function (err) {
                console.log(err);
                swal(GCONSTANTS.MSG.ERR500);
            });
        }
    }

    $('#label-detail').on('hidden.bs.modal', function () {
        $scope.$apply(function () {
            $scope.label = {
                name: '',
                status: 1
            };
        });
    });

}
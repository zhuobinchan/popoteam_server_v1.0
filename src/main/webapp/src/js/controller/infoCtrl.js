/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/infoServ'], function (app) {

    app.controller('InfoCtrl', InfoCtrl);

});

InfoCtrl.$inject = ['$scope', 'InfoServ', 'data_host', 'Upload'];

function InfoCtrl($scope, InfoServ, data_host, Upload) {
    var moment = require('moment');

    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'title',
        val: '',
        sendTimeBegin: '',
        sendTimeEnd: ''
    };
    $scope.info = {
        title: '',
        url: '',
        imageId: 348
    };
    $scope.isLoading = false;
    $scope.searchInfosData = searchInfosData; //筛选
    $scope.resetInfosData = resetInfosData; //重置筛选
    $scope.chgInfosStuta = chgInfosStuta; //屏蔽、恢复
    $scope.saveInfo = saveInfo; //发布资讯
    $scope.getInfoDetails = getInfoDetails; //发布资讯

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
                name: '发布日期',
                field: 'createTime',
                minWidth: 80
            },
            {
                name: '文章链接',
                field: 'url',
                minWidth: 80
            },
            {
                name: '状态',
                field: 'type',
                cellFilter: 'infoTypeFilter',
                minWidth: 80
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
                getInfos(newPage, pageSize);
            });
        }
    };

    getInfos(1, $scope.gridOptions.paginationPageSize);

    function getInfos(page, size) {
        $scope.isLoading = true;
        InfoServ.getInfosInPages(page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.gridOptions.totalItems = data.totalSize;
                $scope.gridOptions.data = data.list;
                $scope.isSuccessed = true;
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function () {
            $scope.isLoading = false;
        });
    }

    function searchInfosData() {
        if (!$scope.searchParams.val && !$scope.searchParams.sendTimeBegin && !$scope.searchParams.sendTimeEnd) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
            sendTimeBegin: $scope.searchParams.sendTimeBegin ? moment($scope.searchParams.sendTimeBegin).format('YYYY-MM-DD hh:mm:ss') : '',
            sendTimeEnd: $scope.searchParams.sendTimeEnd ? moment($scope.searchParams.sendTimeEnd).format('YYYY-MM-DD hh:mm:ss') : ''
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = '&' + $.param(objParams);
        getInfos(1, $scope.gridOptions.paginationPageSize);
    }

    function resetInfosData() {
        $scope.params = '';
        $scope.searchParams = {
            key: 'title',
            val: '',
            sendTimeBegin: '',
            sendTimeEnd: ''
        };
        getInfos(1, $scope.gridOptions.paginationPageSize);
    }

    function chgInfosStuta(isOpen) {
        var selectedItems = $scope.gridApi.selection.getSelectedRows();
        if (selectedItems.length <= 0) {
            swal(GCONSTANTS.MSG.NO_SELECTED);
            return;
        }
        var ids = _(selectedItems).pluck('id');
        InfoServ.setInfosStatus(ids, isOpen).success(function (data) {
            if (200 == data.code) {
                swal(isOpen ? '恢复' : '屏蔽' + '成功');
                angular.forEach(selectedItems, function (item) {
                    item.type = isOpen ? 0 : 1;
                });
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function (err) {
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        });
    }

    function saveInfo() {
        var submitObj = angular.copy($scope.info);
        if (submitObj.id) {
            delete submitObj.image;
            InfoServ.updateInfo(submitObj).success(function (data) {
                if (200 == data.code) {
                    swal({
                        title: '编辑成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#info-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.info = {
                                title: '',
                                url: '',
                                imageId: 348
                            };
                        });
                    });
                    getInfos(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
                }
            }).error(function (err) {
                console.log(err);
                swal(GCONSTANTS.MSG.ERR500);
            });
        } else {
            InfoServ.publishInfo(submitObj).success(function (data) {
                if (200 == data.code) {
                    swal({
                        title: '发布成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#info-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.info = {
                                title: '',
                                url: '',
                                imageId: 348
                            };
                        });
                    });
                    getInfos(1, $scope.gridOptions.paginationPageSize);
                } else {
                    swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
                }
            }).error(function (err) {
                console.log(err);
                swal(GCONSTANTS.MSG.ERR500);
            });
        }
    }


    /**
     * 上传图片
     */
    $scope.uploading = false;
    $scope.selectFiles = function (files, fileType) {
        if (files && files.length) {
            $scope.upload(files[0]);
        }
    };

    // 上传单个文件（图片）
    $scope.upload = function (fileObj) {
        $scope.uploading = true;
        Upload.upload({
            url: data_host + '/ctrl/common/upload',
            method: 'POST',
            transformRequest: angular.identity,
            data: {uploadfiles: fileObj, sizes: 0}
        }).then(function (resp) {
            if (resp.data.code == 200) {
                $scope.info.imageId = resp.data.succeed[0];
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
        });
    };


    function getInfoDetails(info) {
        if(info){
            $scope.info = angular.copy(info);
        }else{
            $scope.info = {};
        }
        $('#info-detail').modal('show');
    }

    $('#info-detail').on('hidden.bs.modal', function () {
        $scope.$apply(function () {
            $scope.info = {
                title: '',
                url: '',
                imageId: 348
            };
        });
    });

}
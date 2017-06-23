/**
 * Created by chenzhuobin on 2017/3/22.
 */
define(['app', 'service/advertisementServ', 'service/SimpleServ'], function (app) {

    app.controller('AdvertisementCtrl', AdvertisementCtrl);

});
AdvertisementCtrl.$inject = ['$scope', 'AdvertisementServ', 'Upload', 'data_host', 'SimpleServ'];

function AdvertisementCtrl($scope, AdvertisementServ, Upload, data_host, SimpleServ) {
    $scope.params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'title',
        val: '',
        status: ''
    };
    $scope.stutasOpts = [
        {txt: '正常', val: 1},
        {txt: '屏蔽', val: 0}
    ];
    $scope.advertisement = {
        name: '',
        status: 1,
        sortBy: 1,
        timeInterval: 3,
    };
    $scope.searchAdvertisementsData = searchAdvertisementsData;
    $scope.saveAdvertisement = saveAdvertisement;
    $scope.resetAdvertisementsData = resetAdvertisementsData;
    $scope.getAdvertisementDetails = getAdvertisementDetails;
    $scope.chgAdvertisementsStatus = chgAdvertisementsStatus;
    $scope.showBarSelect = showBarSelect;
    $scope.isLoading = false;
    $scope.file = null;
    $scope.imageFile = null;

    $scope.advertisementGridOpts = {
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
                name: '广告ID',
                field: 'id',
                minWidth: 60
            },
            {
                name: '标题',
                field: 'title',
                minWidth: 80
            },
            {
                name: '广告内容',
                field: 'content',
                minWidth: 80
            },
            {
                name: '排序规则（小到大）',
                field: 'sortBy',
                minWidth: 80
            },
            {
                name: '屏蔽状态',
                field: 'status',
                cellFilter: 'advertisementStatusFilter',
                minWidth: 80
            },
            {
                name: '轮播时长（秒）',
                field: 'timeInterval',
                minWidth: 80
            },
            {
                name: '图片比例',
                field: 'picFormat',
                cellFilter: 'picFormatFilter',
                minWidth: 80
            },
            {
                name: '图片',
                // field: 'headId',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.imageUrl}}" alt=""></div>',
                cellFilter: 'emptyFilter',
                minWidth: 60
            },

            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getAdvertisementDetails(row.entity)">编辑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            /*gridApi.selection.on.rowSelectionChanged($scope, function (row) {
             });*/
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getAdvertisments(newPage, pageSize);
            });
        }
    };

    getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);

    function getAdvertisments(page, size) {
        $scope.isLoading = true;
        SimpleServ.getObjectInPages('cgpDaoAdvertisement', page, size, $scope.params).success(function (data) {
            if (200 == data.code) {
                $scope.advertisementGridOpts.totalItems = data.totalSize;
                $scope.advertisementGridOpts.data = data.list;
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

    function getAdvertisementDetails(advertisement) {
        $scope.advertisement = angular.copy(advertisement);
        showBarSelect();
        $('#advertisement-detail').modal('show');
    }

    function searchAdvertisementsData() {
        if (!$scope.searchParams.val && !$scope.searchParams.status) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
            status: $scope.searchParams.status
        };
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        $scope.params = JSON.stringify(objParams);
        getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);

    }

    function resetAdvertisementsData() {
        $scope.params = '';
        $scope.searchParams = {
            key: 'title',
            val: '',
        };
        getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);
    }

    function chgAdvertisementsStatus(status) {
        $scope.advertisementIds = _.pluck($scope.gridApi.selection.getSelectedRows(), 'id');

        if ($scope.advertisementIds.length) {
            SimpleServ.setObjectStatusByIds('cgpDaoAdvertisement', $scope.advertisementIds, status).success(function (data) {
                if (data.code == 200) {
                    swal("操作成功");
                    $scope.advertisementIds = [];
                    getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);
                } else {
                    swal("操作失败，请稍后再试");
                }
            }).error(function (r) {
                console.log(r)
            });
        }
    }

    function saveAdvertisement() {
        var submitObj = angular.copy($scope.advertisement);
        var picFormatA = parseInt($("#picFormatA").val());
        var picFormatB = parseInt($("#picFormatB").val());
        if (!(1 <=  picFormatA && picFormatA <= 3 && 1 <= picFormatB && picFormatB <= 3)) {
            swal("图片尺寸请在1-3之间");
            return;
        }

        if (submitObj.id) {//编辑

            Upload.upload({
                url: data_host + '/ctrl/advertisement/update?' + $.param({
                    id: submitObj.id,
                    title: submitObj.title,
                    content: submitObj.content,
                    webUrl: submitObj.webUrl,
                    picFormat: picFormatA + "_" + picFormatB,
                    timeInterval: submitObj.timeInterval,
                    sortBy: submitObj.sortBy,
                    status: submitObj.status,
                    barId: submitObj.barId
                }),
                method: 'POST',
                dataType: "json",
                //transformRequest: angular.identity,
                file: $scope.imageFile,
            }).then(function (resp) {
                if (resp.data.code == 200) {
                    swal({
                        title: '修改成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#advertisement-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.label = {
                                name: '',
                                status: 1,
                                sortBy: 1,
                                timeInterval: 3,
                            };
                        });
                    });
                    getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);
                }
            }, function (resp) {
                swal('发布失败，请稍后再试');
            })['finally'](function () {
                $scope.uploading = false;
                $scope.imageFile = null;
            });

        } else {//新建

            Upload.upload({
                url: data_host + '/ctrl/advertisement/add?' + $.param({
                    id: submitObj.id,
                    title: submitObj.title,
                    content: submitObj.content,
                    webUrl: submitObj.webUrl,
                    picFormat: $("#picFormatA").val() + "_" + $("#picFormatB").val(),
                    timeInterval: submitObj.timeInterval,
                    sortBy: submitObj.sortBy,
                    status: submitObj.status,
                    barId: submitObj.barId
                }),
                method: 'POST',
                dataType: "json",
                //transformRequest: angular.identity,
                file: $scope.imageFile,
            }).then(function (resp) {
                if (resp.data.code == 200) {
                    swal({
                        title: '新建成功',
                        type: 'success',
                        comfirmButtonText: '确定'
                    }, function () {
                        $('#advertisement-detail').modal('hide');
                        $scope.$apply(function () {
                            $scope.advertisement = {
                                title: '',
                                status: 1,
                                sortBy: 1,
                                timeInterval: 3,
                            };
                        });
                    });
                    getAdvertisments(1, $scope.advertisementGridOpts.paginationPageSize);
                }
            }, function (resp) {
                swal('发布失败，请稍后再试');
            })['finally'](function () {
                $scope.uploading = false;
                $scope.imageFile = null;
            });

        }
    }


    $scope.bars = [];
    function showBarSelect() {
        SimpleServ.getObjects("cgpDaoBar", "{'status':'1'}").success(function (data2) {
            if (data2.code == 200) {
                $scope.bars = data2.list;
            } else {
                swal("操作失败，请稍后再试");
            }
        }).error(function (r) {
            console.log(r)
        });
    }


    $('#advertisement-detail').on('hidden.bs.modal', function () {
        $scope.$apply(function () {
            $scope.advertisement = {
                name: '',
                status: 1,
                sortBy: 1,
                timeInterval: 3,
            };
        });
    });


    //预览广告效果
    $scope.previewAdvertisement = function () {
        SimpleServ.getObjects('cgpDaoAdvertisement', "{'status':'1','orderBySort':'asc'}").success(function (data) {
            if (data.code == 200) {
                $("#advertisementView").html("")
                var widthCount=0;
                $.each(data.list, function (index, value) {
                    var picFormat = value.picFormat;
                    var imageUrl = value.imageUrl;
                    var AdvHeight = parseInt(picFormat.substring(0, picFormat.lastIndexOf("_")));
                    var AdvWidth = parseInt(picFormat.substring(picFormat.lastIndexOf("_") + 1, picFormat.length));
                    widthCount+=AdvWidth;
                    $("#advertisementView").append("<div style='float: left' id='advertisementView-"+index+"'></div>");
                    if (AdvHeight==2&&AdvWidth==2&&widthCount%3==1){
                        $("#advertisementView-"+index).css("float","right");
                    }
                    $("#advertisementView-"+index).append("<img src='"+imageUrl+"' style='width: "+(100*AdvWidth)+"px;height: "+(100*AdvHeight)+"px' />");
                });
            } else {
                swal("操作失败，请稍后再试");
            }
        });
    }

}
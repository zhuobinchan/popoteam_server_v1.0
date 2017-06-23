/**
 * Created by v1sionary on 2016/6/8.
 */
define(['app', 'service/userServ', 'service/commonServ'], function (app) {

    app.controller('ComplainCtrl', ComplainCtrl);

});

ComplainCtrl.$inject = ['$scope', 'UserServ', 'CommonServ'];

function ComplainCtrl($scope, UserServ, CommonServ) {
    var moment = require('moment');

    var params = ''; //筛选关键字
    $scope.searchParams = {
        key: 'nickName',
        val: '',
        releaseTimeBegin:'',
        releaseTimeEnd:'',
        complainTimesBegin: '',
        complainTimesEnd: '',
        provinceId:'',
        cityId:'',
        sex:''
    };
    $scope.currentPhotoOwner = {};
    $scope.complainDetail = {
    };
    $scope.isLoading = false;
    $scope.gettingDetails = false;
    $scope.sendingWarn = false;
    $scope.searchComplains = searchComplains; //筛选
    $scope.resetComplains = resetComplains; //重置筛选
    $scope.getComplainDetails = getComplainDetails; //
    $scope.freezeUser = freezeUser; //冻结用户
    $scope.deletePhoto = deletePhoto; //删除照片
    $scope.gaveWarn = gaveWarn; //发送警告
    /*
     * 省市区列表
     */
    $scope.provinceList = [];
    $scope.cityList = [];
    $scope.areaList = [];
    $scope.getDistrict = getDistrict;//获取省市区
    /*  一开始先拿到所有的省的列表 */

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
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.photoUser.head.url}}" alt=""></div>',
                width: 60
            },
            {
                name: '发布者昵称',
                field: 'photoUser.nickName',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '性别',
                field: 'photoUser.sex',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '年龄',
                field: 'photoUser.age',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '发布者ID',
                field: 'photoUser.identify',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '省',
                field: 'photoUser.province.name',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '市',
                field: 'photoUser.city.name',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '发布日期',
                field: 'photoUser.city.name',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '被举报日期',
                field: 'photoUser.city.name',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '举报次数',
                field: 'complainTimes',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '举报人',
                field: 'user.identify',
                cellTemplate: '<div style="max-height:100%;max-width:100%;text-align:center;padding:2px;">' +
                '<img style="max-height:100%;max-width:40px;" src="/res/img/default-photo.png" ng-src="{{row.entity.user.head.url}}" alt=""></div>',
                minWidth: 80
            },
            {
                name: '举报人ID',
                field: 'user.identify',
                cellFilter: 'emptyFilter',
                minWidth: 80
            },
            {
                name: '操作',
                cellTemplate: '<button type="button" class="btn btn-default btn-console" ' +
                'ng-click="grid.appScope.getComplainDetails(row.entity)">编辑</button>',
                minWidth: 90
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                getComplains(newPage, pageSize);
            });
        }
    };


    //数据初始化
    getDistrict(1);
    getComplains(1, $scope.gridOptions.paginationPageSize);

    function getComplains(page, size) {
        $scope.isLoading = true;
        UserServ.getComplainsInPages(page, size, params).success(function (data) {
            if (200 == data.code) {
                $scope.gridOptions.totalItems = data.totalSize;
                _(data.list).each(function (item) {
                   item.photoUser.age = moment().diff(item.photoUser.birthday,'years');
                });
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

    function searchComplains() {
        if (!$scope.searchParams.val && !$scope.searchParams.releaseTimeBegin && !$scope.searchParams.releaseTimeEnd &&
                !$scope.searchParams.complainTimesBegin && !$scope.searchParams.complainTimesEnd && !$scope.searchParams.sex &&
                !$scope.searchParams.provinceId && !$scope.searchParams.cityId) {
            swal(GCONSTANTS.MSG.NO_SEARCH_KEY_DATE);
            return;
        }
        var objParams = {
            releaseTimeBegin: $scope.searchParams.sendTimeBegin ?
                    moment($scope.searchParams.sendTimeBegin).format('YYYY-MM-DD hh:mm:ss') : '',
            releaseTimeEnd: $scope.searchParams.sendTimeEnd ?
                    moment($scope.searchParams.sendTimeEnd).format('YYYY-MM-DD hh:mm:ss') : '',
            complainTimesBegin:$scope.searchParams.sendTimeEnd,
            complainTimesEnd:$scope.searchParams.complainTimesEnd,
            provinceId:$scope.searchParams.provinceId,
            cityId:$scope.searchParams.cityId
        };
        if($scope.searchParams.sex){
            objParams['sex'] = $scope.searchParams.sex;
        }
        objParams[$scope.searchParams.key] = $scope.searchParams.val;
        params = '&' + $.param(objParams);
        getComplains(1, $scope.gridOptions.paginationPageSize);
    }

    function resetComplains() {
        params = '';
        $scope.searchParams = {
            key: 'nickName',
            val: '',
            releaseTimeBegin:'',
            releaseTimeEnd:'',
            complainTimesBegin: '',
            complainTimesEnd: '',
            provinceId:'',
            cityId:'',
            sex:''
        };
        getComplains(1, $scope.gridOptions.paginationPageSize);
    }

    function getComplainDetails(complain){
        $scope.gettingDetails = true;
        $scope.currentPhotoOwner = complain.photoUser;
        UserServ.getComplainById(complain.photoId).success(function (data) {
            if(200 == data.code){
                $scope.complainDetail = {
                    photo: data.photo,
                    complainList: data.complainList
                };
                angular.forEach($scope.complainDetail.complainList, function (item) {
                    item.fromNow = moment(item.createTime).fromNow();
                });
            }else{
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function(err){
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function () {
            $scope.gettingDetails = false;
        });
        $('#complain-detail').modal('show');
    }

    $('#complain-detail').on('hidden.bs.modal',function(){
        $scope.$apply(function () {
            $scope.complainDetail = {};
            $scope.currentPhotoOwner = {};
        });
    });


    function getDistrict(type, code) {
        CommonServ.getDistricts(type, code).success(function (data) {
            if (type == 1) {
                $scope.provinceList = data.list;
            } else if (type == 2) {
                $scope.cityList = data.list;
            }
        }).error(function (r) {
            console.log(r);
        });
    }

    function freezeUser(userId){
        UserServ.freeze([userId], true).success(function (data) {
            if (data.code == 200) {
                swal("成功冻结。");
                $scope.currentPhotoOwner.userBase.freeze = 1;
            } else {
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function (r) {
            console.log(r);
            swal(GCONSTANTS.MSG.ERR500);
        });
    }

    function deletePhoto(photoId){
        UserServ.deletePhoto(photoId).success(function (data) {
            if(200 == data.code){
                swal({
                    title:'成功删除',
                    type:'success',
                    confirmButtonText: '确定'
                }, function () {
                    $('#complain-detail').modal('hide');
                    $scope.$apply(function(){
                        $scope.complainDetail = {};
                        $scope.currentPhotoOwner = {};
                    });
                });
                getComplains(1, $scope.gridOptions.paginationPageSize);
            }else{
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function(err){
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        });
    }

    function gaveWarn(photoId,userId){
        $scope.sendingWarn = true;
        UserServ.warnUser(photoId,userId).success(function (data) {
            if(200 == data.code){
                swal('警告已发送。');
            }else{
                swal(data.message + GCONSTANTS.MSG.SUFFIX_GET_DATA_NOT200);
            }
        }).error(function(err){
            console.log(err);
            swal(GCONSTANTS.MSG.ERR500);
        }).finally(function () {
            $scope.sendingWarn = false;
        });
    }

}
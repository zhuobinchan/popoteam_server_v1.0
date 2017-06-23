/**
 * Created by v1sionary on 2016/6/7.
 */
define(['angularAMD', 'service/commonServ'], function (angularAMD) {

    angularAMD.directive('custLocation', custLocation);

});

var custLocation = ['CommonServ', function (CommonServ){
    return {
        restrict: 'AC',
        replace: true,
        templateUrl: '/src/tpl/location.selector.tpl.html',
        scope: {
            ngModel: '='
        },
        link: function (scope, eles, attrs) {
            scope.provinceList = [];
            scope.cityList = [];
            scope.areaList = [];
            scope.province = {};
            scope.citye = {};
            scope.area = {};
            scope.detailAddress = '';
            scope.changeProvince = changeProvince;
            scope.changeCity = changeCity;
            scope.changeArea = changeArea;

            getProvinces();


            function changeProvince(){
                scope.cityList = [];
                scope.areaList = [];
                CommonServ.getDistricts(2, scope.province.code).success(function (data) {
                    if(200 == data.code) {
                        scope.cityList = data.list;
                        scope.ngModel = {
                            "province" : scope.province == null || scope.province == '' ? '' : scope.province,
                            "city" : scope.city || '',
                            "area" : scope.area || ''
                        };
                    }
                });
            }


            function changeCity(){
                scope.areaList = [];
                CommonServ.getDistricts(3, scope.city.code).success(function (data) {
                    if(200 == data.code) {
                        scope.areaList = data.list;
                        scope.ngModel = {
                            "province" : scope.province,
                            "city" : scope.city == null || scope.city == '' ? '' : scope.city,
                            "area" : scope.area || ''

                        };
                    }
                });
            }

            function changeArea(){
                scope.ngModel = {
                    "province" : scope.province,
                    "city" : scope.city,
                    "area" : scope.area == null || scope.area == '' ? '' : scope.area
                };
            }

            function getProvinces(){
                scope.province = [];
                CommonServ.getDistricts(1).success(function (data) {
                    if(200 == data.code) {
                        scope.provinceList = data.list;
                    }
                });
            }
        }
    }
}];
/**
 * Created by v1sionary on 2016/6/7.
 */
define(['angularAMD', 'service/commonServ'], function (angularAMD) {

    angularAMD.directive('multiCitiesDire', multiCitiesDire);

});

var multiCitiesDire = ['CommonServ', function (CommonServ){
    return {
        restrict: 'AC',
        replace: true,
        templateUrl: '/src/tpl/multi.cities.selector.tpl.html',
        scope: {
            selectedDistricts: '='
        },
        link: function (scope, eles, attrs) {
            scope.currentProvince = {};//当前选择省份
            scope.provinceList = [];//省份列表
            scope.currentCities = [];//当前省份下的城市列表
            scope.selectedDistricts = [];//已选地区列表
            scope.isSelectedAll = true;//是否选择全部区域

            scope.getCities = getCities;
            scope.selectAllDistricts = selectAllDistricts;//选择全部区域
            scope.selectCities = selectCities;
            scope.deleteProvince = deleteProvince;
            scope.deleteCity = deleteCity;
            scope.selectAll = selectAll;
            scope.checkIsSelectedAll = checkIsSelectedAll;
            scope.clearSelected = clearSelected;//删除选择地区
            scope.doNotClear = doNotClear;//删除选择地区

            getProvinces();

            function getCities(province) {
                if (province.isActive) return;

                //设置高亮样式
                scope.isSelectedAll = false;
                angular.forEach(scope.provinceList, function (p) {
                    p.isActive = false;
                });
                province.isActive = true;

                scope.currentProvince = province;//记录当前选择省份

                CommonServ.getDistricts(2, province.code).success(function (data) {
                    if(200 == data.code) {
                        var citiesList = data.list;

                        checkExitedInSelected(citiesList, province.code);
                        scope.currentCities = citiesList;
                    }
                });

            }

            function selectAllDistricts() {
                if (scope.isSelectedAll) return;
                scope.isSelectedAll = true;
                angular.forEach(scope.provinceList, function (p) {
                    p.isActive = false;
                });
                scope.currentCities = [];
                if(scope.selectedDistricts.length > 0){
                    scope.toMakeSure = true;
                }
            }

            function clearSelected() {
                scope.selectedDistricts = [];
                scope.toMakeSure = false;
            }

            function doNotClear() {
                scope.toMakeSure = false;
            }

            function selectCities() {
                var isSelectedALLcities = scope.currentProvince.isSelectedAll;
                var cities = _(scope.currentCities).filter(function (c) {
                    return c.isSelected == true;
                });

                if (!cities || cities.length == 0) {
                    return;
                }

                var exitedItem = _(scope.selectedDistricts).find(function (d) {
                    return d.province.code === scope.currentProvince.code;
                });
                var _copyProvince = angular.copy(scope.currentProvince,{}),
                        _copyCities = angular.copy(cities,[]);
                if (exitedItem) {
                    exitedItem.province = _copyProvince;
                    if(isSelectedALLcities){
                        delete exitedItem.cities;
                    }else {
                        exitedItem.cities = _copyCities
                    }
                } else {
                    if(isSelectedALLcities){
                        scope.selectedDistricts.push({
                            province: _copyProvince
                        });
                    }else{
                        scope.selectedDistricts.push({
                            province: _copyProvince,
                            cities: _copyCities
                        });
                    }
                }
                console.log(scope.selectedDistricts);
            }

            function deleteProvince(index) {
                scope.selectedDistricts.splice(index, 1);
                checkExitedInSelected(scope.currentCities, scope.currentProvince.code);
            }

            function deleteCity(provinceIndex, cityIndex) {
                var list = scope.selectedDistricts[provinceIndex].cities;
                list.splice(cityIndex, 1);
                if (list.length == 0) {
                    deleteProvince(provinceIndex);
                }
                checkExitedInSelected(scope.currentCities, scope.currentProvince.code);
            }

            function selectAll() {
                angular.forEach(scope.currentCities,function (c) {
                    c.isSelected = scope.currentProvince.isSelectedAll;
                })
            }

            function checkIsSelectedAll() {
                var notSelectedItem = _(scope.currentCities).find(function (c) {
                    return c.isSelected == false;
                });
                if(notSelectedItem){
                    scope.currentProvince.isSelectedAll = false;
                }else{
                    if(!scope.currentProvince.isSelectedAll){
                        scope.currentProvince.isSelectedAll = true;
                    }
                }
            }

            /**
             * 检查城市是否被选择
             *
             * @param listToCheck 检查的城市数组
             * @param provinceCode 所在省份的code
             */
            function checkExitedInSelected(listToCheck, provinceCode) {
                _(listToCheck).each(function (c) {
                    c.isSelected = false;
                });
                scope.currentProvince.isSelectedAll = false;
                var _itemsInSelected = {}, _citiesInSelected = [];
                if (scope.selectedDistricts.length > 0) {
                    _itemsInSelected = _(scope.selectedDistricts).find(function (d) {
                        return d.province.code === provinceCode;
                    });
                    if (_itemsInSelected && _itemsInSelected.province) {
                        scope.currentProvince = _itemsInSelected.province;
                        if(_itemsInSelected.province.isSelectedAll){
                            _(listToCheck).each(function (c) {
                                c.isSelected = true;
                            });
                        }else{
                            _citiesInSelected = _(_itemsInSelected.cities).pluck('code');
                            _(listToCheck).each(function (c) {
                                c.isSelected = _citiesInSelected.indexOf(c.code) != -1;
                            });
                        }
                    }
                }
            }

            function getProvinces(){
                scope.provinceList = [];
                CommonServ.getDistricts(1).success(function (data) {
                    if(200 == data.code) {
                        scope.provinceList = data.list;
                    }
                });
            }
        }
    }
}];
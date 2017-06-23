/**
 * Created by sherry on 2016/6/12.
 */
require(['angularAMD'], function (angularAMD) {
    /**
     * loading提示指令
     * */
    angularAMD.directive('gLoadingTip', ['$timeout', function ($timeout) {
        return {
            restrict: 'A',
            replace: true,
            scope: {
                gIsLoading: '=gIsLoading',
                gIsSuccessed: '=gIsSuccessed'
            },
            templateUrl: '/src/tpl/data-loading.tpl.html',
            link: function (scope, tElement, tAttrs) {
                scope.gLoadingTips = tAttrs.gLoadingTips || '正在获取...';
                scope.gSuccessedTips = tAttrs.gSuccessedTips || '获取成功';
                scope.$watchCollection('[gIsLoading,gIsSuccessed]', function (newVal) {

                    scope.gIsLoading = newVal[0];
                    scope.gIsSuccessed = newVal[1];
                    if (scope.gIsSuccessed) {
                        $timeout(function () {
                            scope.gIsSuccessed = false;
                        }, 1000);
                    }
                });
            }
        }
    }]);

    angularAMD.directive('formList', function() {
        return {
            restrict : 'EA',
            replace : true,
            scope : {
                list : '@list',
                key: '@key'
            },
            template : '<span ng-bind="formedList"></span>',
            link : function(scope) {
                scope.$watchCollection('list', function (newVal) {
                    if(newVal){
                        var list = JSON.parse(newVal);
                    }
                    scope.formedList = _.pluck(list, scope.key).toString();
                });
            }
        }
    });

    /**
     * asyncImg提示指令
     * */
    angularAMD.directive('asyncImg', ['$http', 'data_host', '$filter', function ($http, data_host, $filter) {
        return {
            restrict: 'A',
            scope: {
                asyncId: '@',
                asyncSize: '@',
                isHasName: '@'
            },
            link: function (scope, eles, attr) {
                var defaultSrc = attr.defaultSrc;
                var style = '';
                scope.$watch('asyncId', function (newVal) {
                    if (!newVal) {//没有传进图片Id(初始化时undefined)时设置默认图片
                        attr.$set('src', defaultSrc);
                        attr.$set('oldAsyncId', 'none');
                    } else if (newVal == '' || newVal == attr.oldAsyncId) {//为了避免图片二次刷新
                        return;
                    } else {
                        if (scope.asyncSize) {
                            style = '&style=' + scope.asyncSize;//设置URL图片压缩参数,格式为‘40p’
                        }
                        $http.get(data_host + '/ctrl/common/getPictures?fileIds=' + newVal + style).success(function (data) {
                            var pic = data.list, mSrc;
                            if (data.code == 200 && pic && pic.length > 0) {
                                mSrc = pic[0].url;
                            } else {
                                mSrc = defaultSrc;
                            }
                            if(attr.isHasName){
                                eles[0].children[0].innerHTML = '<img style="max-height: 100%;max-width:100%;" src="'+ mSrc +'" alt="效果图"/>';
                                eles[0].children[1].innerHTML = $filter('fileNameSubFilter')(pic[0].name);
                            }else{
                                attr.$set('src', mSrc);
                            }
                            attr.$set('oldAsyncId', newVal);
                        }).error(function (r) {
                            console.log('error: get async img fail');
                            attr.$set('src', defaultSrc);
                        });
                    }
                });
            }
        }
    }]);

    angularAMD.directive('pie', function() {
        return {
            scope: {
                id: "@",
                title: "=",
                legend: "=",
                item: "=",
                data: "="
            },
            restrict: 'AE',
            template: '<div style="height:200px;"></div>',
            replace: true,
            link: function($scope, element, attrs, controller) {
                var myChart = echarts.init(document.getElementById($scope.id),'macarons');

                var option = {
                    title: {
                        text: $scope.title,
                        x: 'center',
                        y: 'top'
                    },
                    // 提示框，鼠标悬浮交互时的信息提示
                    tooltip: {
                        show: true,
                        trigger: 'item'
                    },
                    // 数据内容数组
                    series: function(){
                        var serie=[];
                        var item = {
                            name:'访问来源',
                            type:'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data: $scope.data
                        };
                        serie.push(item);
                        return serie;
                    }()
                };
                // 图例
                if($scope.legend){
                    option['legend'] = {
                        orient: 'vertical', // 'vertical'
                        x: 'right', // 'center' | 'left' | {number},
                        y: 'center', // 'center' | 'bottom' | {number}
                        data: $scope.legend
                    }
                }

                myChart.setOption(option);

                // 监控数据是否变化
                $scope.$watch('data', function (newVal) {
                    var option = myChart.getOption();
                    option.series = option.series || [];
                    newVal = newVal || {};
                    var item = {
                        name:'访问来源',
                        type:'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data: newVal
                    };
                    option.series = [];
                    option.series.push(item);
                    echarts.init(document.getElementById($scope.id),'macarons').setOption(option);
                }, true);
            }
        };
    });
    angularAMD.directive('bar', function() {
        return {
            scope: {
                id: "@",
                legend: "=",
                item: "=",
                data: "="
            },
            restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
            template: '<div style="height:400px"></div>',
            replace: true,
            link: function($scope, iElm, iAttrs, controller) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = echarts.init(document.getElementById($scope.id),'macarons');

                var option = {
                    tooltip: {
                        show: true,
                        trigger: "axis"
                    },
                    legend: {data:$scope.legend},
                    xAxis: [{
                        type: 'category',
                        data: $scope.item
                    }],
                    yAxis: [{
                        type: 'value'
                    }],
                    series: function() {
                        var serie = [];
                        for (var i = 0; i < $scope.legend.length; i++) {

                            var item = {
                                name: $scope.legend[i],
                                type: 'bar',
                                data: $scope.data[i]
                            };
                            serie.push(item);
                        }
                        return serie;
                    }()
                };

                // 为echarts对象加载数据
                myChart.setOption(option);

                // 监控数据是否变化
                $scope.$watch('item + data', function () {
                    var option = {
                        tooltip: {
                            show: true,
                            trigger: "axis"
                        },
                        legend: {data:$scope.legend},
                        xAxis: [{
                            type: 'category',
                            data: $scope.item
                        }],
                        yAxis: [{
                            type: 'value'
                        }],
                        series: function() {
                            var serie = [];
                            for (var i = 0; i < $scope.legend.length; i++) {

                                var item = {
                                    name: $scope.legend[i],
                                    type: 'bar',
                                    data: $scope.data[i]
                                };
                                serie.push(item);
                            }
                            return serie;
                        }()
                    };
                    echarts.init(document.getElementById($scope.id),'macarons').setOption(option);
                });
            }
        };
    });
});
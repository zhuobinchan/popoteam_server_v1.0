/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app', 'service/userServ', 'service/groupServ', 'service/activityServ'], function (app) {
    app.controller('StatisDataCtrl', StatisDataCtrl);
    // 用户、队伍、约会线性图
    app
    //    .directive('multiline', function() {
    //    return {
    //        scope: {
    //            id: "@",
    //            legend: "=",
    //            item: "=",
    //            data: "="
    //
    //        },
    //        restrict: 'E',
    //        template: '<div style="height:300px;"></div>',
    //        replace: true,
    //        link: function($scope, element, attrs, controller) {
    //            var option = {
    //                // 提示框，鼠标悬浮交互时的信息提示
    //                tooltip: {
    //                    show: true,
    //                    trigger: 'item'
    //                },
    //                // 图例
    //                legend: {
    //                    data: $scope.legend
    //                },
    //                // 横轴坐标轴
    //                xAxis: [{
    //                    type: 'category',
    //                    data: $scope.item
    //                }],
    //                // 纵轴坐标轴
    //                yAxis: [{
    //                    type: 'value'
    //                }],
    //                // 数据内容数组
    //                series: function(){
    //                    var serie=[];
    //                    for(var i=0;i<$scope.legend.length;i++){
    //                        var item = {
    //                            name : $scope.legend[i],
    //                            type: 'line',
    //                            data: $scope.data[i]
    //                        };
    //                        serie.push(item);
    //                    }
    //                    return serie;
    //                }()
    //            };
    //            var myChart = echarts.init(document.getElementById($scope.id),'macarons');
    //            myChart.setOption(option);
    //        }
    //    };
    //})
    //    .directive('usergroup', function() {
    //        return {
    //            scope: {
    //                id: "@",
    //                legend: "=",
    //                item: "=",
    //                data: "="
    //            },
    //            color:'red',
    //            restrict: 'E',
    //            template: '<div style="height:500px;"><select name=""> <option value="0">DIVCSS5</option> <option value="1">DIVCSS5</option> </select> </div>',
    //            replace: true,
    //            link: function($scope, element, attrs, controller) {
    //                //var option = {
    //                //    // 提示框，鼠标悬浮交互时的信息提示
    //                //    tooltip: {
    //                //        show: true,
    //                //        trigger: 'item'
    //                //    },
    //                //    // 图例
    //                //    legend: {
    //                //        data: $scope.legend
    //                //    },
    //                //    // 横轴坐标轴
    //                //    xAxis: [{
    //                //        type: 'category',
    //                //        data: $scope.item
    //                //    }],
    //                //    // 纵轴坐标轴
    //                //    yAxis: [{
    //                //        type: 'value'
    //                //    }],
    //                //    // 数据内容数组
    //                //    series: function(){
    //                //        var serie=[];
    //                //        for(var i=0;i<$scope.legend.length;i++){
    //                //            var item = {
    //                //                name : $scope.legend[i],
    //                //                type: 'line',
    //                //                data: $scope.data[i]
    //                //            };
    //                //            serie.push(item);
    //                //        }
    //                //        return serie;
    //                //    }()
    //                //};
    //                option = {
    //                    tooltip : {
    //                        trigger: 'axis'
    //                    },
    //                    toolbox: {
    //                        show : true,
    //                        y: 'bottom',
    //                        feature : {
    //                            mark : {show: true},
    //                            dataView : {show: true, readOnly: false},
    //                            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
    //                            restore : {show: true},
    //                            saveAsImage : {show: true}
    //                        }
    //                    },
    //                    calculable : true,
    //                    legend: {
    //                        data:['18岁以下','18-29岁','30-45岁','45岁以上','队伍总量','同地区同类型','同地区不同类型','同市同类型','同市不同类型']
    //                    },
    //                    xAxis : [
    //                        {
    //                            type : 'category',
    //                            splitLine : {show : false},
    //                            data : ['周一','周二','周三','周四','周五','周六','周日']
    //                        }
    //                    ],
    //                    yAxis : [
    //                        {
    //                            type : 'value',
    //                            position: 'right'
    //                        }
    //                    ],
    //                    series : [
    //                        {
    //                            name:'18岁以下',
    //                            type:'bar',
    //                            tooltip : {trigger: 'item'},
    //                            stack: '广告',
    //                            data:[120, 132, 101, 134, 90, 230, 210]
    //                        },
    //                        {
    //                            name:'18-29岁',
    //                            type:'bar',
    //                            tooltip : {trigger: 'item'},
    //                            stack: '广告',
    //                            data:[220, 182, 191, 234, 290, 330, 310]
    //                        },
    //                        {
    //                            name:'30-45岁',
    //                            type:'bar',
    //                            tooltip : {trigger: 'item'},
    //                            stack: '广告',
    //                            data:[150, 232, 201, 154, 190, 330, 410]
    //                        },
    //                        {
    //                            name:'队伍总量',
    //                            type:'line',
    //                            data:[862, 1018, 964, 1026, 1679, 1600, 1570]
    //                        },
    //
    //                        {
    //                            name:'队伍细分',
    //                            type:'pie',
    //                            tooltip : {
    //                                trigger: 'item',
    //                                formatter: '{a} <br/>{b} : {c} ({d}%)'
    //                            },
    //                            center: [160,130],
    //                            radius : [0, 50],
    //                            itemStyle :　{
    //                                normal : {
    //                                    labelLine : {
    //                                        length : 20
    //                                    }
    //                                }
    //                            },
    //                            data:[
    //                                {value:1048, name:'同地区同类型'},
    //                                {value:251, name:'同地区不同类型'},
    //                                {value:147, name:'同市同类型'},
    //                                {value:102, name:'同市不同类型'}
    //                            ]
    //                        }
    //                    ]
    //                };
    //
    //                var myChart = echarts.init(document.getElementById($scope.id),'macarons');
    //                myChart.setOption(option);
    //            }
    //        };
    //    })
    //    .directive('userbar', function() {
    //        return {
    //            scope: {
    //                id: "@",
    //                legend: "=",
    //                item: "=",
    //                data: "="
    //            },
    //            color:'red',
    //            restrict: 'E',
    //            template: '<div style="height:250px;"></div>',
    //            replace: true,
    //            link: function($scope, element, attrs, controller) {
    //                //var option = {
    //                //    // 提示框，鼠标悬浮交互时的信息提示
    //                //    tooltip: {
    //                //        show: true,
    //                //        trigger: 'item'
    //                //    },
    //                //    // 图例
    //                //    legend: {
    //                //        data: $scope.legend
    //                //    },
    //                //    // 横轴坐标轴
    //                //    xAxis: [{
    //                //        type: 'category',
    //                //        data: $scope.item
    //                //    }],
    //                //    // 纵轴坐标轴
    //                //    yAxis: [{
    //                //        type: 'value'
    //                //    }],
    //                //    // 数据内容数组
    //                //    series: function(){
    //                //        var serie=[];
    //                //        for(var i=0;i<$scope.legend.length;i++){
    //                //            var item = {
    //                //                name : $scope.legend[i],
    //                //                type: 'line',
    //                //                data: $scope.data[i]
    //                //            };
    //                //            serie.push(item);
    //                //        }
    //                //        return serie;
    //                //    }()
    //                //};
    //                option = {
    //                    title : {
    //                        text: '用户量分析',
    //                        x:'left'
    //                    },
    //                    tooltip : {
    //                        trigger: 'axis'
    //                    },
    //                    legend: {
    //                        data:['用户量'],
    //                        x:'center',
    //                        y:'bottom'
    //                    },
    //                    //toolbox: {
    //                    //    show : true,
    //                    //    feature : {
    //                    //        //mark : {show: true},
    //                    //        //dataView : {show: true, readOnly: false},
    //                    //        //magicType : {show: true, type: ['line', 'bar']},
    //                    //        //restore : {show: true},
    //                    //        //saveAsImage : {show: true}
    //                    //    }
    //                    //},
    //                    calculable : true,
    //                    xAxis : [
    //                        {
    //                            type : 'category',
    //                            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
    //                        }
    //                    ],
    //                    yAxis : [
    //                        {
    //                            type : 'value'
    //                        }
    //                    ],
    //                    series : [
    //                        {
    //                            name:'用户量',
    //                            type:'bar',
    //                            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
    //                            markPoint : {
    //                                data : [
    //                                    {type : 'max', name: '最大值'},
    //                                    {type : 'min', name: '最小值'}
    //                                ]
    //                            },
    //                            markLine : {
    //                                data : [
    //                                    {type : 'average', name: '平均值'}
    //                                ]
    //                            }
    //                        }
    //                    ]
    //                };
    //
    //                var myChart = echarts.init(document.getElementById($scope.id),'macarons');
    //                myChart.setOption(option);
    //            }
    //        };
    //    })
        .directive('userpie', function() {

            return {
                scope: {
                    id: "@",
                    legend: "=",
                    item: "=",
                    data: "="
                },
                restrict: 'E',
                template: '<div style="height:200px;"></div>',
                replace: true,
                link: function($scope, element, attrs, controller) {
                    $scope.userData = [
                        {value:335, name:'18岁以下'},
                        {value:310, name:'18-29岁'},
                        {value:234, name:'30-45岁'},
                        {value:135, name:'45岁以上'}
                    ];
                    //var option = {
                    //    // 提示框，鼠标悬浮交互时的信息提示
                    //    tooltip: {
                    //        show: true,
                    //        trigger: 'item'
                    //    },
                    //    // 图例
                    //    legend: {
                    //        data: $scope.legend
                    //    },
                    //    // 横轴坐标轴
                    //    xAxis: [{
                    //        type: 'category',
                    //        data: $scope.item
                    //    }],
                    //    // 纵轴坐标轴
                    //    yAxis: [{
                    //        type: 'value'
                    //    }],
                    //    // 数据内容数组
                    //    series: function(){
                    //        var serie=[];
                    //        for(var i=0;i<$scope.legend.length;i++){
                    //            var item = {
                    //                name : $scope.legend[i],
                    //                type: 'line',
                    //                data: $scope.data[i]
                    //            };
                    //            serie.push(item);
                    //        }
                    //        return serie;
                    //    }()
                    //};
                    option = {
                        title : {
                            text: '用户量统计',
                            x:'right'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        //legend: {
                        //    x:'left',
                        //    y:'center',
                        //    orient : 'vertical',
                        //    data:['18岁以下','18-29岁','30-45岁','45岁以上']
                        //},
                        //toolbox: {
                        //    show : true,
                        //    feature : {
                        //        restore : {show: true},
                        //        saveAsImage : {show: true}
                        //    }
                        //},
                        calculable : true,
                            series: function(){
                                var serie=[];

                                    var item = {
                                        type: 'pie',
                                        radius : '55%',
                                        center: ['50%', '60%'],
                                        data: $scope.userData
                                    };
                                    serie.push(item);

                                return serie;
                            }()
                    };

                    var myChart = echarts.init(document.getElementById($scope.id),'macarons');
                    myChart.setOption(option);
                }
            };
        })
        //.directive('groupbar', function() {
        //    return {
        //        scope: {
        //            id: "@",
        //            legend: "=",
        //            item: "=",
        //            data: "="
        //        },
        //        restrict: 'E',
        //        template: '<div style="height:200px;"></div>',
        //        replace: true,
        //        link: function($scope, element, attrs, controller) {
        //            //var option = {
        //            //    // 提示框，鼠标悬浮交互时的信息提示
        //            //    tooltip: {
        //            //        show: true,
        //            //        trigger: 'item'
        //            //    },
        //            //    // 图例
        //            //    legend: {
        //            //        data: $scope.legend
        //            //    },
        //            //    // 横轴坐标轴
        //            //    xAxis: [{
        //            //        type: 'category',
        //            //        data: $scope.item
        //            //    }],
        //            //    // 纵轴坐标轴
        //            //    yAxis: [{
        //            //        type: 'value'
        //            //    }],
        //            //    // 数据内容数组
        //            //    series: function(){
        //            //        var serie=[];
        //            //        for(var i=0;i<$scope.legend.length;i++){
        //            //            var item = {
        //            //                name : $scope.legend[i],
        //            //                type: 'line',
        //            //                data: $scope.data[i]
        //            //            };
        //            //            serie.push(item);
        //            //        }
        //            //        return serie;
        //            //    }()
        //            //};
        //            option = {
        //                title : {
        //                    text: '队伍分析',
        //                    x:'left'
        //                },
        //                tooltip : {
        //                    trigger: 'axis'
        //                },
        //                legend: {
        //                    data:['队伍量'],
        //                    x:'center',
        //                    y:'bottom'
        //                },
        //                //toolbox: {
        //                //    show : true,
        //                //    feature : {
        //                //        //mark : {show: true},
        //                //        //dataView : {show: true, readOnly: false},
        //                //        //magicType : {show: true, type: ['line', 'bar']},
        //                //        //restore : {show: true},
        //                //        saveAsImage : {show: true}
        //                //    }
        //                //},
        //                calculable : true,
        //                xAxis : [
        //                    {
        //                        type : 'category',
        //                        data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        //                    }
        //                ],
        //                yAxis : [
        //                    {
        //                        type : 'value'
        //                    }
        //                ],
        //                series : [
        //                    {
        //                        name:'队伍量',
        //                        type:'bar',
        //                        data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
        //                        markPoint : {
        //                            data : [
        //                                {type : 'max', name: '最大值'},
        //                                {type : 'min', name: '最小值'}
        //                            ]
        //                        },
        //                        markLine : {
        //                            data : [
        //                                {type : 'average', name: '平均值'}
        //                            ]
        //                        }
        //                    }
        //                ]
        //            };
        //
        //            var myChart = echarts.init(document.getElementById($scope.id),'macarons');
        //            myChart.setOption(option);
        //        }
        //    };
        //})
        .directive('grouppie', function() {
            return {
                scope: {
                    id: "@",
                    legend: "=",
                    item: "=",
                    data: "="
                },
                restrict: 'E',
                template: '<div style="height:200px;"></div>',
                replace: true,
                link: function($scope, element, attrs, controller) {
                    //var option = {
                    //    // 提示框，鼠标悬浮交互时的信息提示
                    //    tooltip: {
                    //        show: true,
                    //        trigger: 'item'
                    //    },
                    //    // 图例
                    //    legend: {
                    //        data: $scope.legend
                    //    },
                    //    // 横轴坐标轴
                    //    xAxis: [{
                    //        type: 'category',
                    //        data: $scope.item
                    //    }],
                    //    // 纵轴坐标轴
                    //    yAxis: [{
                    //        type: 'value'
                    //    }],
                    //    // 数据内容数组
                    //    series: function(){
                    //        var serie=[];
                    //        for(var i=0;i<$scope.legend.length;i++){
                    //            var item = {
                    //                name : $scope.legend[i],
                    //                type: 'line',
                    //                data: $scope.data[i]
                    //            };
                    //            serie.push(item);
                    //        }
                    //        return serie;
                    //    }()
                    //};
                    option = {
                        title : {
                            text: '队伍总量分析',
                            x:'right'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        //legend: {
                        //    orient : 'vertical',
                        //    x : 'left',
                        //    data:['一起出去唱K','今晚去蹦迪','出去喝一杯','我们出去吧']
                        //},
                        //toolbox: {
                        //    show : true,
                        //    feature : {
                        //        restore : {show: true},
                        //        saveAsImage : {show: true}
                        //    }
                        //},
                        calculable : true,
                        series : [
                            {
                                name:'访问来源',
                                type:'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                    {value:335, name:'一起出去唱K'},
                                    {value:310, name:'今晚去蹦迪'},
                                    {value:234, name:'出去喝一杯'},
                                    {value:135, name:'我们出去吧'}
                                ]
                            }
                        ]
                    };

                    var myChart = echarts.init(document.getElementById($scope.id),'macarons');
                    myChart.setOption(option);
                }
            };
        })
        .directive('activitypie', function() {
            return {
                scope: {
                    id: "@",
                    legend: "=",
                    item: "=",
                    data: "="
                },
                restrict: 'E',
                template: '<div style="height:200px;"></div>',
                replace: true,
                link: function($scope, element, attrs, controller) {
                    //var option = {
                    //    // 提示框，鼠标悬浮交互时的信息提示
                    //    tooltip: {
                    //        show: true,
                    //        trigger: 'item'
                    //    },
                    //    // 图例
                    //    legend: {
                    //        data: $scope.legend
                    //    },
                    //    // 横轴坐标轴
                    //    xAxis: [{
                    //        type: 'category',
                    //        data: $scope.item
                    //    }],
                    //    // 纵轴坐标轴
                    //    yAxis: [{
                    //        type: 'value'
                    //    }],
                    //    // 数据内容数组
                    //    series: function(){
                    //        var serie=[];
                    //        for(var i=0;i<$scope.legend.length;i++){
                    //            var item = {
                    //                name : $scope.legend[i],
                    //                type: 'line',
                    //                data: $scope.data[i]
                    //            };
                    //            serie.push(item);
                    //        }
                    //        return serie;
                    //    }()
                    //};
                    option = {
                        title : {
                            text: '约会总量分析',
                            x:'right'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        //legend: {
                        //    orient : 'vertical',
                        //    x : 'left',
                        //    data:['一起出去唱K','今晚去蹦迪','出去喝一杯','我们出去吧']
                        //},
                        //toolbox: {
                        //    show : true,
                        //    feature : {
                        //        restore : {show: true},
                        //        saveAsImage : {show: true}
                        //    }
                        //},
                        calculable : true,
                        series : [
                            {
                                name:'访问来源',
                                type:'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                    {value:335, name:'一起出去唱K'},
                                    {value:310, name:'今晚去蹦迪'},
                                    {value:234, name:'出去喝一杯'},
                                    {value:135, name:'我们出去吧'}
                                ]
                            }
                        ]
                    };

                    var myChart = echarts.init(document.getElementById($scope.id),'macarons');
                    myChart.setOption(option);
                }
            };
        })
        //.directive('activitybar', function() {
        //    return {
        //        scope: {
        //            id: "@",
        //            legend: "=",
        //            item: "=",
        //            data: "="
        //        },
        //        restrict: 'E',
        //        template: '<div style="height:200px;"></div>',
        //        replace: true,
        //        link: function($scope, element, attrs, controller) {
        //            //var option = {
        //            //    // 提示框，鼠标悬浮交互时的信息提示
        //            //    tooltip: {
        //            //        show: true,
        //            //        trigger: 'item'
        //            //    },
        //            //    // 图例
        //            //    legend: {
        //            //        data: $scope.legend
        //            //    },
        //            //    // 横轴坐标轴
        //            //    xAxis: [{
        //            //        type: 'category',
        //            //        data: $scope.item
        //            //    }],
        //            //    // 纵轴坐标轴
        //            //    yAxis: [{
        //            //        type: 'value'
        //            //    }],
        //            //    // 数据内容数组
        //            //    series: function(){
        //            //        var serie=[];
        //            //        for(var i=0;i<$scope.legend.length;i++){
        //            //            var item = {
        //            //                name : $scope.legend[i],
        //            //                type: 'line',
        //            //                data: $scope.data[i]
        //            //            };
        //            //            serie.push(item);
        //            //        }
        //            //        return serie;
        //            //    }()
        //            //};
        //            option = {
        //                title : {
        //                    text: '约会分析',
        //                    x:'left'
        //                },
        //                tooltip : {
        //                    trigger: 'axis'
        //                },
        //                legend: {
        //                    data:['约会量'],
        //                    x:'center',
        //                    y:'bottom'
        //                },
        //                //toolbox: {
        //                //    show : true,
        //                //    feature : {
        //                //        //mark : {show: true},
        //                //        //dataView : {show: true, readOnly: false},
        //                //        //magicType : {show: true, type: ['line', 'bar']},
        //                //        //restore : {show: true},
        //                //        saveAsImage : {show: true}
        //                //    }
        //                //},
        //                calculable : true,
        //                xAxis : [
        //                    {
        //                        type : 'category',
        //                        data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        //                    }
        //                ],
        //                yAxis : [
        //                    {
        //                        type : 'value'
        //                    }
        //                ],
        //                series : [
        //                    {
        //                        name:'约会量',
        //                        type:'bar',
        //                        data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
        //                        markPoint : {
        //                            data : [
        //                                {type : 'max', name: '最大值'},
        //                                {type : 'min', name: '最小值'}
        //                            ]
        //                        },
        //                        markLine : {
        //                            data : [
        //                                {type : 'average', name: '平均值'}
        //                            ]
        //                        }
        //                    }
        //                ]
        //            };
        //
        //            var myChart = echarts.init(document.getElementById($scope.id),'macarons');
        //            myChart.setOption(option);
        //        }
        //    };
        //})
        //.directive('activityradar', function() {
        //    return {
        //        scope: {
        //            id: "@",
        //            legend: "=",
        //            item: "=",
        //            data: "="
        //        },
        //        restrict: 'E',
        //        template: '<div style="height:200px;"></div>',
        //        replace: true,
        //        link: function($scope, element, attrs, controller) {
        //            //var option = {
        //            //    // 提示框，鼠标悬浮交互时的信息提示
        //            //    tooltip: {
        //            //        show: true,
        //            //        trigger: 'item'
        //            //    },
        //            //    // 图例
        //            //    legend: {
        //            //        data: $scope.legend
        //            //    },
        //            //    // 横轴坐标轴
        //            //    xAxis: [{
        //            //        type: 'category',
        //            //        data: $scope.item
        //            //    }],
        //            //    // 纵轴坐标轴
        //            //    yAxis: [{
        //            //        type: 'value'
        //            //    }],
        //            //    // 数据内容数组
        //            //    series: function(){
        //            //        var serie=[];
        //            //        for(var i=0;i<$scope.legend.length;i++){
        //            //            var item = {
        //            //                name : $scope.legend[i],
        //            //                type: 'line',
        //            //                data: $scope.data[i]
        //            //            };
        //            //            serie.push(item);
        //            //        }
        //            //        return serie;
        //            //    }()
        //            //};
        //
        //            option = {
        //                title : {
        //                    text: '约会类型分析',
        //                    x:'left'
        //                },
        //                tooltip : {
        //                    trigger: 'axis'
        //                },
        //                //legend: {
        //                //    orient : 'vertical',
        //                //    x : 'right',
        //                //    y : 'bottom',
        //                //    data:['约会类型']
        //                //},
        //                //toolbox: {
        //                //    show : true,
        //                //    feature : {
        //                //        saveAsImage : {show: true}
        //                //    }
        //                //},
        //                polar : [
        //                    {
        //                        indicator : [
        //                            { text: '同地区同类型'},
        //                            { text: '同地区不同类型'},
        //                            { text: '同市同类型'},
        //                            { text: '同市不同类型'}
        //                        ]
        //                    }
        //                ],
        //                calculable : true,
        //                series : [
        //                    {
        //                        name: '约会类型',
        //                        type: 'radar',
        //                        data : [
        //                            {
        //                                value : [4300, 10000, 28000, 35000, 50000, 19000],
        //                                name : '约会类型'
        //                            }
        //                        ]
        //                    }
        //                ]
        //            };
        //
        //
        //            var myChart = echarts.init(document.getElementById($scope.id),'macarons');
        //            myChart.setOption(option);
        //        }
        //    };
        //});
});

StatisDataCtrl.$inject = ['$scope', 'UserServ', 'GroupServ', 'ActivityServ'];

function StatisDataCtrl($scope, UserServ, GroupServ, ActivityServ) {
    $scope.cycle = [
        {name:'当前周',id:'0'},
        {name:'当天',id:'1'},
        {name:'上一周',id:'2'},
        {name:'当前月',id:'3'},
        {name:'上一月',id:'4'}
    ];
    $scope.cycleType = [
        {name:'按天',id:'0'},
        {name:'按月',id:'1'}
    ];

    // 用户量统计饼图
    $scope.userselected = $scope.cycle[0].id;//如果想要第一个值
    $scope.userData = [{value:0,name:'男'},{value:0,name:'女'}];
    $scope.userLegend = ["男", "女"];
    $scope.userSum = 0;

    // 队伍量统计饼图
    $scope.groupselected = $scope.cycle[0].id;//如果想要第一个值
    $scope.groupData = [{value:0,name:'男'},{value:0,name:'女'}];
    $scope.groupLegend = ["男", "女"];
    $scope.groupSum = 0;

    $scope.userCycleType = $scope.cycleType[0].id;//如果想要第一个值
    $scope.groupCycleType = $scope.cycleType[0].id;//如果想要第一个值
    $scope.activityCycleType = $scope.cycleType[0].id;//如果想要第一个值

    $scope.selectChange = function(type){
        if(1 == type){
            getUserDataForCharts($scope.userselected);
        }else{
            getGroupDataForCharts($scope.groupselected);
        }

    }

    // 用户总量统计
    function getUserDataForCharts(type){
        UserServ.getUserSumForCharts(type).success(function(data){
            if (data.code == 200) {
                var maleTotal = new Object();
                maleTotal.value = data.data.maleTotal;
                maleTotal.name = '男';

                var femaleTotal = new Object();
                femaleTotal.value = data.data.femaleTotal;
                femaleTotal.name = '女';

                $scope.userData = [];
                $scope.userData.push(maleTotal);
                $scope.userData.push(femaleTotal);

                $scope.userSum = data.data.userTotal;
            }

        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
        });
    }

    function getGroupDataForCharts(type){
        GroupServ.getGroupSumForCharts(type).success(function(data){
            if (data.code == 200) {
                var maleTotal = new Object();
                maleTotal.value = data.data.maleTotal ? data.data.maleTotal : 0;
                maleTotal.name = '男';

                var femaleTotal = new Object();
                femaleTotal.value = data.data.femaleTotal ? data.data.femaleTotal : 0;
                femaleTotal.name = '女';

                $scope.groupData = [];
                $scope.groupData.push(maleTotal);
                $scope.groupData.push(femaleTotal);

                $scope.groupSum = data.data.groupTotal;
            }

        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
        });
    }

    // 队伍总量统计
    getUserDataForCharts(0);

    getGroupDataForCharts(0);

    $scope.userLegend = ["用户量"];
    $scope.userBarItem = [];  //Y轴展示数据
    $scope.userBarData = [];

    $scope.groupLegend = ["队伍量"];
    $scope.groupBarItem = [];  //Y轴展示数据
    $scope.groupBarData = [];

    $scope.activityLegend = ["约会量"];
    $scope.activityBarItem = [];  //Y轴展示数据
    $scope.activityBarData = [];

    var moment = require('moment');
    $scope.params = {};
    $scope.user = {};
    $scope.user.beginTime = getNowFormatDate();
    $scope.user.endTime = getNowFormatDate();
    $scope.group = {};
    $scope.group.beginTime = getNowFormatDate();
    $scope.group.endTime = getNowFormatDate();

    $scope.activity = {};
    $scope.activity.beginTime = getNowFormatDate();
    $scope.activity.endTime = getNowFormatDate();

    $scope.searchUserSumForBar = function(){

        if($scope.user.beginTime == undefined || $scope.user.endTime == undefined){
            swal("开始时间跟结束时间不能为空！")
        }

        if ($scope.user.beginTime) {
            $scope.params.beginTime = moment($scope.user.beginTime).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.user.endTime) {
            $scope.params.endTime = moment($scope.user.endTime).format('YYYY-MM-DD hh:mm:ss');
        }

        $scope.params.type = $scope.userCycleType;
        UserServ.getUserSumForBar($scope.params).success(function(data){
            if (data.code == 200) {
                $scope.userBarItem = data.item;
                $scope.userBarData = [];
                $scope.userBarData.push(data.data);
            }

        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
        });
    };

    $scope.searchGroupSumForBar = function(){

        if($scope.group.beginTime == undefined || $scope.group.endTime == undefined){
            swal("开始时间跟结束时间不能为空！")
        }

        if ($scope.group.beginTime) {
            $scope.params.beginTime = moment($scope.group.beginTime).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.group.endTime) {
            $scope.params.endTime = moment($scope.group.endTime).format('YYYY-MM-DD hh:mm:ss');
        }

        $scope.params.type = $scope.groupCycleType;
        GroupServ.getGroupSumForBar($scope.params).success(function(data){
            if (data.code == 200) {
                $scope.groupBarItem = data.item;
                $scope.groupBarData = [];
                $scope.groupBarData.push(data.data);
            }

        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
        });
    };

    $scope.searchActivitySumForBar = function(){

        if($scope.activity.beginTime == undefined || $scope.activity.endTime == undefined){
            swal("开始时间跟结束时间不能为空！")
        }

        if ($scope.activity.beginTime) {
            $scope.params.beginTime = moment($scope.activity.beginTime).format('YYYY-MM-DD hh:mm:ss');
        }
        if ($scope.activity.endTime) {
            $scope.params.endTime = moment($scope.activity.endTime).format('YYYY-MM-DD hh:mm:ss');
        }

        $scope.params.type = $scope.groupCycleType;

        ActivityServ.getActivitySumForBar($scope.params).success(function(data){
            if (data.code == 200) {
                $scope.activityBarItem = data.item;
                $scope.activityBarData = [];
                $scope.activityBarData.push(data.data);
            }

        }).error(function (r) {
            console.log(r);
        })['finally'](function () {
        });
    };

    $scope.searchUserSumForBar();
    $scope.searchGroupSumForBar();
    $scope.searchActivitySumForBar();

}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate
        //+ " " + date.getHours() + seperator2 + date.getMinutes()
        //+ seperator2 + date.getSeconds();
    return currentdate;
}
/**
 * Created by sherry on 2016/6/6.
 */

define(['angularAMD', 'uiRouter', 'uiGrid', 'datetimepickertpl', 'dateTimeInput', 'filter/filter', 'directive/directive', 'ngUploadFile'], function (angularAMD) {

    'use strict';
    var app = angular.module("app", ['ui.router', 'ui.grid', 'ui.dateTimeInput', 'ui.bootstrap.datetimepicker', 'ui.grid.pagination', 'ui.grid.selection', 'ui.grid.exporter','ui.grid.treeView','ngFileUpload']);

    var moment = require('moment');
    moment.locale('zh-cn');

    app.value("data_host", location.protocol + '//' + window.location.host);

    app.config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('user', angularAMD.route({
                url: '/user',
                templateUrl: 'src/view/user.html',
                controller: 'UserCtrl', controllerUrl: 'controller/userCtrl'
            }))
            .state('userDetails', angularAMD.route({
                url: '/userDetails/:userId',
                templateUrl: 'src/view/userDetails.html',
                controller: 'UserDetailsCtrl', controllerUrl: 'controller/userDetailsCtrl'
            }))
            .state('usersetting', angularAMD.route({
                url: '/usersetting',
                templateUrl: 'src/view/userSetting.html',
                controller: 'UserSettingCtrl', controllerUrl: 'controller/userSettingCtrl'
            }))
            .state('group', angularAMD.route({
                url:'/group',
                templateUrl:'src/view/group.html',
                controller:'GroupCtrl', controllerUrl:'controller/groupCtrl'
            }))
            .state('activity', angularAMD.route({
                url:'/activity',
                templateUrl:'src/view/activity.html',
                controller:'ActivityCtrl', controllerUrl:'controller/activityCtrl'
            }))
            .state('complain', angularAMD.route({
                url: '/complain',
                templateUrl: 'src/view/complain.html',
                controller: 'ComplainCtrl', controllerUrl: 'controller/complainCtrl'
            }))
            .state('announcePublish', angularAMD.route({
                url: '/announcePublish',
                templateUrl: 'src/view/announcePublish.html',
                controller: 'AnnouncePublishCtrl', controllerUrl: 'controller/announcePublishCtrl'
            }))
            .state('announceHistory', angularAMD.route({
                url: '/announceHistory',
                templateUrl: 'src/view/announceHistory.html',
                controller: 'AnnounceHistoryCtrl', controllerUrl: 'controller/announceHistoryCtrl'
            }))
            .state('info', angularAMD.route({
                url: '/info',
                templateUrl: 'src/view/info.html',
                controller: 'InfoCtrl', controllerUrl: 'controller/infoCtrl'
            }))
            .state('interestLabel', angularAMD.route({
                url: '/interestLabel',
                templateUrl: 'src/view/interestLabel.html',
                controller: 'InterestLabelCtrl', controllerUrl: 'controller/interestLabelCtrl'
            }))
            .state('jobLabel', angularAMD.route({
                url: '/jobLabel',
                templateUrl: 'src/view/jobLabel.html',
                controller: 'JobLabelCtrl', controllerUrl: 'controller/jobLabelCtrl'
            }))
            .state('datingConf', angularAMD.route({
                url: '/datingConf',
                templateUrl: 'src/view/datingConf.html',
                controller: 'DatingConfCtrl', controllerUrl: 'controller/datingConfCtrl'
            }))
            .state('clientConf', angularAMD.route({
                url: '/clientConf',
                templateUrl: 'src/view/clientConf.html',
                controller: 'ClientConfCtrl', controllerUrl: 'controller/clientConfCtrl'
            }))
            .state('robotConf', angularAMD.route({
                url: '/robotConf',
                templateUrl: 'src/view/robotConf.html',
                controller: 'RobotConfCtrl', controllerUrl: 'controller/robotConfCtrl'
            }))
            .state('userStatisData', angularAMD.route({
                url: '/userStatisData',
                templateUrl: 'src/view/userStatisData.html',
                controller: 'UserStatisDataCtrl', controllerUrl: 'controller/userStatisDataCtrl'
            }))
            .state('groupStatisData', angularAMD.route({
                url: '/groupStatisData',
                templateUrl: 'src/view/groupStatisData.html',
                controller: 'GroupStatisDataCtrl', controllerUrl: 'controller/groupStatisDataCtrl'
            }))
            .state('activityStatisData', angularAMD.route({
                url: '/activityStatisData',
                templateUrl: 'src/view/activityStatisData.html',
                controller: 'ActivityStatisDataCtrl', controllerUrl: 'controller/activityStatisDataCtrl'
            }))
            .state('statisData', angularAMD.route({
                url: '/statisData',
                templateUrl: 'src/view/statisData.html',
                controller: 'StatisDataCtrl', controllerUrl: 'controller/statisDataCtrl'
            }))
            .state('varConf', angularAMD.route({
                url: '/varConf',
                templateUrl: 'src/view/varConf.html',
                controller: 'VarConfCtrl', controllerUrl: 'controller/varConfCtrl'
            }))
            .state('adminLoginHis', angularAMD.route({
                url: '/adminLoginHis',
                templateUrl: 'src/view/adminLoginHis.html',
                controller: 'AdminLoginHisCtrl', controllerUrl: 'controller/adminLoginHisCtrl'
            }))
            .state('adminConf', angularAMD.route({
                url: '/adminConf',
                templateUrl: 'src/view/adminConf.html',
                controller: 'AdminConfCtrl', controllerUrl: 'controller/adminConfCtrl'
            }))
            .state('roleConf', angularAMD.route({
                url: '/roleConf',
                templateUrl: 'src/view/roleConf.html',
                controller: 'RoleConfCtrl', controllerUrl: 'controller/roleConfCtrl'
            }))
            .state('pswConf', angularAMD.route({
                url: '/pswConf',
                templateUrl: 'src/view/pswConf.html',
                controller: 'PswConfCtrl', controllerUrl: 'controller/pswConfCtrl'
            }))
            .state('clearHuanXinConf', angularAMD.route({
                url: '/clearHuanXinConf',
                templateUrl: 'src/view/clearHuanXinConf.html',
                controller: 'ClearHuanXinConfCtrl', controllerUrl: 'controller/clearHuanXinConfCtrl'
            }))
            .state('pie', angularAMD.route({
            url: '/pie',
            templateUrl: 'src/view/pie.html',
            controller: 'PieCtrl', controllerUrl: 'controller/pieCtrl'
            }))
            .state('advertisement',angularAMD.route({
                url:'/advertisement' ,
                templateUrl:'src/view/advertisement.html',
                controller:'AdvertisementCtrl',controllerUrl:'controller/advertisementCtrl'
            }))
            .state('bar',angularAMD.route({
                url:'/bar' ,
                templateUrl:'src/view/bar.html',
                controller:'BarCtrl',controllerUrl:'controller/barCtrl'
            }))
            .state('appVersion',angularAMD.route({
                url:'/appVersion' ,
                templateUrl:'src/view/appVersion.html',
                controller:'AppVersionCtrl',controllerUrl:'controller/appVersionCtrl'
            }))
            .state('dailyLivingStatisData',angularAMD.route({
                url:'/dailyLivingStatisData' ,
                templateUrl:'src/view/dailyLivingStatisData.html',
                controller:'LivingStatisDataCtrl',controllerUrl:'controller/livingStatisDataCtrl'
            }))
            .state('userBlackList',angularAMD.route({
                url:'/userBlackList' ,
                templateUrl:'src/view/userBlackList.html',
                controller:'UserBlackListCtrl',controllerUrl:'controller/userBlackListCtrl'
            }))
            .state('fancyLabel',angularAMD.route({
                url:'/fancyLabel' ,
                templateUrl:'src/view/fancyLabel.html',
                controller:'FancyLabelCtrl',controllerUrl:'controller/fancyLabelCtrl'
            }))
        ;
        //$urlRouterProvider.when('', 'user');
        //$urlRouterProvider.otherwise("/beta");
    });

    app.config(function ($httpProvider) {
        // Use x-www-form-urlencoded Content-Type
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
        $httpProvider.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';
        $httpProvider.defaults.transformRequest = [function (data) {
            return angular.isObject(data) && String(data) !== '[object File]' ? $.param(data, true) : data;
        }];
    });

    return angularAMD.bootstrap(app);

});
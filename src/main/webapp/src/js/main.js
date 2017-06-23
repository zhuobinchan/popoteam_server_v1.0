/**
 * Created by sherry on 2016/6/6.
 */
require.config({

    baseUrl: 'src/js',
    paths: {
        jquery: '../../dep/jQuery-2.1.4/jquery-2.1.4.min',
        bootstrap: '../../dep/bootstrap-3.3.6/js/bootstrap.min',
        angular: '../../dep/angular-1.4.8/angular.min',
        angularAMD: '../../dep/angularAMD/angularAMD.min',
        angularCookies: '../../dep/angular-1.4.8/angular-cookies.min',
        angularAnimate: '../../dep/angular-1.4.8/angular-animate.min',
        uiGrid: '../../dep/ng-grid/ui-grid',
        uiRouter: '../../dep/ui-router/angular-ui-router.min',
        angularSanitize: '../../dep/angular-1.4.8/angular-sanitize.min',
        sweetAlert: '../../dep/sweetalert/sweet-alert.min',
        underscore: '../../dep/underscore-1.8.3/underscore-min',
        datetimepicker: '../../dep/angular-bootstrap-datetimepicker/src/js/datetimepicker',
        datetimepickertpl:'../../dep/angular-bootstrap-datetimepicker/src/js/datetimepicker.templates',
        dateTimeInput: '../../dep/angular-bootstrap-datetimepicker/src/js/dateTimeInput',
        FileAPI: '../../dep/ng-file-upload-bower-12.0.4/FileAPIConfig',
        ngUploadFile: '../../dep/ng-file-upload-bower-12.0.4/ng-file-upload.min',
        ngUploadFile_shim: '../../dep/ng-file-upload-bower-12.0.4/ng-file-upload-shim',
        moment: '../../dep/moment/min/moment-with-locales.min',
        echarts: '../../dep/echarts-2.2.7/build/dist/echarts-all.js',
        wangEditor:'../../dep/wangEditor-2.1.23/dist/js/wangEditor',
    },
    shim: {
        angular:{
            exports: 'angular'
        },
        ngStorage:{
            deps: ['angular']
        },
        angularAMD: {
            deps: ['angular']
        },
        uiRouter: {
            deps: ['angular']
        },
        angularAnimate: {
            deps: ['angularAMD']
        },
        uiGrid: {
            deps: ['angular']
        },
        bootstrap: {
            deps: ['jquery']
        },
        angularCookies:{
            deps: ['angular']
        },
        datetimepicker:{
            deps: ['moment','angular']
        },
        datetimepickertpl:{
            deps: ['datetimepicker']
        },
        moment:{
            exports: 'moment'
        },
        dateTimeInput:{
            deps: ['moment','angular']
        },
        sweetAlert:{
            exports: 'swal'
        },
        ngUploadFile: {
            deps: ['angular', 'ngUploadFile_shim']
        },
        ngUploadFile_shim:{
            deps: ['FileAPI']
        },
        echarts:{
            deps:['angular','jquery']
        },
        jqueryHotkeys:{
            deps:['jquery'],
        },
        bootstrapWysiwyg:{
            deps:['jqueryHotkeys','jquery','bootstrap']
        },
        wangEditor:{
            depts:['jquery']
        },

    },


    deps: ['jquery','bootstrap','underscore','app']

});

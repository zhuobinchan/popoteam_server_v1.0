/**
 * Created by sherry on 2016/6/8.
 */
define(['app', 'directive/multiCitiesSelector.directive', 'service/announceServ'], function (app) {
    app.controller('AnnouncePublishCtrl', AnnouncePublishCtrl);
});

AnnouncePublishCtrl.$inject = ['$scope', 'AnnounceServ', 'Upload', 'data_host'];

function AnnouncePublishCtrl($scope, AnnounceServ, Upload, data_host) {

    $scope.selectedDistricts = [];
    $scope.announceObj = {title: '', content: '', url:''};
    $scope.announceType = 1;
    $scope.userType = 1;
    $scope.file = null;
    $scope.imageFile = null;

    $scope.selectCities = selectCities;
    $scope.addAnnounce = addAnnounce;

    $scope.change = function(type){
        $scope.announceType = type;
    }
    function selectCities() {
        $('#multiCitiesModal').modal('show')
    }
    
    function addAnnounce() {
        $scope.loading = true;
        // $scope.editorContent = $('#myEditor').html();

        if (1 == $scope.announceType){
            publishByDis();
        }else{
            publicByUser();
        }
    }

    // 按地区分布
    function publishByDis(){
        if(!$scope.url&&$('#myEditor').html()==""){
            swal("富文本编辑区 和 公告网址不能同时为空");
            return;
        }

        if (!$scope.announceObj.title) {
            swal("标题不能为空");
            return;
        }

        if($scope.selectedDistricts){
            $scope.announceObj.districts = [];
            $scope.announceObj.types = [];

            var districtArray = [], typeArray= [];
            _($scope.selectedDistricts).each(function (obj) {
                if(obj.cities){
                    _(obj.cities).each(function (elem) {
                        districtArray.push(elem.code);
                        typeArray.push(1);
                    });
                }
                else{
                    districtArray.push(obj.province.code);
                    typeArray.push(0);
                }
            });
        }
        $scope.announceObj.districts = districtArray;
        $scope.announceObj.types = typeArray;
        //AnnounceServ.publish($scope.announceObj).success(function (data) {
        //    if(200 == data.code) {
        //        swal('发布成功');
        //    }else{
        //        swal('发布失败，请稍后再试');
        //    }
        //}).error(function (err) {
        //    console.log(err);
        //})['finally'](function () {
        //    $scope.isLoading = false;
        //    $scope.announceObj = [];
        //    $scope.selectedDistricts = [];
        //});

        Upload.upload({
            url: data_host + '/ctrl/manage/announcement/release?' + $.param({
                // title: $scope.announceObj.title,
                // content: $scope.announceObj.content,
                // url: $scope.announceObj.url,
                // districts : $scope.announceObj.districts,
                // types :  $scope.announceObj.types
            }),
            method: 'POST',
            dataType: "json",
            //transformRequest: angular.identity,
            file: $scope.imageFile,
            data: {
                // file: $scope.imageFile,
                title: $scope.announceObj.title,
                content:$scope.announceObj.content,
                editorArea: $('#myEditor').html(),
                url: $scope.announceObj.url,
                districts : $scope.announceObj.districts,
                types :  $scope.announceObj.types
            }
        }).then(function (resp) {
            if (resp.data.code == 200) {
                swal('发布成功');
            }
        }, function (resp) {
            swal('发布失败，请稍后再试');
        })['finally'](function () {
            $scope.uploading = false;
            $scope.announceObj = [];
            $scope.selectedDistricts = [];
            $('#myEditor').html("")
        });
    }

    function publicByUser(){
        if(!$scope.url&&$('#myEditor').html()==""){
            swal("富文本编辑区 和 公告网址不能同时为空");
            return;
        }

        if (!$scope.file) {
            swal("请上传用户名单");
            return;
        }

        $scope.uploading = true;

        var params = {
            uploadfiles: $scope.file,
            //imageFile: $scope.imageFile == null? null : $scope.imageFile,
            sizes: 0,
            title: $scope.announceObj.title,
            content:$scope.announceObj.content,
            editorArea: $('#myEditor').html(),
            url: $scope.announceObj.url,
            userType: $scope.userType
        }

        if($scope.imageFile != null){
            params['imageFile'] =  $scope.imageFile;
        }
        Upload.upload({
            url: data_host + '/ctrl/manage/announcement/uploadUserToRelease',
            method: 'POST',
            transformRequest: angular.identity,
            file: $scope.imageFile,
            data: params
        }).then(function (resp) {
            if (resp.data.code == 200) {
                swal("数据导入成功！" + "不存在用户：" + resp.data.fail);
            }
        }, function (resp) {
            swal("上传失败，请稍后尝试");
        })['finally'](function () {
            $scope.uploading = false;
            $scope.announceObj = [];
            $scope.file = null;
            $scope.imageFile = null;
            $('#myEditor').html("")
        });
    }

    //加载富文本框的模块
    require(['jquery','wangEditor'] ,function ($,wangEditor) {
        var editor = new wangEditor('myEditor');
        // 上传图片（举例）
        editor.config.uploadImgUrl = data_host + '/ctrl/manage/announcement/image/upload';
        editor.config.uploadImgFileName = "image"
        // 设置 headers（举例）
        editor.config.uploadHeaders = {
            'Accept' : 'text/x-json',
        };
        editor.config.menus=[
            'source',
            '|',
            'bold',
            'underline',
            'italic',
            'strikethrough',
            'eraser',
            'forecolor',
            'bgcolor',
            '|',
            'quote',
            'fontfamily',
            'fontsize',
            'head',
            'unorderlist',
            'orderlist',
            'alignleft',
            'aligncenter',
            'alignright',
            '|',
            'link',
            'unlink',
            'table',
            'emotion',
            '|',
            'img',
            'video',
            'location',
            'insertcode',
            '|',
            'undo',
            'redo',
            'fullscreen',
            '|',
            'lineheight',
            'indent'
        ];
        // 隐藏掉插入网络图片功能。该配置，只有在你正确配置了图片上传功能之后才可用。
        editor.config.hideLinkImg = false;
        editor.create();

    });

}
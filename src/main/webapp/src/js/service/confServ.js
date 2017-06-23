/**
 * Created by sherry on 2016/6/16.
 */

'use strict';
define(['app'], function (app) {
    app.factory('ConfServ', ConfServ);
});

var ConfServ = ['data_host', '$http', function (data_host, $http) {
    return {
        getAppSetting: function () {
            return $http.get(data_host + '/ctrl/setting/searchApp');
        },
        updateAppSetting: function (obj) {
            console.log(obj);
            return $http.post(
                '/ctrl/setting/updateApp',
                obj
            );
        },
        getDateSetting: function () {
            return $http.get(data_host + '/ctrl/setting/searchDate');
        },
        updateDateSetting: function (obj) {
            return $http.post(
                '/ctrl/setting/updateDate',
                obj
            );
        },
        getMatchSetting: function () {
            return $http.get('/ctrl/setting/searchMatchWeight');
        },
        updateMatchSetting: function (obj) {
            return $http.post(
                '/ctrl/setting/updateMatchWeight',
                obj
            );
        },
        getVarSetting: function () {
            return $http.get(data_host + '/ctrl/setting/searchVarSetting');
        },
        updateVarSetting: function (obj) {
            console.log(obj);
            return $http.post(
                '/ctrl/setting/updateVarSetting',
                obj
            );
        },
        updateSuperLikeConfig: function (obj){
            return $http.post(data_host +
                '/ctrl/setting/userSuperLikeConfig/update',
                obj
            );
        },
        searchSuperLikeConfigInPage:function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/setting/userSuperLikeConfig/search?methodType=3&size=' + size + '&page=' + page
                + p);
        },
        updateUserSuperLikeConfig:function (userId,times) {
            return $http.post(
                data_host + '/ctrl/setting/userSuperLikeConfig/update',
                {
                    id: userId,
                    times:times
                }
            );
        },
        addUserSuperLikeConfig:function (userIdentify,userPhone,times) {
            return $http.post(
                data_host + '/ctrl/setting/userSuperLikeConfig/add',
                {
                    userIdentify: userIdentify,
                    userPhone:userPhone,
                    times:times
                }
            );
        },
        deleteUserSuperLikeConfigByIds:function (ids) {
            return $http.post(
                data_host + '/ctrl/setting/userSuperLikeConfig/delete',
                {
                    ids:ids
                }
            );
        }
    };
}];
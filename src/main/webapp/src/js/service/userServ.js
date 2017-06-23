/**
 * Created by sherry on 2016/6/12.
 */
define(['app'], function (app) {

    app.factory('UserServ',UserServ)

});

var UserServ = ['data_host','$http',function(data_host,$http){
    return {
        getAll: function () {
            return $http.get(data_host + '/ctrl/user/search?methodType=1');
        },

        getById: function (id) {
            return $http.get(data_host + '/ctrl/user/search?methodType=2&id=' + id);
        },

        getByParamsInPages: function (params, addParams) {
            return $http.get(data_host + '/ctrl/user/search?methodType=3&' + $.param(params) + '&' + $.param(addParams));
        },

        export: function (params, addParams) {
            return $http.get(data_host + '/ctrl/user/export?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },

        getInPages: function (page, size) {
            return $http.get(data_host + '/ctrl/user/search?methodType=3&page=' + page + '&size=' + size);
        },

        searchPhoto: function (page, size, userId) {
            return $http.get(data_host + '/ctrl/user/searchPhoto?methodType=3&page=' + page + '&size=' + size + '&userId=' + userId);
        },

        publish: function (announceObj) {
            return $http.post(
                data_host + '/ctrl/user/release',
                announceObj
            );
        },

        freeze: function (userIds, status) {
            return $http.post(
                    data_host + '/ctrl/user/freezeUserOrNot',
                    {
                        userIds: userIds,
                        freeze: status
                    }
            );
        },

        getFriendsById: function (params) {
            return $http.get(data_host + '/ctrl/user/searchFriend?methodType=3&' + $.param(params));
        },

        getComplainById: function (id) {
            return $http.get(data_host + '/ctrl/user/searchComplain?methodType=2&id=' + id);
        },

        getComplainsInPages: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/user/searchComplain?style=40w_40h&methodType=3&size=' + size + '&page=' + page
                    + p);
        },

        deletePhoto: function (photoId) {
            return $http.post(
                    data_host + '/ctrl/user/deletePhoto',
                    {photoId: photoId}
            );
        },

        warnUser: function (photoId,userId) {
            return $http.post(
                    data_host + '/ctrl/user/warnUser',
                    {photoId: photoId,userId:userId}
            );
        },

        getUserStaticDataPage: function (params, addParams) {
            return $http.get(data_host + '/ctrl/user/searchUserStatisData?methodType=2&' + $.param(params) + '&' + $.param(addParams));
        },

        exportUserStaticData: function (params, addParams) {
            return $http.get(data_host + '/ctrl/user/exportExcel?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },
        getUserInterestStatisData: function (params) {
            return $http.get(data_host + '/ctrl/user/searchUserInterestStatisData?methodType=2&' + $.param(params));
        },

        getUserJobStaticDataPage: function (params) {
            return $http.get(data_host + '/ctrl/user/searchUserJobStatisData?methodType=2&' + $.param(params));
        },

        exportMonthData: function (params, addParams) {
            return $http.get(data_host + '/ctrl/user/exportMonthData?' + $.param(params) + '&' + $.param(addParams), {responseType: "blob"});
        },

        getUserSumStatisData: function () {
            return $http.get(data_host + '/ctrl/user/searcnUserSumStatisData');
        },

        getUserSumForCharts: function (type) {
            return $http.get(data_host + '/ctrl/user/searcnUserSumForCharts?type=' + type);
        },
        getUserSumForBar: function (params) {
            return $http.get(data_host + '/ctrl/user/searcnUserSumForBar?' + $.param(params));
        },

        getUserBlackListsInPage: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/user/searchUserBlackList?methodType=3&size=' + size + '&page=' + page
                + p);
        },

        setUserBlackListStatusByIds:function (ids , status) {
            return $http.post(
                data_host + '/ctrl/user/setUserBlackListStatus',
                {
                    ids: ids,
                    status:status
                }
            );
        },

        addUserBlackListsByIdsOrPhones:function (identifiesOrPhones , type) {
            return $http.post(
                data_host + '/ctrl/user/addUserBlackLists',
                {
                    identifiesOrPhones: identifiesOrPhones,
                    type:type
                }
            );
        }


    }
}];
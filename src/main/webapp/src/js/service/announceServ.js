/**
 * Created by sherry on 2016/6/8.
 */
define(['app'], function (app) {

    app.factory('AnnounceServ', AnnounceServ)

});

var AnnounceServ = ['data_host', '$http', function (data_host, $http) {
    return {
        getAll: function () {
            return $http.get(data_host + '/ctrl/manage/announcement/search?methodType=1');
        },

        getById: function (id) {
            return $http.get(data_host + '/ctrl/manage/announcement/search?methodType=2&id=' + id);
        },

        getByParamsInPages: function (params, addparams) {
            return $http.get(data_host + '/ctrl/manage/announcement/search?methodType=3&' + $.param(params) + '&' + $.param(addparams));
        },

        getInPages: function (page, size) {
            return $http.get(data_host + '/ctrl/manage/announcement/search?methodType=3&page=' + page + '&size=' + size);
        },

        publish: function (announceObj) {
            return $http.post(
                data_host + '/ctrl/manage/announcement/release',
                announceObj
            );
        },

        block: function (announcementIds, status) {
            return $http.post(data_host + '/ctrl/manage/announcement/openOrNot?announcementIds=' + announcementIds + '&isOpen=' + status);
        },
        exportUserById:function (announcementId) {
            return $http.get(
                data_host + '/ctrl/manage/announcement/exportUserById?announcementId='+announcementId,
                {
                    responseType: "blob"
                });
        }
    }
}];
/**
 * Created by sherry on 2016/6/8.
 */
define(['app'], function (app) {

    app.factory('InfoServ', InfoServ)

});

var InfoServ = ['data_host', '$http', function (data_host, $http) {
    return {

        publishInfo: function (info) {
            return $http.post(
                    data_host + '/ctrl/manage/consultation/release',
                    info
            );
        },

        updateInfo: function (info) {
            return $http.post(
                    data_host + '/ctrl/manage/consultation/update',
                    info
            );
        },

        getInfoById: function (id) {
            return $http.get(data_host + '/ctrl/manage/consultation/search?methodType=2&id=' + id);
        },

        getInfosInPages: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/manage/consultation/search?methodType=3&size=' + size + '&page=' + page
                    + p);
        },

        setInfosStatus: function (infoIds,isOpen) {
            return $http.post(
                    data_host + '/ctrl/manage/consultation/openOrNot',
                    {
                        consultationIds: infoIds,
                        isOpen: isOpen
                    }
            );
        }
    }
}];
/**
 * Created by chenzhoubin on 2017/5/2.
 */
define(['app'], function (app) {

    app.factory('FancyLabelServ', FancyLabelServ)

});

var FancyLabelServ = ['data_host', '$http', function (data_host, $http) {
    return {
        getFancyLabelById: function (id) {
            return $http.get(data_host + '/ctrl/manage/fancy/search?methodType=2&id=' + id);
        },

        getFancyLabelsInPages: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/manage/fancy/search?methodType=3&size=' + size + '&page=' + page
                + p);
        },

        setFancyLabelStatus: function (ids, isOpen) {
            return $http.post(
                data_host + '/ctrl/manage/fancy/openOrNot',
                {
                    fancyIds: ids,
                    isOpen: isOpen
                }
            );
        },

        addFancyLabel: function (label) {
            return $http.post(
                data_host + '/ctrl/manage/fancy/add',
                label
            );
        },

        updateFancyLabel: function (label) {
            return $http.post(
                data_host + '/ctrl/manage/fancy/update',
                label
            );
        }
    }
}];
/**
 * Created by sherry on 2016/6/8.
 */
define(['app'], function (app) {

    app.factory('InterestLabelServ', InterestLabelServ)

});

var InterestLabelServ = ['data_host', '$http', function (data_host, $http) {
    return {
        getInterestLabelById: function (id) {
            return $http.get(data_host + '/ctrl/manage/interest/search?methodType=2&id=' + id);
        },

        getInterestLabelsInPages: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/manage/interest/search?methodType=3&size=' + size + '&page=' + page
                    + p);
        },

        setInterestLabelStatus: function (ids, isOpen) {
            return $http.post(
                    data_host + '/ctrl/manage/interest/openOrNot',
                    {
                        interestIds: ids,
                        isOpen: isOpen
                    }
            );
        },

        addInterestLabel: function (label) {
            return $http.post(
                    data_host + '/ctrl/manage/interest/add',
                    label
            );
        },

        updateInterestLabel: function (label) {
            return $http.post(
                    data_host + '/ctrl/manage/interest/update',
                    label
            );
        }
    }
}];
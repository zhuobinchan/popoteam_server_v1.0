/**
 * Created by sherry on 2016/6/8.
 */
define(['app'], function (app) {

    app.factory('JobLabelServ', JobLabelServ)

});

var JobLabelServ = ['data_host', '$http', function (data_host, $http) {
    return {
        getJobLabelById: function (id) {
            return $http.get(data_host + '/ctrl/manage/job/search?methodType=2&id=' + id);
        },

        getJobLabelsInPages: function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/manage/job/search?methodType=3&size=' + size + '&page=' + page
                    + p);
        },

        setJobLabelStatus: function (ids, isOpen) {
            return $http.post(
                    data_host + '/ctrl/manage/job/openOrNot',
                    {
                        jobIds: ids,
                        isOpen: isOpen
                    }
            );
        },

        addJobLabel: function (label) {
            return $http.post(
                    data_host + '/ctrl/manage/job/add',
                    label
            );
        },

        updateJobLabel: function (label) {
            return $http.post(
                    data_host + '/ctrl/manage/job/update',
                    label
            );
        }
    }
}];
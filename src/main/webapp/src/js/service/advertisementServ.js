/**
 * Created by chenzhuobin on 2017/3/22.
 */
define(['app'], function (app) {

    app.factory('AdvertisementServ', AdvertisementServ);

});

var AdvertisementServ =['data_host', '$http', function (data_host, $http){
    return {
        getAdvertisementInPages:function (page, size, params) {
            var p = params ? params : '';
            return $http.get(data_host + '/ctrl/advertisement/search?methodType=3&size=' + size + '&page=' + page
                + p);
        },
       /* addAdvertisement: function (advertisement) {
            return $http.post(
                data_host + '/ctrl/advertisement/add',
                advertisement
            );


        },

        updateAdvertisement: function (advertisement) {
            return $http.post(
                data_host + '/ctrl/advertisement/update',
                advertisement
            );
        },*/
        deleteAdvertisementByIds:function (ids) {
            return $http.post(
                data_host + '/ctrl/advertisement/delete',
                {
                    ids: ids
                }
            );
        },
        setAdvertisementsStatusByIds:function (ids,status) {
            return $http.post(
                data_host + '/ctrl/advertisement/setStatus',
                {
                    ids: ids,
                    status:status
                }
            );
        }



    }
}];
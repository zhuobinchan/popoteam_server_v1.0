/**
 * Created by v1sionary on 2016/6/7.
 */
define(['app'], function (app) {

    app.factory('CommonServ',CommonServ)

});

var CommonServ = ['data_host','$http',function(data_host,$http){
    return{
        getDistricts: function(type,code){
            return $http.get(data_host+'/ctrl/common/getDistrict?type='+type+(code?'&code='+code:''));
        },
        getBarList: function(){
            return $http.get(data_host+'/ctrl/common/getBarList');
        },
        downloadFileByUrl:function (url) {
            window.location.href = url;
            return ;
            // return $http.get(url,{responseType: "arraybuffer"});
        }

    }
}];
/**
 * Created by xmu on 2015-12-31.
 */
require(['angularAMD'], function (angularAMD) {

    angularAMD.filter('dollartext', function () {

        return function (input) {
            return Number(input) > 0 ? ("$" + input) : input;
        };

    });

    angularAMD.filter('percent', function () {

        return function (input) {
            return input + '%';
        };

    });

    angularAMD.filter('selectFilter', function () {
        var selectHash = {
            false: '否',
            true: '是'
        };
        return function (input) {
            return selectHash[input];
        };
    });

    angularAMD.filter('gendeFilter', function () {
        var genderHash = {
            M: '男',
            F: '女'
        };
        return function (input) {
            return genderHash[input];
        };
    });

    angularAMD.filter('ageFilter', function () {
        return function (input) {
            if (undefined == input || null == input) {
                return '--';
            } else {
                var dt = Date.parse(input.replace(/-/g, "/"));
                var a = new Date(dt);
                var now = new Date();
                var s = now.getTime() - a.getTime();
                return Math.floor(s / (365 * 24 * 3600 * 1000));
            }
        }
    });

    angularAMD.filter('toFixedFilter', function () {
        return function (input) {
            return Number(input).toFixed(2);
        }
    });

    angularAMD.filter('numberNaNFilter', function () {
        return function (input) {
            return isNaN(input) ? 0 : input;
        }
    });

    angularAMD.filter('timeRangeFilter', function () {
        return function (input, min, max) {
            min = parseInt(min);
            max = parseInt(max);
            for (var i = min; i <= max; i++) {
                if (i < 10) {
                    i = '0' + i;
                }
                input.push('' + i);
            }
            return input;
        }
    });

    angularAMD.filter('weekdayFilter', function () {
        var hash = {
            1: '一',
            2: '二',
            3: '三',
            4: '四',
            5: '五',
            6: '六',
            7: '七'
        };
        return function (input) {
            if (input) {
                return '周' + hash[input];
            }
        }
    });

    angularAMD.filter('dateTimeFilter', function () {
        return function (input) {
            var date = new Date(input).toISOString();
            return moment(date).format('YYYY-MM-DD HH:mm:ss');
        }
    });

    angularAMD.filter('fileSizeFilter', function () {
        return function (input) {
            if (input && input != '') {
                var val = input / 1024;
                return (val >= 1 ? ((val / 1024) > 1 ? (val / 1024).toFixed(2) + 'MB' : val.toFixed(2) + 'KB') : value + 'B');
            }
            return input;
        }
    });

    angularAMD.filter('strLengthFilter', function () {
        return function (input, maxLength) {
            if (typeof input == 'string' && input.length > maxLength) {
                return input.substring(0, maxLength - 3) + '...';
            }
            return input;
        }
    });

    angularAMD.filter('infoTypeFilter', function () {
        var hash = {
            0: '公开',
            1: '屏蔽中'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    angularAMD.filter('freezeTypeFilter', function () {
        var hash = {
            0: '正常',
            1: '冻结'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 标签创建者
     */
    angularAMD.filter('labelTypeFilter', function () {
        var hash = {
            0: '自建',
            1: '系统'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 标签状态
     */
    angularAMD.filter('labelStatusFilter', function () {
        var hash = {
            0: '屏蔽中',
            1: '正常'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 表格暂无
     */
    angularAMD.filter('emptyFilter', function () {
        return function (input) {
            if (input && input != '') {
                return input;
            }
            return '';
        }
    });

    /**
     * 文字长度
     */
    angularAMD.filter('textLengthFilter', function () {
        return function (input, length) {
            if (input && input.length > length) {
                return input.substring(0, length) + "...";
            } else {
                return input;
            }
        }
    });

    /**
     * 队伍性别
     */
    angularAMD.filter('groupTypeFilter', function () {
        var hash = {
            0: '男',
            1: '女',
            2: '混合'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 队伍类型
     */
    angularAMD.filter('groupBarFilter', function () {
        var hash = {
            1: '一起去唱K',
            2: '今晚去蹦迪',
            3:'出去喝一杯',
            4:'我们出去吧'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 队伍状态
     */
    angularAMD.filter('groupStatusFilter', function () {
        var hash = {
            1: '投票中',
            3: '解散'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    /**
     * 约会状态
     */
    angularAMD.filter('activityStatusFilter', function () {
        var hash = {
            0: '约会中',
            1: '解散'
        };
        return function(input){
            if(!isNaN(parseInt(input))){
                return hash[input];
            }
            return input;
        }
    });

    angularAMD.filter('trueOrFalseFilter', function () {
        var selectHash = {
            1: '是',
            0: '否'
        };
        return function (input) {
            return selectHash[input];
        };
    });

    angularAMD.filter('advertisementStatusFilter', function () {
        var selectHash = {
            1: '公开',
            0: '屏蔽中'
        };
        return function (input) {
            return selectHash[input];
        };
    });

    angularAMD.filter('barStatusFilter', function () {
        var selectHash = {
            1: '公开',
            0: '屏蔽中'
        };
        return function (input) {
            return selectHash[input];
        };
    });

    angularAMD.filter('barImgFilter', function () {
        return function (input) {
            var lastIndex = input.lastIndexOf(".");
            var result = input.substring(0,lastIndex).concat("_A".concat(input.substring(lastIndex,input.length)))
            return result;
        };
    });

    angularAMD.filter('picFormatFilter', function () {
        return function (input) {
            return input.replace("_","*");
        };
    });

    angularAMD.filter('picFormatSizeFilter', function () {
        return function (input,param) {
            if (undefined != input && null != input) {
                return input.split("_")[param];
            } else {
                return 0;
            }
        };
    });

});
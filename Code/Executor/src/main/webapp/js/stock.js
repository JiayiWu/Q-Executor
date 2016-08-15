/**
 * Created by song on 16-8-3.
 *
 * stock.html相关操作
 */

/**
 * 股票ID
 */
var id = '';

$(function () {
    id = getUrlParam('id');
    init();

    setInterval(function () {
        currentData.getCurrentData();
    }, 3000);
});

/**
 * 初始化
 */
function init() {
    $('#code').text('(' + id + ')');

    basicData.getBasicData();
    currentData.getCurrentData();

    // 固定侧边栏
    fix_sidebar($('.sidebar-left'));

    $('#tab-1').tab();
    $('#tab-2').tab();
}

/**
 * 最新数据对象
 * @type {{getCurrentData: currentData.getCurrentData, showCurrentData: currentData.showCurrentData}}
 */
var currentData = {
    getCurrentData: function () { // 获取最新数据
        jQuery.ajax({
            url: 'http://hq.finance.ifeng.com/q.php?l=' + id,
            dataType: 'script',
            cache: true,
            success: function () {
                currentData.showCurrentData(eval('json_q')[id]);
            }
        });
    },
    /**
     * 显示当前数据
     * @param data 当前数据，数据含义如下
     * 0:19.71                当前价格
     * 1:19.88                昨日收盘价
     * 2:-0.17                涨跌额
     * 3:-0.86                涨跌幅
     * 4:19.89                今日开盘价
     * 5:20.02                最高
     * 6:19.61                最低
     * 7:19.71
     * 8:19.73
     * 9:867473                成交量
     * 10:17184357.45        成交额
     * 11:19.71                买一报价
     * 12~15                买二至买五报价
     * 16:2900                买一申请2900股
     * 17~20                买二至买五申请
     * 21~25                卖一至卖五报价
     * 26~30                卖一至卖五申请
     * 31:4000
     * 32:2900
     * 33:4000
     * 34:1470881274        当前时间
     * 35:1470881276
     * 36:19.71
     */
    showCurrentData: function (data) {
        $('#price').text(data[0]);
        $('#price-2').text(data[0]);
        $('#close').text(data[1]);

        var color_inc = data[2] > 0 ? 'red' : 'green';
        $('#inc-dec-num').text(data[2]).css('color', color_inc);
        $('#inc-dec-rate').text(data[3] + '%').css('color', color_inc);
        if (data[2] > 0) {
            $('#increase-decrease').attr('src', '../images/increase.png');
        } else {
            $('#increase-decrease').attr('src', '../images/decrease.png');
        }

        $('#open').text(data[4]);
        $('#high').text(data[5]);
        $('#low').text(data[6]);

        $('#volume').text(format.format_number(data[9] / 100) + '手');
        $('#amount').text(format.format_number(data[10]) + '元');

        $("#amplitude").text(((data[5] - data[6]) / data[1] * 100).toFixed(2) + "%");

        $("#turnover").text((basicData.stockBasicInfo.outstanding) ?
            (((data[9] / basicData.stockBasicInfo.outstanding) * 100).toFixed(2) + "%") : " --");

        $('#buy-1-price').text(data[11]);
        $('#buy-2-price').text(data[12]);
        $('#buy-3-price').text(data[13]);
        $('#buy-4-price').text(data[14]);
        $('#buy-5-price').text(data[15]);
        $('#buy-1-amount').text(data[16] / 100);
        $('#buy-2-amount').text(data[17] / 100);
        $('#buy-3-amount').text(data[18] / 100);
        $('#buy-4-amount').text(data[19] / 100);
        $('#buy-5-amount').text(data[20] / 100);
        $('#sell-1-price').text(data[21]);
        $('#sell-2-price').text(data[22]);
        $('#sell-3-price').text(data[23]);
        $('#sell-4-price').text(data[24]);
        $('#sell-5-price').text(data[25]);
        $('#sell-1-amount').text(data[26] / 100);
        $('#sell-2-amount').text(data[27] / 100);
        $('#sell-3-amount').text(data[28] / 100);
        $('#sell-4-amount').text(data[29] / 100);
        $('#sell-5-amount').text(data[30] / 100);

        var buy = data[16] + data[17] + data[18] + data[19] + data[20];
        var sell = data[26] + data[27] + data[28] + data[29] + data[30];
        var commission = buy - sell;
        var committee = commission / (buy + sell) * 100;

        var color_committee = commission > 0 ? 'red' : 'green';
        $('#committee').text(committee.toFixed(2) + '%').css('color', color_committee);
        $('#commission').text(commission).css('color', color_committee);

        var hq_time = new Date(data[34] * 1000);
        $("#time").text(hq_time.getFullYear() + "年" +
            format.format_time(hq_time.getMonth() + 1) + "月" +
            format.format_time(hq_time.getDate()) + "日 " +
            format.format_time(hq_time.getHours()) + ":" +
            format.format_time(hq_time.getMinutes()) + ":" +
            format.format_time(hq_time.getSeconds()));
    }
};

/**
 * 基本信息对象
 * @type {{stockBasicInfo: {name: string, code: string, industry: string, area: string, pe: number, outstanding: number, totals: number, totalAssets: number, liquidAssets: number, fixedAssets: number, reserved: number, reservedPerShare: number, eps: number, bvps: number, pb: number, timeToMarket: number}, getBasicData: basicData.getBasicData, show: basicData.show}}
 */
var basicData = {
    /**
     * 股票基本信息
     *
     * code;//股票代码
     * name;//股票公司名称
     * industry;//股票的类型
     * area;//股票的公司创建地
     * pe;//市盈率
     * outstanding;//流通股本
     * totals;//总股本(万)
     * totalAssets;//总资产(万)
     * liquidAssets;//流动资产
     * fixedAssets;//固定资产
     * reserved;//公积金
     * reservedPerShare;//每股公积金
     * eps;//每股收益
     * bvps;//每股净资
     * pb;//市净率
     * timeToMarket;//股票的上市日期
     * @type {{name: string, code: string, industry: string, area: string, pe: number, outstanding: number, totals: number, totalAssets: number, liquidAssets: number, fixedAssets: number, reserved: number, reservedPerShare: number, eps: number, bvps: number, pb: number, timeToMarket: number}}
     */
    stockBasicInfo: {
        name: '',
        code: '',
        industry: '',
        area: '',
        pe: 0,
        outstanding: 0,
        totals: 0,
        totalAssets: 0,
        liquidAssets: 0,
        fixedAssets: 0,
        reserved: 0,
        reservedPerShare: 0,
        eps: 0,
        bvps: 0,
        pb: 0,
        timeToMarket: 0
    },
    getBasicData: function () { // 获取基本信息
        jQuery.ajax({
            url: '/BasicComInfo',
            cache: false,
            dataType: 'json',
            data: 'codeNum=' + id,
            success: function (data) {
                basicData.stockBasicInfo = data;

                basicData.show();
            }
        })
    },
    show: function () { // 显示基本信息
        $('#name').text(this.stockBasicInfo.name);
        $('#name-2').text(this.stockBasicInfo.name);
        $('#type').text(this.stockBasicInfo.industry);
        $('#area').text(this.stockBasicInfo.area);
        $('#pe_ttm').text(this.stockBasicInfo.pe);
        $('#outstanding').text(this.stockBasicInfo.outstanding);
        $('#total').text(this.stockBasicInfo.totals);
        $('#totalAssets').text(this.stockBasicInfo.totalAssets);
        $('#marketValue').text(this.stockBasicInfo.totalAssets);
        $('#liquidAssets').text(this.stockBasicInfo.liquidAssets);
        $('#fixedAssets').text(this.stockBasicInfo.fixedAssets);
        $('#reserved').text(this.stockBasicInfo.reserved);
        $('#reservedPerShare').text(this.stockBasicInfo.reservedPerShare);
        $('#eps').text(this.stockBasicInfo.eps);
        $('#eps-2').text(this.stockBasicInfo.eps);
        $('#bvps').text(this.stockBasicInfo.bvps);
        $('#pb').text(this.stockBasicInfo.pb);
        $('#timeToMarket').text(basicData.formatTime(this.stockBasicInfo.timeToMarket));
    },
    /**
     * 格式化时间，将long型转换为'yy年MM月dd日'格式
     * @param time
     */
    formatTime: function (time) {
        var date = new Date(time);

        return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日';
    }
};

/**
 * 订单对象，包含
 *      逐笔数据、大单数据、分价数据、分时数据
 * @type {{getPerShareData: order.getPerShareData, getLargeOrderData: order.getLargeOrderData, getPriceSeriesData: order.getPriceSeriesData, getTimeSeriesData: order.getTimeSeriesData, showPerShareData: order.showPerShareData, showLargeOrderData: order.showLargeOrderData, showPriceSeriesData: order.showPriceSeriesData, showTimeSeriesData: order.showTimeSeriesData}}
 */
var order = {
    getPerShareData: function () { // 获取逐笔数据
        jQuery.ajax({
            url: '/PerStockInfo',
            type: 'post',
            data: 'codeNum=' + id,
            dataType: 'json',
            success: function (data) {

            }
        });
    },
    getLargeOrderData: function () { // 获取大单数据

    },
    getPriceSeriesData: function () { // 获取分价数据

    },
    getTimeSeriesData: function () { // 获取分时数据

    },
    showPerShareData: function (data) { // 显示逐笔数据

    },
    showLargeOrderData: function (data) { // 显示大单数据

    },
    showPriceSeriesData: function (data) { // 显示分价数据

    },
    showTimeSeriesData: function (data) { // 显示分时数据

    }
};

/**
 * 格式化对象
 * @type {{format_time: format.format_time, format_number: format.format_number}}
 */
var format = {
    /**
     * 格式化时间格式，不足两位的补0
     *      如： 3 --> 03
     * @param _time
     * @returns {*}
     */
    format_time: function (_time) {
        if (_time < 10
        ) {
            _time = "0" + _time;
        }
        return _time;
    },

    /**
     * 格式化数字，如：
     *      340000 --> 34万
     *      450000000 --> 4.5亿
     * @param _number
     */
    format_number: function (_number) {
        if (_number > 1e8) {
            return (_number / 1e8).toFixed(2) + '亿';
        } else if (_number > 1e4) {
            return (_number / 1e4).toFixed(2) + '万';
        }

        return _number;
    }
};

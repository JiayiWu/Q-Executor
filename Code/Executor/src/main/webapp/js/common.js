/**
 * Created by song on 16-7-31.
 *
 * 相关共享操作
 */

$(function () {
    isLogin();

    // 初始化背景
    initBackground();

    // 激活提示工具
    $('[data-toggle="tooltip"]').tooltip();
});

/**
 * 判断用户是否登录
 */
function isLogin() {
    jQuery.ajax({
        url: '/isLogin',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                afterLogin(data.object.nickName);
            }
        }
    });
}

/**
 * 登录后的操作
 */
function afterLogin(userName) {
    $('.navbar-right').empty().append(
        '<li class="dropdown">' +
        '<a href="#" class="dropdown-toggle" data-toggle="dropdown">' +
        userName + '<b class="caret"></b></a>' +
        '<ul class="dropdown-menu">' +
        '<li><a href="../page/account.html">个人中心</a></li>' +
        '<li><a href="../page/history.html">交易历史</a></li>' +
        '<li><a href="../page/message.html">我的消息</a></li>' +
        '<li class="divider"></li>' +
        '<li><a id="log-out" style="cursor: pointer;">登出</a></li>' +
        '</ul></li>'
    );

    $('#log-out').on('click', function (event) {
        event.preventDefault();
        logOut()
    });
}

/**
 * 登出
 */
function logOut() {
    jQuery.ajax({
        url: '/logout',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                afterLogOut();
            }
        }
    })
}

/**
 * 登出后的操作
 */
function afterLogOut() {
    $('.navbar-right').empty().append(
        '<li>' +
        '<a href="../page/login.html">登录</a>' +
        '</li>'
    ).append(
        '<li>' +
        '<a href="../page/signup.html">注册</a>' +
        '</li>'
    );
}

/**
 * 初始化背景
 */
function initBackground() {
    var background = $('#background');

    // 界面宽度，需考虑滚动条的宽度
    var width = $(document.body).height() + 70 >= window.innerHeight
        ? $(window).width() : window.innerWidth;
    // 界面高度，需考虑导航栏的高度
    var height = window.innerHeight - 50;

    // 背景图片自适应
    background.css({
        'width': width + 'px',
        'height': height + 'px'
    });

    // 固定背景
    $(document).on('scroll', function () {
        background.css({
            'top': window.scrollY + 50 + 'px'
        });
    });
}

/**
 * 获取url中的参数
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

/**
 * 固定侧边栏
 * @param sidebar
 */
function fix_sidebar(sidebar) {
    var jWindow = $(window);
    
    jWindow.scroll(function () {
        //noinspection JSValidateTypes
        var scrollHeight = jWindow.scrollTop();

        $(sidebar).css({
            position: "relative",
            top: scrollHeight + "px"
        });
    });
}

/**
 * 获取文本颜色，用于判断 涨/跌 等
 * @param num
 */
function get_color(num) {
    if (num > 0) {
        return 'red';
    } else if (num < 0) {
        return 'green';
    } else {
        return 'black';
    }
}
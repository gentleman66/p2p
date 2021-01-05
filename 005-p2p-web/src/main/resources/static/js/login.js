var referrer = "";//登录后返回页面
referrer = document.referrer;


if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}


//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});
//验证手机号
function blurPhone(){
	var falg=false
	var phone=$.trim($("#phone").val());
	if(phone==""){
		$("#showId1").html("手机号不能为空");
		/*showError("showId","手机号不能为空");*/
		return false;
	}else if(!/^1[1-9]\d{9}$/.test(phone)) {
		$("#showId1").html("手机号码格式有误，请重填");
		return false;
	}else {
		falg=true;
		return;
	}
	return falg;
}
function focusPhone() {
	$("#showId1").html("");
}
function focusPassword() {
	$("#showId2").html("");
}
function focusCapatcha() {
	$("#showId3").html("");
}
//验证密码
function blurPassword() {
	var falg=false;
	var password=$.trim($("#loginPassword").val());
	if(password=="") {
		$("#showId2").html("密码不能为空");
		return false;
	}else if(password.length<6||password.length>16){
		$("#showId2").html("密码应为 6-16 位");
		return false;
	}else if(!/^[0-9a-zA-Z]+$/.test(password)){
		$("#showId2").html("密码字符只可使用数字和大小写英文字母");
		return false;
	}else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(password)){
		$("#showId2").html("码应同时包含英文和数字");
		return false;
	}else {
		falg=true;
		return;
	}
	return falg;
}
//验证验证码
function blurCapatcha() {
	var captcha=$.trim($("#captcha").val());
	var falg=false;
	if(captcha==""){
		$("#showId3").html("验证码不能为空！");
		return false;
	}else {
		$.ajax({
			url: "/p2p/checkCaptcha",
			type: "post",
			data: {
				captcha: captcha
			},
			success: function (jsonObject) {
				if (jsonObject.msg == "操作成功") {
					$("#showId3").html("");
					falg=true;
					return;
				} else {
					$("#showId3").html(jsonObject.msg);
					return false;
				}
			},
			error: function () {
				$("#showId3").html("系统繁忙!");
				return false;
			}
		})
	}
	return falg;
}

//登录验证
function Login() {
	if(!blurCapatcha()&&blurPassword()&&blurPhone()){
		return;
	}
	var loginPassword=$.trim($("#loginPassword").val());
	var phone=$.trim($("#phone").val());
	var errorText1= $("#showId1").html();
	var errorText2= $("#showId2").html();
	var errorText3= $("#showId3").html();
	if(errorText1==""&&errorText2==""&&errorText3==""){
		$.ajax({
			url:"/p2p/login",
			type:"post",
			data:{
				phone:phone,
				loginPassword:$.md5(loginPassword)
			},
			success:function (data) {
				if (data.msg=="操作成功") {
					//跳转到上次页面
					window.location.href=referrer;
					return;
				}else {
					$("#showId3").html(data.msg);
					return;
				}
			},
			error:function () {
				$("#showId3").html("系统繁忙!");
				return;
			}
		});
	}

}

$(function () {
	//加载平台信息
	loadStat();
});

function loadStat() {
	$.ajax({
		url:"/p2p/loadStat",
		type:"get",
		success:function (jsonObject) {
			$(".queryHistoryAnnualRate").html(jsonObject.queryHistoryAnnualRate);
			$("#queryAllUserCount").html(jsonObject.queryAllUserCount);
			$("#queryAllBidMoney").html(jsonObject.queryAllBidMoney);
		}
	});
}













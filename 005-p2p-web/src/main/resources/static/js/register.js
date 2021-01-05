//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});

	$("#phone").on("blur",function () {
		var phone=$.trim($("#phone").val());
		if(phone==""){
			showError("phone","手机号不能为空");
			return;
		}else if(!/^1[1-9]\d{9}$/.test(phone)){
			showError("phone","手机号码格式有误，请重填");
			return;
		}else {
			$.ajax({
				//返回json
				url:"/p2p/checkPhoneUnique",
				type:"post",
				data:{
					phone:phone
				},
				success:function (data) {
					if(data.msg=="操作成功"){
						showSuccess("phone");
						return;
					}else {
						showError("phone",data.msg);
						return;
					}
				}
			});
		}
	})
	$("#phone").on("focus",function () {
		hideError("phone")
	});
	$("#loginPassword").on("blur",function () {
		var password=$.trim($("#loginPassword").val());
		var replaypassword=$.trim($("#repalyloginPassword").val());
		if(password==""){
			showError("loginPassword","密码不能为空");
			return;
		}else if(password.length<6||password.length>16){
			showError("loginPassword","密码应为 6-16 位");

		}else if(!/^[0-9a-zA-Z]+$/.test(password)){
			showError("loginPassword","密码字符只可使用数字和大小写英文字母")
		}else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(password)){
			showError("loginPassword","密码应同时包含英文和数字")
		}else {
			showSuccess("loginPassword");
		}
		//提示用户输入再次密码
		if(replaypassword!=password){
			showError("repalyloginPassword","两次密码输入不一致");
		}
	});

	//验证再次登录密码
	$("#repalyloginPassword").on("blur",function () {
		var password=$.trim($("#loginPassword").val());
		var replaypassword=$.trim($("#repalyloginPassword").val());
		if(password==""){
			showError("loginPassword","登录密码不能为空");
		}else if(replaypassword==""){
			showError("repalyloginPassword","再次登录密码不能为空");
		}else if(password!=replaypassword){
			showError("repalyloginPassword","两次密码不一致");
		}else {
			showSuccess("repalyloginPassword");
		}
	});

	$("#loginPassword").on("focus",function () {
		hideError("loginPassword");
	});
	$("#repalyloginPassword").on("focus",function () {
		hideError("repalyloginPassword");
	});

	//切换验证码
	$("#captcha").on("click",function () {
		this.src="/p2p/jcaptcha/captcha";
	});

	//发送手机短信
	$("#messageCodeBtn").on("click",function () {

		$("#phone").blur();
		$("#loginPassword").blur();
		var phone = $.trim($("#phone").val());
		//$("input[name$='letter']")
		var errorText = $("div[id$='Err']").text();

		if (errorText == "") {
			// 请求后台返回是否发送验证码成功
			$.ajax({
				url: "/p2p/messageCode",
				type: "post",
				data: {
					phone:phone
				},
				success: function (data) {
					if (data.msg == "操作成功") {
						if (!$("#messageCodeBtn").hasClass("on")) {
							$.leftTime(60, function (d) {
								if (d.status) {
									$("#messageCodeBtn").addClass("on");
									$("#messageCodeBtn").html(d.s == "00" ? "60秒后获取" : d.s + "秒后获取");

								} else {
									// 倒计时结束
									$("#messageCodeBtn").removeClass("on");
									$("#messageCodeBtn").html("获取验证码");
								}
							})
						}
					} else {
						showError("messageCodeErr", data.msg)
					}
				},
				error: function () {
					showError("messageCodeErr", "业务繁忙，请稍后重试")
				}

			})
		}
	});

	//验证手机短信
	$("#messageCode").on("blur",function () {
		//验证填写的手机验证码
		var MobileVerification=$.trim($("#messageCode").val());
		if(MobileVerification==""){
			showError("messageCode","请输入验证码")
			return;
		}
		$.ajax({
			url:"/p2p/verification",
			type:"post",
			data:{
				MobileVerification:MobileVerification
			},
			success:function (data) {
				if(data.msg=="操作成功"){
					showSuccess("messageCode");
					return;
				}else {
					showError("messageCode",data.msg);
					return;
				}
			},
			error:function () {
				showError("messageCode","业务繁忙，稍后再试");
			}
		});
	});

	/*$("#messageCode").on("blur",function () {
		var captcha=$.trim($("#messageCode").val());
		if(captcha==""){
			showError("messageCode","请输入验证码");
		}else {
			$.ajax({
				url:"/p2p/checkCaptcha" ,
				type:"post" ,
				data:{
					captcha:captcha
				},
				success:function (jsonObject) {
					if(jsonObject.msg=="操作成功"){
						showSuccess("messageCode");
					}else {
						showError("messageCode",jsonObject.msg);
					}
				}
			});
		}
	});*/

	// 注册按钮单击事件
	$("#btnRegist").on("click",function () {
		$("#phone").blur();
		$("#loginPassword").blur();
		$("#messageCode").blur();
		var phone = $.trim($("#phone").val());
		var loginPassword = $.trim($("#loginPassword").val());
		//$("input[name$='letter']")
		if(loginPassword!=""&&loginPassword!=null){
			$("#loginPassword").val($.md5(loginPassword));
		}

		var errorText = $("div[id$='Err']").text();
		if (errorText == "") {
			$.ajax({
				url:"/p2p/register",
				type:"post",
				data:{
					"phone":phone,
					"loginPassword":$.md5(loginPassword)
				},
				success:function (data) {
					if (data.msg=="操作成功") {

						window.location.href="/p2p/realName"

					}else {
						showError("loginPassword","注册失败!");
					}
				},
				error:function () {
					showError("loginPassword","系统繁忙!");
				}
			});
		}
	});

});


//同意实名认证协议
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
	//验证真实姓名只能输入中文
	$("#realName").on("blur",function () {
		var name=$.trim($("#realName").val());
		if(name==""){
			showError("realName","名字不能为空！");
		}else if(!/^[\u4e00-\u9fa5]{0,}$/.test(name)){
			showError("realName","名字必须是中文！");
		}else {
			showSuccess("realName");
		}
	});

	$("#realName").on("focus",function () {
		hideError("realName");
	});

	//验证身份证号码格式
	$("#idCard").on("blur",function () {
		var idCard=$.trim($("#idCard").val());
		if(idCard==""){
			showError("idCard","身份号码不能为空");
		}else if(idCard.length<15||idCard.length>18){
			showError("idCard","身份证号码应为 15 或 18 位");
		}/*else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(name)){
			showError("idCard","身份证号码格式不对");
		}*/
		else {
			$.ajax({
				url:"/p2p/checkIdCardUnique",
				type:"post",
				data:{
					idCard:idCard
				},
				success:function (data) {
					if(data.msg=="操作成功"){
						showSuccess("idCard");
						return;
					}else {
						showError("idCard",data.msg);
					}
				}
			});
			showSuccess("idCard");
		}
	});
	$("#idCard").on("focus",function () {
		hideError("idCard");
	});
	//图形验证码
		$("#captcha").on("blur",function () {
			var captcha=$.trim($("#captcha").val());

			if(captcha==""){
				showError("captcha","验证码不能为空！");
			}else {
				$.ajax({
					url: "/p2p/checkCaptcha",
					type: "post",
					data: {
						captcha: captcha
					},
					success: function (jsonObject) {
						if (jsonObject.msg == "操作成功") {
							showSuccess("captcha");

						} else {
							showError("captcha", jsonObject.msg);

						}
					},
					error: function () {
						showError("captcha", "系统繁忙!");
					}
				})
			}
	});

		$("#captcha").on("focus",function () {
			hideError("captcha");
		});
		//点击图片刷新
		$("#picture").on("click",function () {
			this.src="jcaptcha/captcha";
		});
	//认证按钮单击事件
	$("#btnRegist").on("click",function () {
		$("#realName").blur();
		$("#idCard").blur();
		var realName = $.trim($("#realName").val());
		var idCard = $.trim($("#idCard").val());
		//$("input[name$='letter']")
		var errorText = $("div[id$='Err']").text();
		if (errorText == "") {
			$.ajax({
				url:"/p2p/authentication",
				type:"post",
				data:{
					realName:realName,
					idCard:idCard
				},
				success:function (data) {
					if (data.msg=="操作成功") {

						window.location.href="/p2p/realName"

					}else {
						showError("idCard","认证失败!");
					}
				},
				error:function () {
					showError("idCard","系统繁忙!");
				}
			});
		}


	})
});
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
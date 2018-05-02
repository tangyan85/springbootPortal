(function ($) {

	$.fn.serializeJson = function () {
		var jsonData1 = {};
		var ingoreTarget = $("#jiraStep")[0].dataset.ingoreTarget;
		var jiraData = [];
		var jiraStepData = {};

		if (!isEmpty(ingoreTarget)) {
			var target = $("#jiraStep").find("#" + ingoreTarget).val();
			if (!isEmpty(target)) {
				$("#jiraStep").find("input,textarea,select").each(function (index, domEle) {
					var ingore = $(this).data("ingore");
					if (ingore && ingore == true) {

					} else {
						jiraStepData[this.name] = $(this).val();
					}

				});
			}
		}
		jiraData.push(jiraStepData);
		jsonData1["jiraProjects"] = jiraData;

		$("#projectStep").find("input,textarea,select").each(function (index, domEle) {
			jsonData1[this.name] = $(this).val();
		});

		$("table").each(function (index, domEle) {
			var target = "" || $(domEle).data("target");
			var targetInput = $("#" + target);
			var table = tableToJson2($(domEle));
			jsonData1[target] = table;
		});

		return JSON.stringify(jsonData1);
	};
})(jQuery);

function btnSumbit(type) {
	debugger;
	var flag = false;
	$("#projectMemberTable>.list-item_copy>tr").each(function () {
		var role = jQuery(this).find("#role").children('option:selected').val();
		var username = jQuery(this).find("#username").val();
		if (!isEmpty(role) && role == 'PM' && !isEmpty(username)) {
			teamleader = username;
			flag = true;
			return false;
		}
		if (flag) {
			return false;
		}
	});
	if (!flag) {
		jQuery.alert("至少添加一个项目经理！");
		return flag;
	}
	

	$("#teamleader").val(teamleader);

	var jsondata = $("#projectForm").serializeJson(); //自动将form表单封装成json
	var successMsg="创建成功！";

	var url = "/project/toAddProjectX";
	if (type == "editProject") {
		url = "/project/toEditProject";
		successMsg="修改成功！";
	}
	console.log(jsondata);
	jQuery.confirm({
		title: '请确认',
		// content: '<div style="text-align:center">确认终止项目？</div>',
		content: '<div style="text-align:center">确认是否提交项目信息？</div>',
		buttons: {
			confirm: {
				text: '确认',
				action: function () {
					jQuery.ajax({
						type: "POST",
						url: url,
						contentType: 'application/json;charset=utf-8', //设置请求头信息
						dataType: "json",
						data: jsondata,
						success: function (msg) {
							if (msg.responseCode == "0000") {
								jQuery.alert(successMsg);
								$("#mainframe", parent.document.body).attr("src", "project/findAllProjects/view");
								//top.location.href = "/index";
							} else {
								jQuery.alert("错误: " + msg.responseMsg);
							}
						}
					});
				}
			},
			cancel: {
				text: '取消'
			}
		}
	});


};


$(document).on('click', '.add', function () {
	var targetDom = $(this).parent().parent().find(".list-item_copy:first");
	var _item = targetDom.children(":last").clone();
	console.log($(this).val())
	targetDom.append(_item);
	targetDom.children(":last").find("input").each(function () {
		$(this).attr("disabled", false);
		$(this).val("");
	});
	targetDom.children(":last").find("select").each(function () {
		var name = this.name;
		if (!isEmpty(name) && name == "inputActionType") {
			if (this.options && this.options[1].value == "UPDATE_OR_NOTHING") {
				this.options.remove(1);
			}
		}
		$(this).attr("disabled", false);
		$(this).get(0).options[0].selected = true;
	})
	targetDom.children(":last").find("textarea").each(function () {
		$(this).attr("disabled", false);
		$(this).val("");
	})
	return false;
});

$(document).on('click', '.del', function () {
	debugger;
	var count = $(this).parent().parent().parent().find("tr").length;
	var id = $(this).attr("value");
	if (id == null || id == "" || id == "undefind") {
		if (count <= 1) {
			jQuery.alert("需至少存在一项!");
			return false;
		} else {
			$(this).parent().parent().remove();
			return false;
		}
	} else {

	
	}
	return false;
});
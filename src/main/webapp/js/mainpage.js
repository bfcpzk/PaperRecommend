function submitTogether(){
	var hide = $("input[id^='hide_']");
	var res = "";
	var iter = $("#iter").val();
	hide.each(function(index, item){
		res += $(item).val();
	});
	if(res == ""){
		$("#warn_info").html("<p><h4>请对至少一个文献进行打分！</h4></p>");
		$("#warn_info").fadeIn(1000);
		$("#warn_info").fadeOut(1000);
	}else{
		if(iter <= 3){
			$("#score_result").val(res);
			$("#feed_back").show();
		}else{
			$.ajax({
				type:"get",//请求类型
				contentType:"application/json;charset=utf-8",//发往服务端的数据类型
				url:"mayLoveList.do",
				data:{},
				dataType:"json",
				success : function(msg) {
					if(msg){//获取成功
						$("#rec_res").show();
						$("#t_body").empty();
						$.each(msg, function (i, item) {
							var $str1 = "<tr><td id='id_" + item.paper_id + "'>" + item.paper_id + "</td><td id='title_" + item.paper_id + "'>" + item.article_title + "</td>";
							$("#t_body").append($str1);
						});
						var str = "<tr><td colspan='2' style='text-align: center;'><a style='text-align:center' id='back' href='javascript:void(0)' onclick='toIndex()'' class='btn btn-info'>确认</a></td></tr>";
						$("#t_body").append(str);
					}else{
						alert("获取失败！");
					}
				}
			});//end ajax
		}
	}
}

function feedBack(){
	var res = $("#score_result").val();
	var iter = $("#iter").val();
	var time = $("#timeLevel").val();
	var length = $("#lengthLevel").val();
	var cite = $("#citeLevel").val();
	if(time == "" || length == "" || cite == ""){
		$("#warn_info").html("<p><h4>请填写您的阅读偏好！</h4></p>");
		$("#warn_info").fadeIn(1000);
		$("#warn_info").fadeOut(1000);
	}else{
		window.location.href="recommendBasedOnScoreLda.do?score=" + res + "&iter=" + iter + "&time=" + time + "&length=" + length + "&cite=" + cite;
	}
}

function chooseRelativity(id, tag){
	if(tag == -1.0){
		$("#res_"+id).html("<p style='color:red'>完全不相关</p><input type='hidden' id='hide_" + id +"' value='" + id + "," + -1.0 + ";'>");
	}else if(tag == -0.5){
		$("#res_"+id).html("<p style='color:indianred'>不太相关</p><input type='hidden' id='hide_" + id +"' value='" + id + "," + -0.5 + ";'>");
	}else if(tag == 0.0){
		$("#res_"+id).html("<p style='color:deepskyblue'>说不清</p><input type='hidden' id='hide_" + id +"' value='" + id + "," + 0.0 + ";'>");
	}else if(tag == 0.5){
		$("#res_"+id).html("<p style='color:yellowgreen'>有点相关</p><input type='hidden' id='hide_" + id +"' value='" + id + "," + 0.5 + ";'>");
	}else if(tag == 1.0){
		$("#res_"+id).html("<p style='color:green'>很相关</p><input type='hidden' id='hide_" + id +"' value='" + id + "," + 1.0 + ";'>");
	}

}

function chooseTime(tag, level){
	$("#timeLevel").val(level);
	$("#timeRes").html(tag);
}

function chooseLength(tag, level){
	$("#lengthLevel").val(level);
	$("#lengthRes").html(tag);
}

function chooseCite(tag, level){
	$("#citeLevel").val(level);
	$("#citeRes").html(tag);
}

function chooseKeywords(kid, keywords){
	if($("#button_"+kid).attr("class") == "btn btn-danger"){
		var str = "<a id='button_" + kid + "' href='javascript:void(0)' onclick='chooseKeywords(" + kid + ", &quot;" + keywords + "&quot;)' class='btn btn-success'>" + keywords + "</a>";
		$("#" + kid).empty();
		$("#" + kid).html(str);
	}else{
		var str = "<a id='button_" + kid + "' href='javascript:void(0)' onclick='chooseKeywords(" + kid + ", &quot;" + keywords + "&quot;)' class='btn btn-danger'>" + keywords + "</a>";
		$("#" + kid).empty();
		$("#" + kid).html(str);
	}
}

function submitKeywords(){
	var subKey = $("a[class='btn btn-success']");
	var res = "";
	subKey.each(function(index, item){
		res += $(item).text() + "@";
	});
	var time = $("#timeLevel").val();
	var length = $("#lengthLevel").val();
	var cite = $("#citeLevel").val();
	if(time == "" || length == "" || cite == ""){
		$("#warn_info").html("<p><h4>请填写您的阅读偏好！</h4></p>");
		$("#warn_info").fadeIn(1000);
		$("#warn_info").fadeOut(1000);
	}else{
		window.location.href="recommendBasedOnKeywords.do?keywordlist=" + res + "&time=" + time + "&length=" + length + "&cite=" + cite;
	}
}

function toMainPage(){
	window.location.href="toWelcomePaper.do";
}

function toIndex(){
	$("#rec_res").hide();
	window.location.href="logOut.do";
}
if (window.console) {
}


var XML_CHAR_MAP = {
	'<' : '&lt;',
	'>' : '&gt;',
	'&' : '&amp;',
	'"' : '&quot;',
	"'" : '&apos;'
};

function escapeXml(s) {
	return s.replace(/[<>&"']/g, function(ch) {
		return XML_CHAR_MAP[ch];
	});
}
function validInputSearch(strData) {
	var result = strData.replace(/`/g, "");
	return result;
}

//Format length of text
function formatLengthJson(stringData){
	if(stringData.length > 15){
		return stringData.substring(0, 15) + '...';
	}
	return stringData;
}

function viewMoreResult(event){
	event.preventDefault();
	var keyword = $("#search").val();
	keyword = validInputSearch(keyword);
	window.location.href = "/student?page=1&keyword=" + keyword;
}

function searchStudentAjax(){
	clearTimeout($(this).data("timer"));
	$("#search-result").html("");
	var keyword = $("#search").val();
	keyword = validInputSearch(keyword);
	if(keyword === ""){
		$(".search-result.col-md-5.col-md-push-6").removeClass("show");
		$("img#loading").removeClass("active");	
	}else{
		$("img#loading").addClass("active");
		$(this).data("timer", setTimeout(function () {
			$.ajax({
				method : "GET",
			/*	headers : {
					'Authorization' : token
				},*/
				url : "/api/student/getStudentByKeyword?keyword="+keyword,
				success: function(result){
					var strResult = '';
					if(result.length > 0){
						$.each(result, function(index,value){
							strResult += "<tr>"
											+"<td class='text'>"+formatLengthJson(escapeXml(value.name))+"</td>"
											+"<td class='text'>"+formatLengthJson(escapeXml(value.code))+"</td>"
											+"<td class='text'>"+formatLengthJson(escapeXml(value.address))+"</td>"
										+"</tr>"
						});
					}else{
						strResult = "<p class='text-center'>Không tìm thấy dữ liệu</p>"
					}
					$("#search-result").html(strResult);
					$(".search-result.col-md-5.col-md-push-6").addClass("show");
				}
			});
			$("img#loading").removeClass("active");	
		},1000));
	}
}
if (window.console) {
	
  console.log("Welcome to your Play application's JavaScript!");
  changeActiveNav();
  
 /* $.ajax({
		method : "GET",
		url : "/demo/indexComet",
		success: function(result){
		}
	});*/
}
$(document).mouseup(function(e) {
	var container = $(".search-result.col-md-5.col-md-push-6");
	if (!container.is(e.target) && container.has(e.target).length === 0) {
		container.removeClass("show");
	}
});

function readURL(input) {

  if (input.files && input.files[0]) {
    var reader = new FileReader();

    reader.onload = function(e) {
      $('#blah').attr('src', e.target.result);
    }

    reader.readAsDataURL(input.files[0]);
  }
}

$("#imgInp").change(function() {
  readURL(this);
});


function changeActiveNav() {
	$(".nav.navbar-nav li").each(function(index){
		$(this).removeClass("active");
	});
	 if (window.location.pathname.indexOf("user") > 0) {
		$(".nav.navbar-nav li:nth-child(1)").addClass("active")
	} else if (window.location.pathname.indexOf("student") > 0) {
		$(".nav.navbar-nav li:nth-child(2)").addClass("active")
	}else if(window.location.pathname.indexOf("class") > 0){
		$(".nav.navbar-nav li:nth-child(3)").addClass("active")
	}
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
	$("img#loading").addClass("active");
	$(this).data("timer", setTimeout(function () {
		$.ajax({
			method : "GET",
			url : "/api/student/getStudentByKeyword?keyword="+keyword,
			success: function(result){
				var strResult = '';
				if(result.length == 0){
					$.each(result, function(index,value){
						strResult += "<tr>"
										+"<td class='text'>"+formatLengthJson(escapeXml(value.id))+"</td>"
										+"<td class='text'>"+formatLengthJson(escapeXml(value.username))+"</td>"
										+"<td class='text'>"+formatLengthJson(escapeXml(value.password))+"</td>"
										+"<td class='text'>"+formatLengthJson(escapeXml(value.status))+"</td>"
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
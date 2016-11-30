/**
 * 
 */
(function(){
	String.prototype.format = function(args) {
		if (arguments.length > 0) {
			var result = this;
			if (arguments.length == 1 && typeof (args) == "object") {
				for ( var key in args) {
					var reg = new RegExp("({" + key + "})", "g");
					result = result.replace(reg, args[key]);
				}
			} else {
				for (var i = 0; i < arguments.length; i++) {
					if (arguments[i] == undefined) {
						return "";
					} else {
						var reg = new RegExp("({[" + i + "]})", "g");
						result = result.replace(reg, arguments[i]);
					}
				}
			}
			return result;
		} else {
			return this;
		}
	} 
	window.ctx=(function(){
		var local = window.location;  //http://www.w3school.com.cn/js/js_window_location.asp
	    var contextPath = local.pathname.split("/")[1];  // /js/js_window_location.asp
	    var basePath = local.protocol+"//"+local.host/*+":"+local.port*/+"/"+contextPath;
	    return basePath;
	})();
	window.submitParams=function(url,method,data){
		var form='<form id="{id}" action="{url}" method="{method}">{inputs}</form>';
		if (method=="get"||method=="GET"||method=="Get")
			for(e in data){
				data[e]=encodeURIComponent(data[e]);
			}
		var inputsTemplate="<input type=\"hidden\" name=\"{name}\" value=\"{value}\"/>";
		var inputs="";
		if (!!data) for(e in data){
			inputs+=(inputsTemplate.format({name:e,value:data[e]}));
		}
		var id=encrypt(new Date());
		form=form.format({id:id,url:url,method:method,inputs:inputs});
		document.body.innerHTML+=(form);
		document.getElementById(id).submit();
	};
	jQuery.ajaxSetup({
	    contentType : "application/x-www-form-urlencoded;charset=utf-8",
	    complete : function(XMLHttpRequest, textStatus) {
	        var sessionstatus = XMLHttpRequest.getResponseHeader("SESSION_STATUS"); // 通过XMLHttpRequest取得响应头，sessionstatus，
	        if (sessionstatus == "TIME_OUT") {
	            // 如果超时就处理 ，指定要跳转的页面
	            window.location.href=ctx+"/login";
	        }
	    }
	}); 
})();


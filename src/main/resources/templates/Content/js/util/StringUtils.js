//将url中拼接的空条件去除
function serializeNotNull(serStr) {
	var result = "";
	var params = serStr.split("&");
	for (i = 0; i < params.length; i++) {
		var param = params[i].split("=");
		if (param[1].length > 0) {
			result += params[i] + "&";
		}
	}
	if (result.lastIndexOf("&") > 0) {
		result = result.substring(0, result.lastIndexOf("&"));
	}
	return result;
}
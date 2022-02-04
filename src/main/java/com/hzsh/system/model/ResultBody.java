package com.hzsh.system.model;

import java.util.HashMap;
import java.util.Map;

public class ResultBody {

	private String statusCode;// 结果状态码
	private String message;// 返回结果提示信息
//	private Map<String, Object> resultMap;// 其他数据

	/*public void put(String key, Object value) {
		if (resultMap == null)
			resultMap = new HashMap<String, Object>();
		resultMap.put(key, value);
	}*/

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
*/
}

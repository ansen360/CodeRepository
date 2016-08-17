package com.tomorrow_p.common;

import java.util.HashMap;
import java.util.Map;

public class HttpErrorUtils {
	public static String STR = "202:Accepted&502:Bad Gateway&405:Bad Method&400:Bad Request&408:ClientTimeout&409:Conflict&201:Created&413:Entity too large&403:Forbidden&504:Gateway timeout&410:Gone&500:Internal error&411:Length required&301:Moved permanently&302:Moved temporarily&300:Multiple choices&406:Not acceptable&203:Not authoritative&404:Not found&501:Not implemented&304:Not modified&204:No content&200:OK&206:Partial&402:Payment required&412:Precondition failed&407:Proxy authentication required&414:Request too long&205:Reset&303:See othe&401:Unauthorized&503:Unavailable&415:Unsupported type305:Use proxy&505:Version not supported";

	public static Map<String, String> getHttpCodeMap() {
		Map<String, String> httpCodeMap = new HashMap<String, String>();
		String[] str = STR.split("&");
		for (int i = 0; i < str.length; i++) {
			httpCodeMap.put(str[i].substring(0, str[i].indexOf(":")), str[i].substring(str[i].indexOf(":") + 1));
		}
		return httpCodeMap;
	}
}

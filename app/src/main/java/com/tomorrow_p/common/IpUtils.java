package com.tomorrow_p.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
	/**根据域名得到ip
	 * @param url
	 * @return
	 */
	public static String getServerIP(String url) {
		try {
			InetAddress myIPaddress = InetAddress.getByName(url);
			String hostAddress = myIPaddress.getHostAddress();
			return hostAddress;
		} catch (UnknownHostException e) {
		}
		return null;
	}
}

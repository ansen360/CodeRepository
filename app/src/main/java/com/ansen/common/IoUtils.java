package com.ansen.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IoUtils {
	/**
	 * @param is
	 * @return inputStream到String
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream is) throws IOException {
		try {
			if (is != null) {
				StringBuilder sb = new StringBuilder();
				String line;
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gbk"));
					// BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					while ((line = reader.readLine()) != null) {
						// sb.append(line);
						sb.append(line).append("\n");
					}
				} finally {
					is.close();
				}
				return sb.toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
}

package com.allarphoto.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TinyUrl {

	String origUrl, tinyUrl;

	public TinyUrl(String link) {
		origUrl = link;
		try {
			URL url = new URL("http://tinyurl.com/create.php");
			URLConnection connect = url.openConnection();
			connect.setReadTimeout(5000);
			connect.setDoOutput(true);
			BufferedOutputStream out = new BufferedOutputStream(connect
					.getOutputStream());
			out.write(new String("url=" + link).getBytes("utf-8"));
			out.close();
			connect.connect();
			String result = getString(connect.getInputStream(),
					"[<a href=\"http://tinyurl.com/");
			int start = result.indexOf("[<a href=\"http://tinyurl.com/") + 10;
			int end = result.indexOf("\"", start);
			if (start > -1 && end > -1) {
				tinyUrl = result.substring(start, end);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return (tinyUrl != null) ? tinyUrl : origUrl;
	}

	private String getString(InputStream in, String till) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(till) > -1)
					return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}

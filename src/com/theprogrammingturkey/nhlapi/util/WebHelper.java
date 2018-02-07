package com.theprogrammingturkey.nhlapi.util;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebHelper
{
	private static final JsonParser PARSER = new JsonParser();

	public static JsonObject makeRequest(String link) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(link).openConnection();
		con.setDoInput(true);
		con.setReadTimeout(5000);
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");

		((HttpURLConnection) con).setRequestMethod("GET");
		con.setConnectTimeout(5000);

		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		int responseCode = con.getResponseCode();

		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
			System.err.println("Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
			throw new Exception();

		StringBuilder buffer = new StringBuilder();
		int chars_read;
		while((chars_read = in.read()) != -1)
			buffer.append((char) chars_read);

		return PARSER.parse(buffer.toString()).getAsJsonObject();
	}

	public enum RequestType
	{
		GET, POST, PUT, DELETE, UPDATE, PATCH;
	}
}

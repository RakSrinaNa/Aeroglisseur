package fr.vaucanson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class Sender
{
	public final static String IP = "127.0.0.1";
	private final static String IP2 = "http://127.0.0.1/";

	public static boolean init() throws IOException
	{
		final InetAddress inet = InetAddress.getByName(IP);
		return inet.isReachable(5000) ? true : false;
	}

	public static String send(String urlParameters)
	{
		System.out.println("Envoi de la requete : " + urlParameters);
		URL url;
		HttpURLConnection con = null;
		try
		{
			url = new URL(IP2);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			con.setRequestProperty("Content-Language", "fr-FR");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			final InputStream is = con.getInputStream();
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			final StringBuffer response = new StringBuffer();
			while((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString().contains("Parse error: syntax error") ? "Error" : response.toString().replace("﻿ ", "").replace("﻿", "").replace("<br />", "\n").replace("</p>", "").replace("<p>", "");
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(con != null)
				con.disconnect();
		}
	}
}

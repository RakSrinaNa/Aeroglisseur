package fr.vaucanson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class Sender implements Runnable
{
	public final static String IP = "www.mrcraftcod.fr";
	private final static String IP2 = "http://www.mrcraftcod.fr/PI/index.php";
	private static String req;

	public Sender(String req)
	{
		this.req = req;
	}
	
	public static boolean init() throws IOException
	{
		System.setProperty("http.proxyHost", "proxy");
		System.setProperty("http.proxyPort", "8080");
		final InetAddress inet = InetAddress.getByName(IP);
		return inet.isReachable(5000) ? true : false;
	}

	synchronized public static String send(String urlParameters)
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

	@Override
	public void run() 
	{
		send(this.req);
	}
}

package fr.vaucanson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sender extends Thread
{
	public final static String IP = "www.mrcraftcod.fr";
	private final static String IP2 = "http://www.mrcraftcod.fr/PI/index.php";
	private static Map<String, Integer> reqs;
	private static Map<String, Integer> reqsSended;
	private static final String[] keys = {"or", "st", "vi"};

	public Sender()
	{
		System.out.println("Creating sender...");
		this.setName("Sender");
		reqs = new HashMap<String, Integer>();
		reqs.put("or", 5);
		reqs.put("vi", 0);
		reqs.put("st", 0);
		reqsSended = new HashMap<String, Integer>();
		reqsSended.put("or", 5);
		reqsSended.put("vi", 0);
		reqsSended.put("st", 0);
		System.out.println("Sender created!");
	}
	
	private static boolean init() throws Exception
	{
		System.out.println("Initializing sender...");
		final InetAddress inet = InetAddress.getByName(IP);
		if(inet.isReachable(5000))
			return true;
		System.out.println("Proxy ins needed, configuring it");
		System.setProperty("http.proxyHost", "proxy");
		System.setProperty("http.proxyPort", "8080");
		if(inet.isReachable(5000))
			return true;
		throw new Exception("Couldn't connect to the server!");
	}

	synchronized private static String send(String urlParameters)
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
		try
        {
	        init();
	        System.out.println("Sender initialized!");
        }
        catch(Exception e)
        {
	        e.printStackTrace();
	        return;
        }
		while(!Thread.interrupted())
		{
			try
			{
				Thread.sleep(50);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			for(String key : keys)
			{
				Map temp = reqs;
				Map temp2 = reqsSended;
				if(reqs.get(key) != reqsSended.get(key))
				{
					System.out.println(Outils.decrypt(send("#" + key + "=" + reqs.get(key))));
					reqsSended.put(key, reqs.get(key));
				}
			}
		}
	}
	
	synchronized public static void addToSend(String key, int value)
	{
		System.out.println("Changing " + key + " to " + value);
		reqs.put(key, value);
	}
	
	public static int getKeyValue(String key)
	{
		return reqs.get(key);
	}
}

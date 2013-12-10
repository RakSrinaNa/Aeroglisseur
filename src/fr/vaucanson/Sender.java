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
	public final static String IP_PING = "127.0.0.1";//"www.mrcraftcod.fr";
	private final static String IP_URL = "http://127.0.0.1/PI/index.html";//"http://www.mrcraftcod.fr/PI/index.php";
	private static Map<String, Integer> requestsSended;
	private static final String[] keys = {"or", "st", "vi"};

	private static boolean init() throws Exception
	{
		System.out.println("Initializing sender...");
		final InetAddress inet = InetAddress.getByName(IP_PING);
		if(inet.isReachable(5000))
			return true;
		System.out.println("Proxy ins needed, configuring it");
		System.setProperty("http.proxyHost", "proxy");
		System.setProperty("http.proxyPort", "8080");
		if(inet.isReachable(5000))
			return true;
		throw new Exception("Couldn't connect to the server!");
	}

	synchronized private static String send(final String key, final int value)
	{
		final String urlParameters = key + "=" + value;
		System.out.println("Envoi de la requete : #" + urlParameters);
		URL url;
		HttpURLConnection connection = null;
		try
		{
			url = new URL(IP_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "fr-FR");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			final DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(urlParameters);
			writer.flush();
			writer.close();
			final InputStream is = connection.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			final StringBuffer response = new StringBuffer();
			while((line = reader.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			reader.close();
			requestsSended.put(key, Interface.getRequests().get(key));
			return response.toString().contains("Parse error: syntax error") ? "Error" : response.toString().replace("﻿ ", "").replace("﻿", "").replace("<br />", "\n").replace("</p>", "").replace("<p>", "");
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(connection != null)
				connection.disconnect();
		}
	}

	public Sender()
	{
		System.out.println("Creating sender...");
		setName("Sender");
		requestsSended = new HashMap<String, Integer>();
		requestsSended.put("or", 5);
		requestsSended.put("vi", 0);
		requestsSended.put("st", 0);
		System.out.println("Sender created!");
	}

	@Override
	public void run()
	{
		try
		{
			init();
			System.out.println("Sender initialized!");
		}
		catch(final Exception e)
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
			catch(final Exception e)
			{
				e.printStackTrace();
			}
			for(final String key : keys)
				if(Interface.getRequests().get(key) != requestsSended.get(key))
					System.out.println(Outils.decrypt(send(key, Interface.getRequests().get(key))));
		}
	}
}

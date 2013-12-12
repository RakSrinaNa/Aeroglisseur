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
import java.util.logging.Level;

public class Sender extends Thread
{
	public static String IP_PING;
	private static String IP_URL;
	private static Map<String, Integer> requestsSended;
	private static final String[] keys = {"or", "st", "vi"};

	private static boolean init() throws Exception
	{
		Main.logger.log(Level.INFO, "Initializing sender...");
		final InetAddress inet = InetAddress.getByName(IP_PING);
		if(inet.isReachable(5000))
			return true;
		Main.logger.log(Level.WARNING, "Proxy is needed, configuring it");
		System.setProperty("http.proxyHost", "proxy");
		System.setProperty("http.proxyPort", "8080");
		if(inet.isReachable(5000))
			return true;
		throw new Exception();
	}

	synchronized private static String send(final String key, final int value)
	{
		final String urlParameters = key + "=" + value;
		Main.logger.log(Level.FINE, "Envoi de la requete : #" + urlParameters);
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

	public Sender(String IP1, String IP2) throws Exception
	{
		this.IP_PING = IP1;
		this.IP_URL = IP2;
		Main.logger.log(Level.INFO, "Creating sender...");
		setName("Sender");
		requestsSended = new HashMap<String, Integer>();
		requestsSended.put("or", 5);
		requestsSended.put("vi", 0);
		requestsSended.put("st", 0);
		init();
		Main.logger.log(Level.INFO, "Sender initialized on " + IP_PING + " sending requests to " + IP_URL + "!");
	}

	@Override
	public void run()
	{
		while(!Thread.interrupted())
		{
			try
			{
				Thread.sleep(50);
			}
			catch(final Exception e){}
			for(final String key : keys)
				if(Interface.getRequests().get(key) != requestsSended.get(key))
					Main.logger.log(Level.FINER, "Recieving response from server: " + Outils.decrypt(send(key, Interface.getRequests().get(key))));
		}
	}
}

package fr.vaucanson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

public class Sender extends Thread
{
	public static String IP_PING;
	private static String IP_URL;
	private static HashMap<String, Integer> requestsSended;
	private static final String[] keys = {"or", "st", "vi", "cv", "ch"};
	private static ArrayList<Long> times;

	/**
	 * Used to initialize the connection with the Arduino Yun
	 * 
	 * @return True if the connection is set
	 * @throws Exception If there were an error with the initialization
	 */
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
		throw new Exception("Can not connect to the Arduino");
	}

	/**
	 * Used to send a GET command to the Arduino
	 * 
	 * @param params The GET params to send
	 * @return The response by the server
	 * @throws Exception If there were an error with the connection
	 */
	synchronized private String sendGet(String key, String value) throws Exception
	{
		Date startDate = new Date();
		URL url = new URL(IP_URL + "arduino/" + key + "/" + value);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = connection.getResponseCode();
		Main.logger.log(Level.INFO, "\nSending 'GET' request to URL : " + url);
		Main.logger.log(Level.INFO, "Response Code : " + responseCode);
		BufferedReader inputSream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = inputSream.readLine()) != null)
			response.append(inputLine);
		inputSream.close();
		times.add((new Date().getTime() - startDate.getTime()));
		long average = 0;
		for(long l : times)
			average += l;
		Main.logger.log(Level.INFO, "Done in " + times.get(times.size() - 1) + " ms\tAverage: " + (average / times.size()));
		return response.toString();
	}

	/**
	 * Constructor
	 * 
	 * @param IP1 The IP for the ping test
	 * @param IP2 The URL for the Arduino internet control page
	 * @throws Exception If there were an error with the connection
	 */
	public Sender(String IP1, String IP2) throws Exception
	{
		Sender.IP_PING = IP1;
		Sender.IP_URL = IP2;
		Main.logger.log(Level.INFO, "Creating sender...");
		setName("Sender");
		times = new ArrayList<Long>();
		requestsSended = new HashMap<String, Integer>();
		requestsSended.put("or", 50);
		requestsSended.put("vi", 0);
		requestsSended.put("st", 0);
		requestsSended.put("ch", 50);
		requestsSended.put("cv", 50);
		init();
		Main.logger.log(Level.INFO, "Sender initialized on " + IP_PING + " sending requests to " + IP_URL + "!");
	}

	/**
	 * Check every 50ms if there is an information to send and send it
	 */
	@Override
	public void run()
	{
		while(!Thread.interrupted())
		{
			for(final String key : keys)
				if(Interface.getRequests().containsKey(key) && (Interface.getRequests().get(key) != requestsSended.get(key)))
				{
					int value = 0;
					try
					{
						value = Interface.getRequests().get(key);
						sendGet(key, String.valueOf(value));
					}
					catch(Exception e)
					{
						e.printStackTrace();
						continue;
					}
					requestsSended.put(key, value);
				}
		}
	}
}

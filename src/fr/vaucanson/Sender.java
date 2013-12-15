package fr.vaucanson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Sender extends Thread
{
	public static String IP_PING;
	private static String IP_URL;
	private static Map<String, Integer> requestsSended;
	private static final String[] keys = {"or", "st", "vi"};
	private static List<Long> times;

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

	synchronized private String sendGet(String params) throws Exception
	{
		Date start = new Date();
		URL url = new URL(IP_URL + "?" + params);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		times.add((new Date().getTime() - start.getTime()));
		long average = 0;
		for(long l : times)
			average += l;
		System.out.println("Done in " + times.get(times.size() - 1) + " ms\tAverage: " + (average/times.size()));
		return response.toString();
	}

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
		init();
		Main.logger.log(Level.INFO, "Sender initialized on " + IP_PING + " sending requests to " + IP_URL + "!");
	}

	@SuppressWarnings("cast")
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
				if((int)Interface.getRequests().get(key) != (int)requestsSended.get(key))
				{
					try
					{
						int value = Interface.getRequests().get(key);
						sendGet(key + "=" + value);
						requestsSended.put(key, value);
					}
					catch(Exception e)
		            {
			            Main.logger.log(Level.WARNING, "Error when contacting Arduino!", e);
		            }
				}
		}
	}
}

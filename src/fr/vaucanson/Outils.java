package fr.vaucanson;

import java.util.HashMap;
import java.util.Map;

public class Outils
{
	public static Map<String, String> decrypt(String get)
	{
		Map<String, String> m = new HashMap<String, String>();
		String[] lin = get.split("<br>");
		for(String s : lin)
			if(s.contains("#or=") || s.contains("#vi="))
				m.put(getValue(s), getTag(s));
		return m;
	}

	private static String getValue(String s)
    {
		return s.substring(s.indexOf('#') + 1, s.indexOf('='));
    }

	private static String getTag(String s)
    {
		return s.substring(s.indexOf('=') + 1);
    }
}

package fr.vaucanson;

import java.io.IOException;

public class Main
{
	public static void main(String[] args)
	{
		new Interface();
		try
        {
	        System.out.println("Commande ping vers " + Sender.IP + " r\351ussie? " + Sender.init());
        }
        catch(IOException e)
        {
	        e.printStackTrace();
	        return;
        }
	}
}

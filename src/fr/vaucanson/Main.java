package fr.vaucanson;

import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("Commande ping vers " + Sender.IP + " r\351ussie? " + Sender.init());
		new Interface();
	}
}

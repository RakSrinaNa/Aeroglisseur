package fr.vaucanson;

public class Main
{
	public static Sender sender;
	
	public static void main(String[] args) throws Exception
	{
		sender = new Sender();
		sender.start();
		new Interface();
	}
}

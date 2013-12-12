package fr.vaucanson;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
	public static Sender sender;
	public static Logger logger;
	
	public static void main(String[] args) throws SecurityException, IOException
	{
		final FileHandler fileTxt = new FileHandler(new File(".", "log.txt").getAbsolutePath(), true);
		fileTxt.setFormatter(new LogFormatter());
		fileTxt.setEncoding("ISO-8859-1");
		logger = Logger.getLogger("Aeroglisseur");
		logger.setLevel(Level.FINER);
		logger.addHandler(fileTxt);
		logger.log(Level.FINEST, "\n\n---------- Starting program ----------\n");
		new Interface();
		try
		{
			sender = new Sender("www.mrcraftcod.fr", "http://www.mrcraftcod.fr/PI/index.php");
			sender.start();
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, "Can't reach " + e.getMessage() + "\tTrying on local server...");
			try
            {
	            sender = new Sender("127.0.0.1", "http://127.0.0.1/PI/index.html");
	            sender.start();
            }
            catch(Exception e1)
            {
            	logger.log(Level.SEVERE, "Can't reach " + e.getMessage() + "\tClosing program!");
            	for(Frame f : Interface.getFrames())
            		f.dispose();
            }
		}
	}
}

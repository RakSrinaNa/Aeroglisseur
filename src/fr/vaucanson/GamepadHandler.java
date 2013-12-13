package fr.vaucanson;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class GamepadHandler extends Thread
{
	private static Controller controller;
	private static Map<Integer, Boolean> buttonsPressed;
	private static Map<Integer, Float> axisStatus, povStatus;
	
	public GamepadHandler() 
	{
		init();
	}
	
	/**
	 * BUTTONS:
	 * 0 -> A
	 * 1 -> B
	 * 2 -> X
	 * 3 -> Y
	 * 4 -> L1
	 * 5 -> R1
	 * 6 -> SELECT
	 * 7 -> START
	 * 8 -> L3
	 * 9 -> R3
	 * 
	 * AXIS:
	 * 0 -> Y Gauche
	 * 1 -> X Gauche
	 * 2 -> Y Droit
	 * 3 -> X Droit
	 * 
	 * POVX:
	 * 1 -> Bas
	 * 0 -> Milieu
	 * -1 -> Haut
	 * 
	 * POVY:
	 * 1 -> Droite
	 * 0 -> Milieu
	 * -1 -> Gauche
	 */
	@Override
	public void run()
	{
		init();
		while(!Thread.interrupted())
		{
			try
            {
	            Thread.sleep(50);
            }
            catch(InterruptedException e)
            {
	            e.printStackTrace();
            }
			controller.poll();
			for(int i = 0; i < buttonsPressed.size(); i++)
				if(buttonsPressed.get(i) != controller.isButtonPressed(i))
				{
					if(i == 0 && controller.isButtonPressed(i))
						Interface.changeValue("st", 0);
					System.out.println("Button " + i + " changed to " + controller.isButtonPressed(i));
				}
			for(int i = 0; i < axisStatus.size(); i++)
				if(axisStatus.get(i) != controller.getAxisValue(i))
					System.out.println("Axis " + i + " changed to " + controller.getAxisValue(i));
			if(controller.getAxisValue(2) != 0)
				Interface.changeValue("vi", (int)(-250 * controller.getAxisValue(2)));
			if(controller.getAxisValue(1) != 0)
				Interface.changeValue("or", (int)(5 * controller.getAxisValue(1)));
			if(povStatus.containsKey(0))
				if(povStatus.get(0) != controller.getPovX())
				{
					if(controller.getPovX() == -1.0)
						Interface.changeValue("or", -1);
					else if(controller.getPovX() == 1.0)
						Interface.changeValue("or", 1);
					System.out.println("POVX changed to " + controller.getPovX());
				}
			if(povStatus.containsKey(1))
				if(povStatus.get(1) != controller.getPovY())
				{
					if(controller.getPovY() == -1.0)
						Interface.changeValue("vi", 100);
					else if(controller.getPovY() == 1.0)
						Interface.changeValue("vi", -100);
					System.out.println("POVX changed to " + controller.getPovY());
				}
			for(int i = 0; i < controller.getButtonCount(); i++)
				buttonsPressed.put(i, controller.isButtonPressed(i));
			for(int i = 0; i < controller.getAxisCount(); i++)
				axisStatus.put(i, controller.getAxisValue(i));
			povStatus.put(0, controller.getPovX());
			povStatus.put(1, controller.getPovY());
		}
	}
	
	private static void init()
	{
		try
        {
	        Controllers.create();
        }
        catch(LWJGLException e)
        {
	        e.printStackTrace();
        }
		Controllers.poll();
		for(int i = 0; i < Controllers.getControllerCount(); i++)
		{
			controller = Controllers.getController(i);
			if(controller.getName().contains("Gamepad F310"))
				break;
		}
		System.out.println("Controller: " + controller.getName());
		while(controller.getAxisValue(0) != 0 || controller.getAxisValue(1) != 0 || controller.getAxisValue(2) != 0 || controller.getAxisValue(3) != 0)
			controller.poll();
		buttonsPressed = new HashMap<Integer, Boolean>();
		axisStatus = new HashMap<Integer, Float>();
		povStatus = new HashMap<Integer, Float>();
	}

	public static Controller getController()
    {
	    return controller;
    }

	public static void setController(Controller controller)
    {
	    GamepadHandler.controller = controller;
    }
}

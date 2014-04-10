package fr.vaucanson;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class GamepadHandler extends Thread
{
	private String ACT_SUSTENT, VALUE_ORIENTATION, VALUE_ORIENTATION_POV = "POVX", VALUE_SPEED, VALUE_SPEED_POV = "POVY", VALUE_CAM_OR_VERT, VALUE_CAM_OR_HOR;
	private final int CONF_ABSOLUTE = 0, CONF_RELATIVE = 1;
	private final int conf = CONF_RELATIVE;
	private Controller controller;
	private Map<String, Boolean> buttonsPressed;
	private Map<String, Float> axisStatus, povStatus;

	/**
	 * Constructor
	 */
	public GamepadHandler()
	{
		if(System.getProperty("os.name").toLowerCase().contains("win"))
		{
			ACT_SUSTENT = "Bouton 0";
			VALUE_CAM_OR_VERT = "Rotation Y";
			VALUE_CAM_OR_HOR = "Rotation X";
			VALUE_SPEED = "Axe Y";
			VALUE_ORIENTATION = "Axe X";
		}
		else
		{
			ACT_SUSTENT = "A";
		}
	}

	/**
	 * Used to initalize the controller
	 * 
	 * @return Nothing
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		Main.logger.log(Level.INFO, "Setting Gamepad Controller...");
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
			if(controller.getName().contains("Gamepad F310") || controller.getName().contains("Generic X-Box pad"))
				break;
		}
		if(controller.equals(null))
		{
			Main.logger.log(Level.SEVERE, "No gamepad found!");
			throw new Exception();
		}
		Main.logger.log(Level.FINE, "Controller: " + controller.getName() + "\tAxis: " + controller.getAxisCount() + "\tButtons: " + controller.getButtonCount());
		Main.logger.log(Level.FINE, "Waiting for joysticks to be set to the middle...");
		while(controller.getAxisValue(0) != 0 || controller.getAxisValue(1) != 0 || controller.getAxisValue(2) != 0 || controller.getAxisValue(3) != 0)
			controller.poll();
		buttonsPressed = new HashMap<String, Boolean>();
		for(int i = 0; i < controller.getButtonCount(); i++)
			buttonsPressed.put(controller.getButtonName(i), controller.isButtonPressed(i));
		axisStatus = new HashMap<String, Float>();
		for(int i = 0; i < controller.getAxisCount(); i++)
			axisStatus.put(controller.getAxisName(i), controller.getAxisValue(i));
		povStatus = new HashMap<String, Float>();
		povStatus.put("POVX", controller.getPovX());
		povStatus.put("POVY", controller.getPovX());
	}

	/**
	 * Checking the inputs of the controller
	 */
	@Override
	public void run()
	{
		try
		{
			init();
			Thread.sleep(450);
		}
		catch(Exception e)
		{
			return;
		}
		while(!Thread.interrupted())
		{
			try
			{
				Thread.sleep(50);
			}
			catch(InterruptedException e)
			{
				Main.logger.log(Level.WARNING, "Error when sleeping for the controller", e);
			}
			controller.poll();
			for(int i = 0; i < buttonsPressed.size(); i++)
				if(buttonsPressed.get(controller.getButtonName(i)) != controller.isButtonPressed(i))
				{
					if(controller.isButtonPressed(i))
						onButtonPressed(controller.getButtonName(i));
					else
						onButtonReleased(controller.getButtonName(i));
					buttonsPressed.put(controller.getButtonName(i), controller.isButtonPressed(i));
				}
			for(int i = 0; i < axisStatus.size(); i++)
				if(axisStatus.get(controller.getAxisName(i)) != controller.getAxisValue(i))
				{
					onAxisValueChange(controller.getAxisName(i), controller.getAxisValue(i), true);
					axisStatus.put(controller.getAxisName(i), controller.getAxisValue(i));
				}
				else
					onAxisValueChange(controller.getAxisName(i), controller.getAxisValue(i), false);
			if(controller.getPovX() != povStatus.get("POVX"))
			{
				onPovValueChange("POVX", controller.getPovX());
				povStatus.put("POVX", controller.getPovX());
			}
			if(controller.getPovX() != povStatus.get("POVX"))
			{
				onPovValueChange("POVY", controller.getPovY());
				povStatus.put("POVY", controller.getPovX());
			}
		}
	}

	/**
	 * Called when a button is pressed
	 * 
	 * @param name The button's name
	 * @return Nothing
	 */
	private void onButtonPressed(final String name)
	{
		Main.logger.log(Level.INFO, "Button " + name + " pressed");
		if(name.equals(ACT_SUSTENT))
			Interface.changeValue("st", -9999);
	}

	/**
	 * Called when a button is released
	 * 
	 * @param name The button's name
	 * @return Nothing
	 */
	private void onButtonReleased(final String name)
	{
		Main.logger.log(Level.INFO, "Button " + name + " released");
	}

	/**
	 * Called when an axis value changed
	 * 
	 * @param name The axis' name
	 * @param value The new value
	 * @return Nothing
	 */
	@SuppressWarnings("all")
	private void onAxisValueChange(final String name, final float value, boolean newValue)
	{
		if(newValue)
			Main.logger.log(Level.INFO, "Axis " + name + " modified to " + value);
		if(conf == CONF_ABSOLUTE)
		{
			if(name.equals(VALUE_ORIENTATION))
				Interface.setValue("or", value);
			else if(name.equals(VALUE_CAM_OR_VERT))
				Interface.setValue("cv", value);
			else if(name.equals(VALUE_CAM_OR_HOR))
				Interface.setValue("ch", value);
			else if(name.equals(VALUE_SPEED))
				Interface.setValue("vi", -value);
		}
		else if(conf == CONF_RELATIVE)
		{
			if(name.equals(VALUE_SPEED))
				Interface.changeValue("vi", (int) (-250 * value));
			else if(name.equals(VALUE_ORIENTATION))
				Interface.changeValue("or", (int) (5 * value));
			else if(name.equals(VALUE_CAM_OR_VERT))
				Interface.changeValue("cv", (int) (-5 * value));
			else if(name.equals(VALUE_CAM_OR_HOR))
				Interface.changeValue("ch", (int) (5 * value));
		}
	}

	/**
	 * Called when a POV axis value changed
	 * 
	 * @param name The POV axis name
	 * @param value The new value
	 * @return Nothing
	 */
	private void onPovValueChange(final String name, final float value)
	{
		Main.logger.log(Level.INFO, "POV " + name + " modified to " + value);
		if(name.equals(VALUE_ORIENTATION_POV))
		{
			Interface.changeValue("or", 10 * (int) value);
		}
		else if(name.equals(VALUE_SPEED_POV))
		{
			Interface.changeValue("vi", 100 * (int) value);
		}
	}
}

package fr.vaucanson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interface extends JFrame implements KeyListener
{
	private static Map<String, Integer> requests;
	private static final long serialVersionUID = 7231194594050358481L;
	private static final int VERSION = 1;
	private static JFrame frame;
	private static JSlider sliderSpeed, sliderOrientation, sliderSustentation;
	private static JPanel panelSliderSpeed, panelTextSpeed, panelSpeed, panelSliderOrientation, panelTextOrientation, panelOrientation, panelSliderSustentation, panelTextSustentation, panelSustentation;
	private static JLabel labelSpeed, labelOrientation, labelSustentation;

	synchronized public static void addToSend(final String key, final int value)
	{
		System.out.println("Changing " + key + " to " + value);
		requests.put(key, value);
	}
	
	public Interface()
	{
		requests = new HashMap<String, Integer>();
		requests.put("or", 5);
		requests.put("vi", 0);
		requests.put("st", 0);
		frame = new JFrame("Controleur de l'a\351roglisseur v" + VERSION);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(570, 110));
		frame.setResizable(true);
		frame.setAlwaysOnTop(true);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.addKeyListener(this);
		frame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowActivated(final WindowEvent e)
			{
			}

			@Override
			public void windowClosed(final WindowEvent e)
			{
			}

			@Override
			public void windowClosing(final WindowEvent e)
			{
				frame.dispose();
			}

			@Override
			public void windowDeactivated(final WindowEvent e)
			{
			}

			@Override
			public void windowDeiconified(final WindowEvent e)
			{
			}

			@Override
			public void windowIconified(final WindowEvent e)
			{
			}

			@Override
			public void windowOpened(final WindowEvent e)
			{
			}
		});
		panelSliderSpeed = new JPanel();
		panelSliderSpeed.setLayout(new BorderLayout());
		panelSliderSpeed.setBackground(Color.ORANGE);
		panelSliderSpeed.setPreferredSize(new Dimension(500, 30));
		panelSliderOrientation = new JPanel();
		panelSliderOrientation.setLayout(new BorderLayout());
		panelSliderOrientation.setBackground(Color.ORANGE);
		panelSliderOrientation.setPreferredSize(new Dimension(500, 30));
		panelSliderSustentation = new JPanel();
		panelSliderSustentation.setLayout(new BorderLayout());
		panelSliderSustentation.setBackground(Color.ORANGE);
		panelSliderSustentation.setPreferredSize(new Dimension(500, 30));
		panelSpeed = new JPanel();
		panelSpeed.setLayout(new BorderLayout());
		panelOrientation = new JPanel();
		panelOrientation.setLayout(new BorderLayout());
		panelSustentation = new JPanel();
		panelSustentation.setLayout(new BorderLayout());
		panelTextSpeed = new JPanel();
		panelTextSpeed.setLayout(new BorderLayout());
		panelTextSpeed.setBackground(Color.GREEN);
		panelTextSpeed.setPreferredSize(new Dimension(50, 5));
		panelTextOrientation = new JPanel();
		panelTextOrientation.setLayout(new BorderLayout());
		panelTextOrientation.setBackground(Color.GREEN);
		panelTextOrientation.setPreferredSize(new Dimension(50, 5));
		panelTextSustentation = new JPanel();
		panelTextSustentation.setLayout(new BorderLayout());
		panelTextSustentation.setBackground(Color.GREEN);
		panelTextSustentation.setPreferredSize(new Dimension(50, 5));
		labelSpeed = new JLabel();
		labelSpeed.setBackground(Color.GRAY);
		labelSpeed.setText("0");
		labelOrientation = new JLabel();
		labelOrientation.setBackground(Color.GRAY);
		labelOrientation.setText("Devant");
		labelSustentation = new JLabel();
		labelSustentation.setBackground(Color.GRAY);
		labelSustentation.setText("OFF");
		sliderSpeed = new JSlider();
		sliderSpeed.setValue(0);
		sliderSpeed.setMaximum(9225);
		sliderSpeed.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				addToSend("vi", sliderSpeed.getValue());
				labelSpeed.setText(String.valueOf(sliderSpeed.getValue()));
			}
		});
		sliderOrientation = new JSlider();
		sliderOrientation.setValue(1);
		sliderOrientation.setMaximum(2);
		sliderOrientation.addChangeListener(new ChangeListener()
		{
			private int transformDirToNumber(String text)
		    {
		        switch(text)
		        {
		        	case "Gauche":
		        		return 0;
		        	case "Devant":
		        		return 1;
		        	case "Droite":
		        		return 2;
		        	default:
		        		return -1;
		        }
		    }
			
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				if(transformDirToNumber(labelOrientation.getText()) != sliderOrientation.getValue())
					addToSend("or", sliderOrientation.getValue());
				switch(sliderOrientation.getValue())
				{
					case 0:
						labelOrientation.setText("Gauche");
					break;
					case 1:
						labelOrientation.setText("Devant");
					break;
					case 2:
						labelOrientation.setText("Droite");
					break;
				}
			}
		});
		sliderSustentation = new JSlider();
		sliderSustentation.setValue(0);
		sliderSustentation.setMaximum(1);
		sliderSustentation.addChangeListener(new ChangeListener()
		{
			private int transformDirToNumber(String text)
		    {
		        switch(text)
		        {
		        	case "OFF":
		        		return 0;
		        	case "ON":
		        		return 1;
		        	default:
		        		return -1;
		        }
		    }
			
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				if(transformDirToNumber(labelSustentation.getText()) != sliderSustentation.getValue())
					addToSend("st", sliderSustentation.getValue());
				switch(sliderSustentation.getValue())
				{
					case 0:
						labelSustentation.setText("OFF");
					break;
					case 1:
						labelSustentation.setText("ON");
					break;
				}
			}
		});
		panelSliderSpeed.add(sliderSpeed);
		panelTextSpeed.add(labelSpeed);
		panelSpeed.add(panelSliderSpeed, BorderLayout.WEST);
		panelSpeed.add(panelTextSpeed, BorderLayout.EAST);
		panelSliderOrientation.add(sliderOrientation);
		panelTextOrientation.add(labelOrientation);
		panelOrientation.add(panelSliderOrientation, BorderLayout.WEST);
		panelOrientation.add(panelTextOrientation, BorderLayout.EAST);
		panelSliderSustentation.add(sliderSustentation);
		panelTextSustentation.add(labelSustentation);
		panelSustentation.add(panelSliderSustentation, BorderLayout.WEST);
		panelSustentation.add(panelTextSustentation, BorderLayout.EAST);
		frame.add(panelSpeed, BorderLayout.NORTH);
		frame.add(panelOrientation, BorderLayout.CENTER);
		frame.add(panelSustentation, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

	@Override
    public void keyPressed(KeyEvent e)
    {
		/*
		System.out.println(e.getExtendedKeyCode());
		if(e.getExtendedKeyCode() == 37)
		{
			if(transformDirToNumber(labelOrientation.getText()) > 0)
			{
				sliderOrientation.setValue(transformDirToNumber(labelOrientation.getText()) - 1);
				addToSend("or", sliderOrientation.getValue());
				switch(sliderOrientation.getValue())
				{
					case 0:
						labelOrientation.setText("Gauche");
					break;
					case 1:
						labelOrientation.setText("Devant");
					break;
				}
			}
		}
		else if(e.getExtendedKeyCode() == 39)
		{
			if(transformDirToNumber(labelOrientation.getText()) < 2)
			{
				sliderOrientation.setValue(transformDirToNumber(labelOrientation.getText()) + 1);
				addToSend("or", sliderOrientation.getValue());
				switch(sliderOrientation.getValue())
				{
					case 1:
						labelOrientation.setText("Devant");
					break;
					case 2:
						labelOrientation.setText("Droite");
					break;
				}
			}
		}
		else if(e.getExtendedKeyCode() == 38)
		{
			if(Integer.parseInt(labelSpeed.getText()) < 9225)
			{
				sliderSpeed.setValue(Integer.parseInt(labelSpeed.getText()) + 1);
				labelSpeed.setText(String.valueOf(sliderSpeed.getValue()));
				addToSend("or", sliderOrientation.getValue());
			}
		}*/
    }

	@Override
    public void keyReleased(KeyEvent e){}

	@Override
    public void keyTyped(KeyEvent e){}

	public static Map<String, Integer> getRequests()
    {
	    return requests;
    }

	public static void setRequests(Map<String, Integer> requests)
    {
	    Interface.requests = requests;
    }
}

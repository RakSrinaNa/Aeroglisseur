package fr.vaucanson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

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
	private static final double VERSION = 1.5;
	private static final int stepSliderSpeed = 250;
	private static JFrame frame;
	private static Hashtable<String, Object> frameObjects;

	synchronized public static void addToSend(final String key, final int value)
	{
		Main.logger.log(Level.FINEST, "Changing " + key + " to " + value);
		requests.put(key, value);
	}

	public Interface()
	{
		frameObjects = new Hashtable<String, Object>();
		requests = new HashMap<String, Integer>();
		requests.put("or", 50);
		requests.put("vi", 0);
		requests.put("st", 0);
		requests.put("cv", 50);
		requests.put("ch", 50);
		Hashtable<Integer, JLabel> labelTableOrientation = new Hashtable<Integer,JLabel>();
		labelTableOrientation.put(0, new JLabel("Gauche"));
		labelTableOrientation.put(50, new JLabel("Devant"));
		labelTableOrientation.put(100, new JLabel("Droite"));
		Hashtable<Integer, JLabel> labelTableCamVert = new Hashtable<Integer,JLabel>();
		labelTableCamVert.put(0, new JLabel("Bas"));
		labelTableCamVert.put(50, new JLabel("Devant"));
		labelTableCamVert.put(100, new JLabel("Haut"));
		Hashtable<Integer, JLabel> labelTableSustentation = new Hashtable<Integer,JLabel>();
		labelTableSustentation.put(0, new JLabel("OFF"));
		labelTableSustentation.put(1, new JLabel("ON"));
		Hashtable<Integer, JLabel> labelTableSpeed = new Hashtable<Integer,JLabel>();
		labelTableSpeed.put(0, new JLabel("0"));
		labelTableSpeed.put(4612, new JLabel("4612"));
		labelTableSpeed.put(9225, new JLabel("9225"));
		frame = new JFrame("Controleur de l'a\351roglisseur v" + VERSION);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(570, 200));
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
		addSlider(0, 100, 50, "or", labelTableOrientation);
		addSlider(0, 9225, 0, "vi", labelTableSpeed);
		addSlider(0, 1, 50, "st", labelTableSustentation);
		addSlider(0, 100, 50, "cv", labelTableCamVert);
		addSlider(0, 100, 50, "ch", labelTableOrientation);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

	private static void addSlider(int min, int max, int base, final String key, Hashtable<Integer, JLabel> labels)
	{
		JPanel pan = new JPanel();
		JPanel panelSlider = new JPanel();
		JPanel panelText = new JPanel();
		JLabel lab = new JLabel();
		JSlider slider = new JSlider();
		pan.setLayout(new BorderLayout());
		pan.setPreferredSize(new Dimension(570, 66));
		pan.setFocusable(false);
		panelSlider.setLayout(new BorderLayout());
		panelSlider.setPreferredSize(new Dimension(500, 30));
		panelSlider.setFocusable(false);
		panelText.setLayout(new BorderLayout());
		panelText.setBackground(Color.RED);
		panelText.setPreferredSize(new Dimension(50, 5));
		panelText.setFocusable(false);
		lab.setBackground(Color.GRAY);
		lab.setText("0");
		lab.setFocusable(false);
		slider.setValue(min);
		slider.setMaximum(max);
		if(labels != null)
		{
			slider.setLabelTable(labels);
			slider.setPaintLabels(true);
		}
		slider.setFocusable(false);
		slider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				changeValue(key, 0);
			}
		});
		panelText.add(lab);
		panelSlider.add(slider);
		pan.add(panelText, BorderLayout.EAST);
		pan.add(panelSlider, BorderLayout.WEST);
		System.out.println(pan.hashCode());
		frame.add(pan);
		frameObjects.put("T" + key, lab);
		frameObjects.put("S" + key, slider);
	}
	
	public static Map<String, Integer> getRequests()
	{
		return requests;
	}

	public static void setRequests(Map<String, Integer> requests)
	{
		Interface.requests = requests;
	}

	public static void setValue(String key, float value)
	{
			JSlider slider = ((JSlider)frameObjects.get("S" + key));
			JLabel lab = ((JLabel)frameObjects.get("T" + key));
			int nValue = (int) ((slider.getMaximum() / 2) + (value * (slider.getMaximum() / 2)));
			slider.setValue(nValue);
			addToSend(key, slider.getValue());
			lab.setText(String.valueOf(slider.getValue()));
			frameObjects.put("S" + key, slider);
			frameObjects.put("T" + key, lab);
	}
	
	public static void changeValue(String key, int value)
	{
			JSlider slider = ((JSlider)frameObjects.get("S" + key));
			JLabel lab = ((JLabel)frameObjects.get("T" + key));
			slider.setValue(slider.getValue() + value);
			addToSend(key, slider.getValue());
			lab.setText(String.valueOf(slider.getValue()));
			frameObjects.put("S" + key, slider);
			frameObjects.put("T" + key, lab);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getExtendedKeyCode() == 37)
			changeValue("or", -10);
		else if(e.getExtendedKeyCode() == 39)
			changeValue("or", 10);
		else if(e.getExtendedKeyCode() == 38)
			changeValue("vi", stepSliderSpeed);
		else if(e.getExtendedKeyCode() == 40)
			changeValue("vi", -1 * stepSliderSpeed);
		else if(e.getExtendedKeyCode() == 17)
			changeValue("st", 0);
	}


	@Override
	public void keyReleased(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}
}

package fr.vaucanson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interface extends JFrame
{
	private static final long serialVersionUID = 7231194594050358481L;
	public JFrame mainFrame;
	private static JPanel contentPanel;
	private static Hashtable<String, Object> frameObjects;
	private static HashMap<String, Integer> requests;
	
	public Interface()
	{
		requests = new HashMap<String, Integer>();
		frameObjects = new Hashtable<String, Object>();
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
		
		/***************************************************************************************************/
		mainFrame = new JFrame("Controles de l'aeroglisseur");
		mainFrame.setLayout(new GridLayout(0, 5));
		mainFrame.setPreferredSize(new Dimension(570, 300));
		mainFrame.setResizable(true);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setVisible(true);
		mainFrame.addWindowListener(new WindowListener()
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
				mainFrame.dispose();
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
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		addSlider(0, 100, 50, "or", labelTableOrientation);
		addSlider(0, 9225, 0, "vi", labelTableSpeed);
		addSlider(0, 1, 50, "st", labelTableSustentation);
		addSlider(0, 100, 50, "cv", labelTableCamVert);
		addSlider(0, 100, 50, "ch", labelTableOrientation);
		mainFrame.add(contentPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.pack();
	}
	
	private void addSlider(int min, int max, int base, final String key, Hashtable<Integer, JLabel> labels)
	{
		JPanel tempPanel = new JPanel(new GridBagLayout());
		JLabel tempLabel = new JLabel();
		JSlider tempSlider = new JSlider();
		tempPanel.setPreferredSize(new Dimension(570, 66));
		tempPanel.setFocusable(false);
		tempSlider.setValue(min);
		tempSlider.setMaximum(max);
		tempSlider.setToolTipText("S" + key);
		if(labels != null)
		{
			tempSlider.setLabelTable(labels);
			tempSlider.setPaintLabels(true);
		}
		tempSlider.setFocusable(false);
		tempSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				changeValue(key, 0);
			}
		});
		tempLabel.setBackground(Color.GRAY);
		tempLabel.setText(String.valueOf(tempSlider.getValue()));
		tempLabel.setHorizontalAlignment(JLabel.CENTER);
		tempLabel.setFocusable(false);
		tempLabel.setToolTipText("T" + key);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.weightx = 0.85;
		tempPanel.add(tempSlider, constraints);
		constraints.gridx = 1;
		constraints.weightx = 0.15;
		tempPanel.add(tempLabel, constraints);
		contentPanel.add(tempPanel);
		frameObjects.put("T" + key, tempLabel);
		frameObjects.put("S" + key, tempSlider);
	}
	
	public void setValue(String key, float value)
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
	
	public void changeValue(String key, int value)
	{
		JSlider slider = ((JSlider)frameObjects.get("S" + key));
		JLabel lab = ((JLabel)frameObjects.get("T" + key));
		slider.setValue(slider.getValue() + value);
		addToSend(key, slider.getValue());
		lab.setText(String.valueOf(slider.getValue()));
		frameObjects.put("S" + key, slider);
		frameObjects.put("T" + key, lab);
	}
	
	synchronized public void addToSend(final String key, final int value)
	{
		Main.logger.log(Level.FINEST, "Changing " + key + " to " + value);
		requests.put(key, value);
	}
}

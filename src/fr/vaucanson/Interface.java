package fr.vaucanson;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interface extends JFrame
{
	private static final long serialVersionUID = 7231194594050358481L;
	public JFrame mainFrame;
	private static GridBagLayout mainLayout;
	private static JPanel contentPanel, camPanel, controlPanel;
	private static Hashtable<String, Object> frameObjects;
	private static HashMap<String, Integer> requests;
	private static int frameWidth, frameHeight;

	/**
	 * Constructor
	 */
	public Interface()
	{
		frameWidth = 800;
		frameHeight = 200;
		requests = new HashMap<String, Integer>();
		frameObjects = new Hashtable<String, Object>();
		Hashtable<Integer, JLabel> labelTableOrientation = new Hashtable<Integer, JLabel>();
		labelTableOrientation.put(0, new JLabel("Gauche"));
		labelTableOrientation.put(50, new JLabel("Devant"));
		labelTableOrientation.put(100, new JLabel("Droite"));
		Hashtable<Integer, JLabel> labelTableCamVert = new Hashtable<Integer, JLabel>();
		labelTableCamVert.put(0, new JLabel("Bas"));
		labelTableCamVert.put(70, new JLabel("Devant"));
		labelTableCamVert.put(100, new JLabel("Haut"));
		Hashtable<Integer, JLabel> labelTableSustentation = new Hashtable<Integer, JLabel>();
		labelTableSustentation.put(0, new JLabel("OFF"));
		labelTableSustentation.put(1, new JLabel("ON"));
		Hashtable<Integer, JLabel> labelTableSpeed = new Hashtable<Integer, JLabel>();
		labelTableSpeed.put(0, new JLabel("0"));
		labelTableSpeed.put(4612, new JLabel("4612"));
		labelTableSpeed.put(9225, new JLabel("9225"));
		/***************************************************************************************************/
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		camPanel = new JPanel();
		camPanel.setLayout(new BoxLayout(camPanel, BoxLayout.Y_AXIS));
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		mainLayout = new GridBagLayout();
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleCam = BorderFactory.createTitledBorder(loweredetched, "Cam\351ra");
		titleCam.setTitleJustification(TitledBorder.CENTER);
		camPanel.setBorder(titleCam);
		TitledBorder titleControl = BorderFactory.createTitledBorder(loweredetched, "Contr\364les");
		titleControl.setTitleJustification(TitledBorder.CENTER);
		controlPanel.setBorder(titleControl);
		addSlider(0, 1, 1, 0, "st", labelTableSustentation, controlPanel);
		addSlider(0, 9225, 250, 0, "vi", labelTableSpeed, controlPanel);
		addSlider(0, 100, 10, 50, "or", labelTableOrientation, controlPanel);
		addSlider(0, 100, 10, 70, "cv", labelTableCamVert, camPanel);
		addSlider(0, 100, 10, 50, "ch", labelTableOrientation, camPanel);
		/***************************************************************************************************/
		mainFrame = new JFrame("Controles de l'aeroglisseur");
		mainFrame.setMinimumSize(new Dimension(850, frameHeight));
		mainFrame.addComponentListener(new ComponentListener()
		{
			@Override
			public void componentHidden(ComponentEvent arg0)
			{}

			@Override
			public void componentMoved(ComponentEvent arg0)
			{}

			@Override
			public void componentResized(ComponentEvent arg0)
			{
				if(arg0.getComponent() instanceof JFrame)
				{
					frameWidth = ((JFrame) arg0.getComponent()).getWidth();
					frameHeight = ((JFrame) arg0.getComponent()).getHeight();
				}
				else if(arg0.getComponent() instanceof JPanel)
				{
					frameWidth = ((JPanel) arg0.getComponent()).getWidth();
					frameHeight = ((JPanel) arg0.getComponent()).getHeight();
				}
				resize();
			}

			@Override
			public void componentShown(ComponentEvent arg0)
			{
				if(arg0.getComponent() instanceof JFrame)
				{
					frameWidth = ((JFrame) arg0.getComponent()).getWidth();
					frameHeight = ((JFrame) arg0.getComponent()).getHeight();
				}
				else if(arg0.getComponent() instanceof JPanel)
				{
					frameWidth = ((JPanel) arg0.getComponent()).getWidth();
					frameHeight = ((JPanel) arg0.getComponent()).getHeight();
				}
				resize();
			}
		});
		mainFrame.setLayout(mainLayout);
		mainFrame.setPreferredSize(new Dimension(frameWidth, 450));
		mainFrame.setResizable(true);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setVisible(true);
		// mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		/************* CONTROLS **************************/
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.weightx = 0.5;
		contentPanel.add(controlPanel, constraints);
		constraints.gridx = 1;
		constraints.insets = new Insets(0, 0, 10, 0);
		contentPanel.add(camPanel, constraints);
		/*********** BUILDING FRMAE ***********************/
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.ipady = frameHeight;
		constraints.ipadx = frameWidth / 2;
		mainFrame.add(contentPanel, constraints);
		/**************************************************/
		mainFrame.pack();
	}

	private void resize()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.ipady = frameHeight;
		constraints.ipadx = frameWidth / 2;
		mainLayout.setConstraints(contentPanel, constraints);
		mainFrame.revalidate();
		mainFrame.repaint();
	}

	/**
	 * Used to add a slider with his label
	 * 
	 * @param min The minimum value of the slider
	 * @param max The maximum value of the slider
	 * @param majT The major tick of the slider
	 * @param bas The default value of the slider
	 * @param key The key for the slider/label
	 * @param labels The table label for the slider
	 * @param cont The container to put the slider with his label
	 * @return Nothing
	 */
	private void addSlider(int min, int max, int majT, int base, final String key, Hashtable<Integer, JLabel> labels, Container cont)
	{
		JPanel tempPanel = new JPanel(new GridBagLayout());
		final JTextField tempLabel = new JTextField();
		JSlider tempSlider = new JSlider();
		tempPanel.setPreferredSize(new Dimension(570, 66));
		tempPanel.setFocusable(false);
		tempSlider.setValue(min);
		tempSlider.setMaximum(max);
		tempSlider.setToolTipText("S" + key);
		tempSlider.setBounds(0, 0, 484, 66);
		tempSlider.setMajorTickSpacing(majT);
		tempSlider.setValue(base);
		tempSlider.setPaintTicks(true);
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
		tempLabel.setEditable(false);
		tempLabel.setOpaque(false);
		tempLabel.setBorder(null);
		tempLabel.setMinimumSize(new Dimension(10, 10));
		tempLabel.setPreferredSize(new Dimension(10, 10));
		tempLabel.setMaximumSize(new Dimension(10, 10));
		tempLabel.setBackground(Color.GRAY);
		tempLabel.setText(String.valueOf(tempSlider.getValue()));
		tempLabel.setHorizontalAlignment(JLabel.CENTER);
		tempLabel.setFocusable(false);
		tempLabel.setToolTipText("T" + key);
		tempLabel.addPropertyChangeListener("text", new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent arg0)
			{}
		});
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
		cont.add(tempPanel);
		frameObjects.put("T" + key, tempLabel);
		frameObjects.put("S" + key, tempSlider);
	}

	/**
	 * Used to set a value to an ID
	 * 
	 * @param key The value ID
	 * @param value The value to set
	 * @return Nothing
	 */
	public static void setValue(String key, float value)
	{
		JSlider slider = ((JSlider) frameObjects.get("S" + key));
		JTextField lab = ((JTextField) frameObjects.get("T" + key));
		int nValue;
		if(key.equals("cv"))
		{
			if(value >= 0)
				nValue = (int) (70 + (value * 30f));
			else
				nValue = (int) (70 + (value * 70f));
		}
		else if(key.equals("vi"))
			nValue = (int) (value * slider.getMaximum());
		else
			nValue = (int) ((slider.getMaximum() / 2) + (value * (slider.getMaximum() / 2)));
		slider.setValue(nValue);
		addToSend(key, slider.getValue());
		lab.setText(String.valueOf(slider.getValue()));
		frameObjects.put("S" + key, slider);
		frameObjects.put("T" + key, lab);
	}

	/**
	 * Used to change the value to an ID
	 * 
	 * @param key The value ID
	 * @param value The value to add to the curent value
	 * @return Nothing
	 */
	public static void changeValue(String key, int value)
	{
		JSlider slider = ((JSlider) frameObjects.get("S" + key));
		JTextField lab = ((JTextField) frameObjects.get("T" + key));
		slider.setValue(value != -9999 ? slider.getValue() + value : (slider.getValue() == 0 ? 1 : 0));
		addToSend(key, slider.getValue());
		lab.setText(String.valueOf(slider.getValue()));
		frameObjects.put("S" + key, slider);
		frameObjects.put("T" + key, lab);
	}

	/**
	 * Used to add to the stack a value to send
	 * 
	 * @param key The value ID
	 * @param value The value
	 * @return Nothing
	 */
	synchronized public static void addToSend(final String key, final int value)
	{
		Main.logger.log(Level.FINEST, "Changing " + key + " to " + value);
		requests.put(key, value);
	}

	/**
	 * @return the requests
	 */
	public static HashMap<String, Integer> getRequests()
	{
		return requests;
	}
}

package fr.vaucanson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interface extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 7231194594050358481L;
	private static final int VERSION = 1;
	private static JFrame frame;
	private static JSlider sliderSpeed, sliderOri;
	private static JPanel panSliderS, panSText, panVit, panSliderO, panOText,
	        panOri;
	private static JLabel sliderTextS, sliderTextO;

	public Interface()
	{
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
		panSliderS = new JPanel();
		panSliderS.setLayout(new BorderLayout());
		panSliderS.setBackground(Color.ORANGE);
		panSliderS.setPreferredSize(new Dimension(500, 30));
		panSliderO = new JPanel();
		panSliderO.setLayout(new BorderLayout());
		panSliderO.setBackground(Color.ORANGE);
		panSliderO.setPreferredSize(new Dimension(500, 30));
		panVit = new JPanel();
		panVit.setLayout(new BorderLayout());
		panOri = new JPanel();
		panOri.setLayout(new BorderLayout());
		panSText = new JPanel();
		panSText.setLayout(new BorderLayout());
		panSText.setBackground(Color.GREEN);
		panSText.setPreferredSize(new Dimension(50, 5));
		panOText = new JPanel();
		panOText.setLayout(new BorderLayout());
		panOText.setBackground(Color.GREEN);
		panOText.setPreferredSize(new Dimension(50, 5));
		sliderTextS = new JLabel();
		sliderTextS.setBackground(Color.GRAY);
		sliderTextS.setText("0");
		sliderTextO = new JLabel();
		sliderTextO.setBackground(Color.GRAY);
		sliderTextO.setText("Devant");
		sliderSpeed = new JSlider();
		sliderSpeed.setValue(0);
		sliderSpeed.setMaximum(9225);
		sliderSpeed.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				sliderTextS.setText(String.valueOf(sliderSpeed.getValue()));
				Sender s = new Sender("v=" + sliderSpeed.getValue());
				s.run();
			}
		});
		sliderOri = new JSlider();
		sliderOri.setValue(1);
		sliderOri.setMaximum(2);
		sliderOri.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(final ChangeEvent arg0)
			{
				if(transformDirToNumber(sliderTextO.getText()) != sliderOri.getValue())
					System.out.println(Outils.decrypt(Sender.send("o=" + sliderOri.getValue())));
				switch(sliderOri.getValue())
				{
					case 0:
						sliderTextO.setText("Gauche");
					break;
					case 1:
						sliderTextO.setText("Devant");
					break;
					case 2:
						sliderTextO.setText("Droite");
					break;
				}
			}
		});
		panSliderS.add(sliderSpeed);
		panSText.add(sliderTextS);
		panVit.add(panSliderS, BorderLayout.WEST);
		panVit.add(panSText, BorderLayout.EAST);
		panSliderO.add(sliderOri);
		panOText.add(sliderTextO);
		panOri.add(panSliderO, BorderLayout.WEST);
		panOri.add(panOText, BorderLayout.EAST);
		frame.add(panVit, BorderLayout.NORTH);
		frame.add(panOri, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

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
    public void keyPressed(KeyEvent e)
    {
		System.out.println(e.getExtendedKeyCode());
		if(e.getExtendedKeyCode() == 37)
		{
			if(transformDirToNumber(sliderTextO.getText()) > 0)
			{
				sliderOri.setValue(transformDirToNumber(sliderTextO.getText()) - 1);
				System.out.println(Outils.decrypt(Sender.send("o=" + sliderOri.getValue())));
				switch(sliderOri.getValue())
				{
					case 0:
						sliderTextO.setText("Gauche");
					break;
					case 1:
						sliderTextO.setText("Devant");
					break;
				}
			}
		}
		else if(e.getExtendedKeyCode() == 39)
		{
			if(transformDirToNumber(sliderTextO.getText()) < 2)
			{
				sliderOri.setValue(transformDirToNumber(sliderTextO.getText()) + 1);
				System.out.println(Outils.decrypt(Sender.send("o=" + sliderOri.getValue())));
				switch(sliderOri.getValue())
				{
					case 1:
						sliderTextO.setText("Devant");
					break;
					case 2:
						sliderTextO.setText("Droite");
					break;
				}
			}
		}
		else if(e.getExtendedKeyCode() == 38)
		{
			if(Integer.parseInt(sliderTextS.getText()) < 9225)
			{
				sliderSpeed.setValue(Integer.parseInt(sliderTextS.getText()) + 1);
				sliderTextS.setText(String.valueOf(sliderSpeed.getValue()));
				Sender s = new Sender("v=" + sliderSpeed.getValue());
				s.run();
			}
		}
    }

	@Override
    public void keyReleased(KeyEvent e){}

	@Override
    public void keyTyped(KeyEvent e){}
}

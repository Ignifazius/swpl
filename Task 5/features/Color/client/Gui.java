/**
 * TODO description
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;

class Gui {
	String farbe;

	public Gui(String title, Client chatClient) {

		farbe = "[schwarz] ";
		ButtonGroup colorGroup = new ButtonGroup();
		JMenu colorMenu = new JMenu("Farbe");
		menuBar.add(colorMenu);

		JRadioButtonMenuItem colorActionBlack = new JRadioButtonMenuItem("schwarz");
		colorMenu.add(colorActionBlack);
		colorGroup.add(colorActionBlack);

		JRadioButtonMenuItem colorActionRed = new JRadioButtonMenuItem("rot");
		colorMenu.add(colorActionRed);
		colorGroup.add(colorActionRed);

		JRadioButtonMenuItem colorActionBlue = new JRadioButtonMenuItem("blau");
		colorMenu.add(colorActionBlue);
		colorGroup.add(colorActionBlue);

		JRadioButtonMenuItem colorActionGreen = new JRadioButtonMenuItem("grün");
		colorMenu.add(colorActionGreen);
		colorGroup.add(colorActionGreen);

		colorActionBlack.setSelected(true);

		colorActionRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				farbe = "[rot] ";
			}
		});

		colorActionBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				farbe = "[schwarz] ";
			}
		});

		colorActionBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				farbe = "[blau] ";
			}
		});

		colorActionGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				farbe = "[grün] ";
			}
		});


	}

	public String modString(String in){
		return original(farbe + in);
	}


}
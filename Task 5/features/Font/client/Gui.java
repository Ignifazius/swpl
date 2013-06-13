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
	String fontItalic, fontBold;

	public Gui(String title, Client chatClient) {

		JMenu fontMenu = new JMenu("Schriftart");
		menuBar.add(fontMenu);



		ButtonGroup fontGroup = new ButtonGroup();	        


		JCheckBoxMenuItem fontActionItalic = new JCheckBoxMenuItem("italic");
		fontMenu.add(fontActionItalic);
		fontGroup.add(fontActionItalic);

		JCheckBoxMenuItem fontActionBold = new JCheckBoxMenuItem("bold");
		fontMenu.add(fontActionBold);
		fontGroup.add(fontActionBold);

		fontActionItalic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fontItalic = "(italic) ";
			}
		});

		fontActionBold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fontBold = "(bold) ";
			}
		});


	}

	public String modString(String in){
		return original(fontBold + fontItalic + in);
	}


}
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import client.*;


/**
 * simple swing gui for the chat client
 */
public class Gui extends JFrame implements ChatLineListener {

	private static final long serialVersionUID = 1L;

	protected JTextArea outputTextbox;
	protected JTextField inputField;

	private static int rowstextarea = 20;
	private static int colstextarea = 60;

	private Client chatClient;
	
	private String farbe = "[schwarz] | ";
	
	
	
	/**
	 * creates layout
	 * 
	 * @param title
	 *            title of the window
	 * @param chatClient
	 *            chatClient that is used for sending and receiving messages
	 */
	public Gui(String title, Client chatClient) {
		System.out.println("starting gui...");

		if (Client.getColorBool()) {
			farbe = "[schwarz] | ";
		
			// Creates a menubar for a JFrame
	        JMenuBar menuBar = new JMenuBar();
	        
	        // Add the menubar to the frame
	        setJMenuBar(menuBar);
	        
	        // Define and add two drop down menu to the menubar
	        JMenu fileMenu = new JMenu("Datei");
	        JMenu colorMenu = new JMenu("Farbe");
	        menuBar.add(fileMenu);
	        menuBar.add(colorMenu);
	
	
			ButtonGroup bg = new ButtonGroup();
	
			JRadioButtonMenuItem colorActionBlack = new JRadioButtonMenuItem("schwarz");
	        colorMenu.add(colorActionBlack);
	        bg.add(colorActionBlack);
			
	        JRadioButtonMenuItem colorActionRed = new JRadioButtonMenuItem("rot");
	        colorMenu.add(colorActionRed);
	        bg.add(colorActionRed);
	        
	        JRadioButtonMenuItem colorActionBlue = new JRadioButtonMenuItem("blau");
	        colorMenu.add(colorActionBlue);
	        bg.add(colorActionBlue);
	        
	        JRadioButtonMenuItem colorActionGreen = new JRadioButtonMenuItem("grün");
	        colorMenu.add(colorActionGreen);
	        bg.add(colorActionGreen);
	        
	        colorActionBlack.setSelected(true);
	
			
			
			
			
			outputTextbox = new JTextArea(Gui.rowstextarea, Gui.colstextarea);
			outputTextbox.setEditable(false);
			inputField = new JTextField();
			inputField.addActionListener(getInput());
			
			colorActionRed.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                farbe = "[rot] | ";
	            }
	        });
			
			colorActionBlack.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                farbe = "[schwarz] | ";
	            }
	        });
			
			colorActionBlue.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                farbe = "[blau] | ";
	            }
	        });
			
			colorActionGreen.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                farbe = "[grün] | ";
	            }
	        });
		
		} else {
			farbe = "";
		}
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputTextbox)
                    .addComponent(inputField))
        );

        // layout.linkSize(SwingConstants.HORIZONTAL, findButton, cancelButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(outputTextbox)
                    .addComponent(inputField));
		
    	// register listener so that we are informed whenever a new chat message
		// is received (observer pattern)
		chatClient.addLineListener(this);

		setTitle(title);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.chatClient = chatClient;
	}
	
	
	
	private ActionListener getInput() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatClient.send((String) farbe + inputField.getText());
				inputField.setText("");
			}
		};
	}

	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(String line) {
		outputTextbox.append(line);
	}

}

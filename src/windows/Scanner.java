package windows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import gfx.ScaledImageLabel;
import util.Keyboard;

@SuppressWarnings("serial")
public class Scanner extends JFrame{	
	
	private volatile long counter = 0L;	
	private volatile boolean needsUpdate = false;
	
	private String roomName;
	private JLabel title;
	private String normalTitle = "?";
	private JLabel subtext;
	private String normalSubtext = "<html><center>Please use the barcode scanner and your Student ID to sign in and out of this room.</center></html>";
	public Scanner(String roomName, String hash) {
		super("Scanner Utility");
        this.roomName = roomName.substring(0, 1).toUpperCase() + roomName.substring(1);
        this.normalTitle = "<html><center>" + this.roomName + " sign in/out station</center></html>";
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		this.setPreferredSize(new Dimension(640,480));
		this.setSize(xSize,ySize);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(this.getContentPane());
        this.pack();
        this.setVisible(true);
        this.addKeyListener(new Keyboard(this, roomName, hash));
        this.counter = System.currentTimeMillis();
        new Thread() {
        	public void run() {
        		while(true) {
        			if(counter < System.currentTimeMillis() - 4000 && needsUpdate) {
        				needsUpdate = false;
        				title.setText(normalTitle);
        				subtext.setText(normalSubtext);
        			}
        			try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						continue;
					}
        		}
        	}
        }.start();
	}
	
	public void addComponentsToPane(Container p) {
		p.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    
		JLabel label = new JLabel("                ",SwingConstants.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridx = 0;
	    c.gridy = 0;
	    p.add(label, c);
	    
	    label = new JLabel(label.getText(),SwingConstants.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridx = 2;
	    c.gridy = 0;
	    p.add(label, c);
	    
		title = new JLabel(normalTitle,SwingConstants.CENTER);
		Font labelFont = title.getFont();
		title.setFont(new Font(labelFont.getName(), Font.PLAIN, 86));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridx = 1;
	    c.gridy = 0;
	    p.add(title, c);
	    
		subtext = new JLabel(normalSubtext,SwingConstants.CENTER);
		labelFont = subtext.getFont();
		subtext.setFont(new Font(labelFont.getName(), Font.PLAIN, 40));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridx = 1;
	    c.gridy = 1;
	    p.add(subtext, c);
	    
	    BufferedImage image;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/pictures/scan.png"));
			ScaledImageLabel imageLabel = new ScaledImageLabel();
			imageLabel.setIcon(new ImageIcon(image));
			c.fill = GridBagConstraints.BOTH;
		    c.weightx = 1.0;
		    c.weighty = 1.0;
		    c.gridx = 1;
		    c.gridy = 2;
		    p.add(imageLabel, c);
		} catch (IOException e) {
			e.printStackTrace();
			JLabel imageLabel = new JLabel("Cannot open file!");
			c.fill = GridBagConstraints.BOTH;
		    c.gridx = 1;
		    c.gridy = 2;
		    p.add(imageLabel, c);
		}
	}
	
	public String getHeaderText() {
		return title.getText();
	}
	
	public String getSubtitleText() {
		return subtext.getText();
	}
	
	public void setHeaderText(String text) {
		title.setText("<html><center>" + text + "</center></html>");
		counter = System.currentTimeMillis();
		needsUpdate = true;
	}
	
	public void setSubtitleText(String text) {
		subtext.setText("<html><center>" + text + "</center></html>");
		counter = System.currentTimeMillis();
		needsUpdate = true;
	}
}

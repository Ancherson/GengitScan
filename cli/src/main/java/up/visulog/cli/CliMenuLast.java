package up.visulog.cli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class CliMenuLast extends JPanel{
	private JTextPane JCommande = new JTextPane();
	private CLIMenu CLIM;
	private JPanel panelMain;
	private JPanel panelCommande;
	
	public void setCommande(String line) {
		this.JCommande.setText(line);
	}
	
	public CliMenuLast(CLIMenu CLIM) {
		this.CLIM = CLIM;
		
		this.setSize(700,500);
		this.setBackground(new Color(180, 211, 212));
		this.setLayout(null);
		
		GridLayout g = new GridLayout(4,1);
		g.setHgap(10);
		panelMain = new JPanel(g);
		panelMain.setBackground(Color.white);
		//panelMain.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		panelMain.setBounds(50, 30, 600, 400);
		
		
		JButton execute = new JButton("Execute");
		execute.setBackground(new Color(180, 211, 212));
		JButton addNewPlugin = new JButton("Add Other Plugin");
		addNewPlugin.setBackground(new Color(180, 211, 212));
		execute.addActionListener((event) -> {
			try {
				CLIM.launch();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		});
		addNewPlugin.addActionListener((event) -> CLIM.changeToCliPlugin());
		
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		panel1.setBackground(Color.white);
		execute.setPreferredSize(new Dimension(240,50));
		execute.setFont(new Font("Monica", Font.PLAIN, 20));
		panel1.add(execute);
		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		panel2.setBackground(Color.white);
		addNewPlugin.setPreferredSize(new Dimension(240,50));
		addNewPlugin.setFont(new Font("Monica", Font.PLAIN, 20));
		panel2.add(addNewPlugin);
		
		JLabel subTitle = new JLabel("you command :");
		JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
		panelTitle.setSize(30, 10);
		panelTitle.setBackground(Color.white);
		panelTitle.add(subTitle);
	
		JCommande.setEditable(false);
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		JCommande.setParagraphAttributes(attribs, true);

		
		
		panelCommande = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelCommande.setLayout(new BorderLayout());
		panelCommande.setBackground(Color.white);
		panelCommande.add(JCommande, BorderLayout.NORTH);
		
		
		panelMain.add(panelTitle);
		panelMain.add(panelCommande);
		
		
		panelMain.add(panel1);
		panelMain.add(panel2);

		
		this.add(panelMain);
	}
	
}

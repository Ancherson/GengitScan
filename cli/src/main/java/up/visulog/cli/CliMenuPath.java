package up.visulog.cli;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CliMenuPath extends JPanel{
	
	private CLIMenu CLIM; 
	private JLabel title;
	private JLabel info;
	private JButton next = new JButton("Next");
	//Maybe change this by a browse button
	private JTextField path = new JTextField();
	
	public CliMenuPath(CLIMenu CLIM) {
		this.CLIM = CLIM;
		
		this.setSize(700,500);
		this.setBackground(new Color(180, 211, 212));
		this.setLayout(null);
		
		GridLayout g = new GridLayout(3,1);
		g.setVgap(80);
		JPanel panelMain = new JPanel(g);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 600, 400);
		
		title = new JLabel("GenGit Scan", 0);
		title.setFont(new Font("Monica", Font.PLAIN, 30));
		
		info = new JLabel("The path to your git project : ", 0);
		info.setFont(new Font("Monica", Font.PLAIN, 15));
		
		next.setBackground(new Color(180, 211, 212));
		next.addActionListener((event) -> {
			CLIM.addPath(path.getText());
			CLIM.changeToCliPlugin();
		});
		
		
		panelMain.add(title);
		
		GridLayout g1 = new GridLayout(1,2);
		g1.setHgap(60);
		JPanel panel2 = new JPanel(g1);
		panel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		panel2.add(info);
		panel2.add(path);
		panelMain.add(panel2);
		
		JPanel panel3 = new JPanel();
		next.setPreferredSize(new Dimension(100,50));
		panel3.add(next);
		panelMain.add(panel3);
		
		this.add(panelMain);
		
	}
}

package up.visulog.cli;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CliMenuLast extends JPanel{
	private CLIMenu CLIM;
	
	public CliMenuLast(CLIMenu CLIM) {
		this.CLIM = CLIM;
		
		this.setSize(700,500);
		this.setBackground(new Color(180, 211, 212));
		this.setLayout(null);
		
		GridLayout g = new GridLayout(1,2);
		g.setHgap(20);
		JPanel panelMain = new JPanel(g);
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
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
		
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 150));
		panel1.setBackground(Color.white);
		execute.setPreferredSize(new Dimension(240,50));
		execute.setFont(new Font("Monica", Font.PLAIN, 20));
		panel1.add(execute);
		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 150));
		panel2.setBackground(Color.white);
		addNewPlugin.setPreferredSize(new Dimension(240,50));
		addNewPlugin.setFont(new Font("Monica", Font.PLAIN, 20));
		panel2.add(addNewPlugin);
		
		
		panelMain.add(panel1);
		panelMain.add(panel2);
		
		this.add(panelMain);
	}
	
}

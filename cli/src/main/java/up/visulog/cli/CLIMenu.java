package up.visulog.cli;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CLIMenu extends JFrame {
	
	private String commande;
	private JPanel mainPanel;
	private CliMenuPlugin menuPlugin;
	private CliMenuParameter menuParameter;
	private CardLayout cardLayout;
	
	public CLIMenu() {
		this.setSize(1200, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GenGit Scan");
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		menuPlugin = new CliMenuPlugin();
		mainPanel.add("menuPlugin" ,menuPlugin);
		
		
		
		this.getContentPane().add(mainPanel);
		cardLayout.show(mainPanel, "menuPlugin");
		
		this.setVisible(true);
	}
	
	
	
}

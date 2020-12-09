package up.visulog.cli;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CLIMenu extends JFrame {
	private String projectID;
	private String privateToken;
	private String commande;
	
	private JPanel mainPanel;
	
	private CliMenuPlugin menuPlugin;
	private CliMenuParameter menuParameter;
	private CliMenuLast menuLast;

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
		
		menuPlugin = new CliMenuPlugin(this);
		mainPanel.add("menuPlugin" ,menuPlugin);
		
		
		
		this.getContentPane().add(mainPanel);
		cardLayout.show(mainPanel, "menuPlugin");
		
		this.menuLast = new CliMenuLast(this);
		mainPanel.add("menuLast",menuLast);
		
		this.setVisible(true);
	}
	
	public void addPlugin(String R) {
		if(commande == null) commande = "--addPlugin="+R;
		else commande = commande + " --addPlugin=" + R;
	}
	
	public void addPrivateToken(String token) {
		this.privateToken = token;
		commande = commande + " --privateToken=" + token;
	}
	
	public void addProjectID(String ID) {
		this.projectID = ID;
		commande = commande + " --projectId=" + ID;
	}
	
	

	
	
	public void changeToCliPara(String result, int version) {
		if(version == 3 && privateToken != null && projectID != null) {
			changeToMenuLast();
			addPlugin(result);
			
		}
		else {
			menuParameter = new CliMenuParameter(this,result,version);
			mainPanel.add("menuParameter", menuParameter);
			cardLayout.show(mainPanel, "menuParameter");
		}
		
	}
	
	public void changeToCliPlugin() {
		cardLayout.show(mainPanel, "menuPlugin");
		this.setSize(1200,500);
	}
	
	public void changeToMenuLast() {
		cardLayout.show(mainPanel, "menuLast");
	}
	
		public void launch() throws IOException, URISyntaxException {
		CLILauncher.launch(commande.split(" "));
		this.dispose();
	}
}


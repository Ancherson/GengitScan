package up.visulog.cli;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CLIMenu extends JFrame {
	private String projectID;
	private String privateToken;
	private String commande = "";
	
	private JPanel mainPanel;
	
	private CliMenuPath menuPath;
	private CliMenuPlugin menuPlugin;
	private CliMenuParameter menuParameter;
	private CliMenuLast menuLast;

	private CardLayout cardLayout;
	
	public CLIMenu() {
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GenGit Scan");
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		menuPath = new CliMenuPath(this);
		mainPanel.add("menuPath", menuPath);
		menuPlugin = new CliMenuPlugin(this);
		mainPanel.add("menuPlugin" ,menuPlugin);
		menuLast = new CliMenuLast(this);
		mainPanel.add("menuLast",menuLast);
		
		
		this.getContentPane().add(mainPanel);
		cardLayout.show(mainPanel, "menuPath");
	
		
		this.setVisible(true);
	}
	
	public void addPath(String path) {
		commande += path;
	}
	
	public void addPlugin(String R) {
		if(commande.length() == 0) commande = "--addPlugin="+R;
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
		this.setLocationRelativeTo(null);
		
	}
	
	public void changeToCliPlugin() {
		this.setSize(1200,500);
		cardLayout.show(mainPanel, "menuPlugin");
		this.setLocationRelativeTo(null);
	}
	
	public void changeToMenuLast() {
		this.setSize(700,500);
		cardLayout.show(mainPanel, "menuLast");
		this.setLocationRelativeTo(null);
	}
	
	public void launch() throws IOException, URISyntaxException {
		this.dispose();
		//TODO add a waiting screen (place dispose after "CLILauncher.launch(commande.split(" "));"
		CLILauncher.launch(commande.split(" "));

	}
}


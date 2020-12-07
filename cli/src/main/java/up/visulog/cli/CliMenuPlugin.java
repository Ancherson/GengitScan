package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class CliMenuPlugin extends JFrame {
	private String[] result = {"--addPlugin="};

	//Buttons that represents the "main" plugins
	private JButton countLinesAdded = makeABeautifulButton("countLinesAdded");
	private JButton countLinesDeleted = makeABeautifulButton("countLinesDeleted");
	private JButton countMergeCommits = makeABeautifulButton("countMergeCommits");
	private JButton countCommits = makeABeautifulButton("countCommits");
	private JButton countContribution = makeABeautifulButton("countContribution");
	
	//Between the lines of "-" are the plugins that use the API, need to insert project Id and Token
	//-------------------------------------------------------------------------------------------------------------
	private JButton getMembers = makeABeautifulButton("getMembers");
	private JButton getExtensions = makeABeautifulButton("getExtensions");
	private JButton countIssues = makeABeautifulButton("countIssues");
	private JButton countComments = makeABeautifulButton("countComments");
	//-------------------------------------------------------------------------------------------------------------
	

	public CliMenuPlugin() {
		this.setTitle("GenGit Scan");
		this.setAlwaysOnTop(true);
		this.setSize(1200,500);
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(new Color(180, 211, 212));
		this.setResizable(false);
		
		JPanel panelMain = new JPanel(new GridLayout(4,1));
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 1100, 400);
		
		this.getContentPane().add(panelMain);
		
		JLabel title = new JLabel("Plugins without API");
		title.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title);
		
		GridLayout g1 = new GridLayout(1,5);
		g1.setHgap(20);
		JPanel buttonNoAPI = new JPanel(g1);
		buttonNoAPI.setBackground(Color.white);
		panelMain.add(buttonNoAPI);
				
		buttonNoAPI.add(countLinesAdded);
		buttonNoAPI.add(countLinesDeleted);
		buttonNoAPI.add(countMergeCommits);
		buttonNoAPI.add(countCommits);
		buttonNoAPI.add(countContribution);
		
		countLinesAdded.addActionListener((event) -> {
			eventButton("countLinesAdded");
		});
		countLinesDeleted.addActionListener((event) -> {
			eventButton("countLinesDeleted");
		});
		countMergeCommits.addActionListener((event) -> {
			eventButton("countMergeCommits");
		});
		countCommits.addActionListener((event) -> {
			eventButton("countCommits");
		});
		countContribution.addActionListener((event) -> {
			eventButton("countContribution");
		});
		
		JLabel title2 = new JLabel("Plugins with API");
		title2.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title2);
		
		GridLayout g2 = new GridLayout(1,4);
		g2.setHgap(20);
		JPanel buttonAPI = new JPanel(g2);
		buttonAPI.setBackground(Color.white);
		panelMain.add(buttonAPI);
		
		buttonAPI.add(getMembers);
		buttonAPI.add(getExtensions);
		buttonAPI.add(countIssues);
		buttonAPI.add(countComments);
		
		getMembers.addActionListener((event) -> {
			eventButton("getMembers");
		});
		
		getExtensions.addActionListener((event) -> {
			eventButton("getExtensions");
		});
		
		countIssues.addActionListener((event) -> {
			eventButton("countIssues");
		});
		
		countComments.addActionListener((event) -> {
			eventButton("countComments");
		});
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JButton makeABeautifulButton(String name) {
		JButton b = new JButton(name);
		b.setBackground(new Color(180, 211, 212));
		return b;
	}
	
	public void eventButton(String name) {
		result[0] += name;
		
		if(name.equals("countLinesAdded") || name.equals("countLinesDeleted")) {
			new CliMenuParameter(result[0],1);
		} else if(name.equals("countCommits")) {
			new CliMenuParameter(result[0],2);
		} else if(name.equals("getMembers") || name.equals("getExtensions") || name.equals("countIssues") || name.equals("countComments")) {
			new CliMenuParameter(result[0],3);
		} else {
			try {
				CLILauncher.launch(result);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		this.dispose();
	}
	
	
	public void paraMenu(int version) {
		new CliMenuParameter(result[0], version);
	}
	
}

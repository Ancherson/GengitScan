package up.visulog.cli;

import javax.swing.*;

import java.awt.*;

public class CliMenuPlugin extends JFrame{
	private JPanel panneauPlugin;
	
	private String [] result;
	

	
	//Buttons that represents the "main" plugins
	
	private JButton countLinesAdded;
	private JButton countLinesDeleted;
	
	private JButton countMergeCommits;
	
	private JButton countCommits;
	
	private JButton countContribution;
	
	//Between the lines of "-" are the plugins that use the API, need to insert project Id and Token
	//-------------------------------------------------------------------------------------------------------------
	
	private JButton getMembers;
	
	private JButton getExtensions;
	
	private JButton countIssues;
	
	private JButton countComments;
	//-------------------------------------------------------------------------------------------------------------
	

	
	public CliMenuPlugin() {
		result = new String [1];
		result[0] = "--addPlugin=";
		this.setTitle("GenGit Scan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,800);
		panneauPlugin = new JPanel();
		panneauPlugin.setLayout(new GridLayout(3,3));
		
		
		countLinesAdded = new JButton("countLinesAdded");
		countLinesDeleted = new JButton("countLinesDeleted");
		countMergeCommits = new JButton("countMergeCommits");
		countCommits = new JButton("countCommits");
		countContribution = new JButton("countContribution");
		
		countLinesAdded.addActionListener((event) -> {result [0]=result[0]+ "countLinesAdded"; new CliMenuParameter(result[0],1);this.dispose();});
		countLinesDeleted.addActionListener((event) -> {result [0]=result[0]+ "countLinesDeleted"; new CliMenuParameter(result[0],1);this.dispose();});
		countMergeCommits.addActionListener((event) -> {result [0]=result[0]+ "countMergeCommits";this.dispose();CLILauncher.setGraphicOver(true);CLILauncher.setArgument(result);});
		countCommits.addActionListener((event) -> {result [0]=result[0]+ "countCommits"; new CliMenuParameter(result[0],1);this.dispose();});
		countContribution.addActionListener((event) -> {result [0]=result[0]+ "countContribution";this.dispose();CLILauncher.setGraphicOver(true);CLILauncher.setArgument(result);});
		
		
		
		getMembers = new JButton("getMembers");
		getExtensions = new JButton("getExtensions");
		countIssues = new JButton("countIssues");
		countComments = new JButton("countComments");
		
		
		getMembers.addActionListener((event) -> {result [0]=result[0]+ "getMembers"; new CliMenuParameter(result[0],2);this.dispose();});
		getExtensions.addActionListener((event) -> {result [0]=result[0]+ "getExtensions"; new CliMenuParameter(result[0],2);this.dispose();});
		countIssues.addActionListener((event) -> {result [0]=result[0]+ "countIssues"; new CliMenuParameter(result[0],2);this.dispose();});
		countComments.addActionListener((event) -> {result [0]=result[0]+ "countComments"; new CliMenuParameter(result[0],2);this.dispose();});
		

		
		panneauPlugin.add(countLinesAdded);
		panneauPlugin.add(countLinesDeleted);
		panneauPlugin.add(countMergeCommits);
		panneauPlugin.add(countCommits);
		panneauPlugin.add(countContribution);
		panneauPlugin.add(getMembers);
		panneauPlugin.add(getExtensions);
		panneauPlugin.add(countIssues);
		panneauPlugin.add(countComments);
		
		this.getContentPane().add(panneauPlugin);
		this.getContentPane().setLayout(new GridLayout());
		this.setVisible(true);
	}
	
	public void paraMenu(int version) {
		new CliMenuParameter(result[0], version);
		
	}
	
}

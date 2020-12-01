package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class CliMenuParameter extends JFrame{

	
	private String [] result;
	private int version;
	
	private JButton back;
	private JButton submit;
	private JPanel panneauPara;

	//CheckBox and RadioButton that will appear when the selected plugin have more parameter
	
	private JRadioButton PerDays;
	private JRadioButton PerWeeks;
	private JRadioButton PerMonths;
	
	private JCheckBox PerAuthor;
	
	private JCheckBox ForAllBranches;
	
	//Texte Area needed to be fill For plugins using API
	
	private JTextField projectId;
	private JTextField privateToken;

	
	public void submitMethode(){
		
		if(version == 1) {
			if(PerAuthor.isSelected()) result[0] = result[0] + "PerAuthor";
			
			if(PerDays.isSelected()) result[0] = result[0] + "PerDays";
			else if (PerWeeks.isSelected()) result[0] = result[0] + "PerWeeks";
			else if(PerMonths.isSelected()) result[0] = result[0] + "PerMonths";
			

			
			if(ForAllBranches.isSelected()) result[0] = result[0] +  "ForAllBranches";
		}
		else {
			result[1] = "--privateToken=" + privateToken.getText();
			result[2] = "--projectId=" + projectId.getText();
			CLILauncher.setArgument(result[1],1);
			CLILauncher.setArgument(result[2],2);
		}
		
		CLILauncher.setGraphicOver(true);
		CLILauncher.setArgument(result[0],0);

	}

	
	public CliMenuParameter(String plugin,int ver) {
		version = ver;
		result = new String [3];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,800);
		
		panneauPara = new JPanel();

		
		result [0] = plugin; 
		if(version == 1) {
			panneauPara.setLayout(new GridLayout(2,2));
			JPanel menuRadio = new JPanel();
			menuRadio.setLayout(new GridLayout(3,1));
			
			PerDays = new JRadioButton("PerDays");
			PerWeeks = new JRadioButton("PerWeeks");
			PerMonths = new JRadioButton("PerMonths");
			ButtonGroup Date = new ButtonGroup();
			
			Date.add(PerDays);
			Date.add(PerWeeks);
			Date.add(PerMonths);
			
			menuRadio.add(PerDays);
			menuRadio.add(PerWeeks);
			menuRadio.add(PerMonths);

			
			JPanel menuBox = new JPanel();
			menuBox.setLayout(new GridLayout(2,1));
			
			PerAuthor = new JCheckBox("PerAuthor");
			ForAllBranches = new JCheckBox("ForAllBranches");	
			
			menuBox.add(PerAuthor);
			menuBox.add(ForAllBranches);
			
			panneauPara.add(menuBox);
			panneauPara.add(menuRadio);
			
			
		}
		else {
			panneauPara.setLayout(new GridLayout(2,2));
			JPanel project = new JPanel();
			project.setLayout(new GridLayout(1,2));
			
			projectId = new JTextField("projectId");
			privateToken = new JTextField("projectToken");
			
			project.add(projectId);
			project.add(privateToken);
			
			panneauPara.add(project);
		}
		back = new JButton("Back");
		back.addActionListener((event) -> {this.dispose();new CliMenuPlugin();});
		submit = new JButton("submit");
		submit.addActionListener((event) -> {this.dispose(); submitMethode();});
		panneauPara.add(back);
		panneauPara.add(submit);
		this.add(panneauPara);
		this.setVisible(true);
	}

	
}

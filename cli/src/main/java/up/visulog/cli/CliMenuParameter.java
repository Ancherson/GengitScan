package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;

public class CliMenuParameter extends JFrame{
	private String [] result;
	
	private JButton back;
	private JButton submit;
	private JPanel panneauPara;

	//CheckBox and RadioButton that will appear when the selected plugin have more parameter
	
	private JRadioButton perDays;
	private JRadioButton perWeeks;
	private JRadioButton perMonths;
	
	private JCheckBox perAuthors;
	
	private JCheckBox forAllBranches;
	
	//Texte Area needed to be fill for plugins using API
	
	private JTextField projectId;
	private JTextField projectToken;


	
	public CliMenuParameter(String plugin,int version) {
		result = new String [3];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,800);
		
		panneauPara = new JPanel();

		
		result [0] = plugin; 
		if(version == 1) {
			panneauPara.setLayout(new GridLayout(2,2));
			JPanel menuRadio = new JPanel();
			menuRadio.setLayout(new GridLayout(3,1));
			
			perDays = new JRadioButton("perDays");
			perWeeks = new JRadioButton("perWeeks");
			perMonths = new JRadioButton("perMonths");
			ButtonGroup Date = new ButtonGroup();
			
			Date.add(perDays);
			Date.add(perWeeks);
			Date.add(perMonths);
			
			menuRadio.add(perDays);
			menuRadio.add(perWeeks);
			menuRadio.add(perMonths);

			
			JPanel menuBox = new JPanel();
			menuBox.setLayout(new GridLayout(2,1));
			
			perAuthors = new JCheckBox("perAuthors");
			forAllBranches = new JCheckBox("forAllBranches");	
			
			menuBox.add(perAuthors);
			menuBox.add(forAllBranches);
			
			panneauPara.add(menuBox);
			panneauPara.add(menuRadio);
			
			
		}
		else {
			panneauPara.setLayout(new GridLayout(2,2));
			JPanel project = new JPanel();
			project.setLayout(new GridLayout(1,2));
			
			projectId = new JTextField("projectId");
			projectToken = new JTextField("projectToken");
			
			project.add(projectId);
			project.add(projectToken);
			
			panneauPara.add(project);
		}
		back = new JButton("Back");
		back.addActionListener((event) -> {this.dispose();new CliMenuPlugin();});
		submit = new JButton("submit");
		submit.addActionListener((event) -> {this.dispose();});
		panneauPara.add(back);
		panneauPara.add(submit);
		this.add(panneauPara);
		this.setVisible(true);
	}

	
}

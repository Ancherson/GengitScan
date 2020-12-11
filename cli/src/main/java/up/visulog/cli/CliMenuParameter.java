package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class CliMenuParameter extends JPanel {
	private CLIMenu CLIM;
	private String result;
	private boolean version;
	
	private JButton back = makeABeautifulButton("BACK");
	private JButton submit = makeABeautifulButton("SUBMIT");

	//CheckBox and RadioButton that will appear when the selected plugin have more parameter
	private ButtonGroup perDate = new ButtonGroup();
	private JRadioButton perDays = makeBeautifulRadioButton("PerDays");
	private JRadioButton perWeeks = makeBeautifulRadioButton("PerWeeks");
	private JRadioButton perMonths = makeBeautifulRadioButton("PerMonths");
	private JRadioButton none = new JRadioButton("None");
	
	private JCheckBox perAuthor = new JCheckBox("perAuthor");
	private JCheckBox forAllBranches = new JCheckBox("forAllBranches");
	
	//Texte Area needed to be fill For plugins using API
	private JTextField projectId = new JTextField();
	private JTextField privateToken = new JTextField();

	
	public CliMenuParameter(CLIMenu CLIM, String pluginName) {
		version = (pluginName.equals("countLinesAdded") || pluginName.equals("countLinesDeleted") || pluginName.equals("countCommits"));
		this.CLIM = CLIM;
		
		none.setFont(new Font("Monica", Font.PLAIN, 15));
		none.setHorizontalAlignment(0);
		none.setOpaque(false);
		none.addActionListener((event) -> NoDate());
		none.setSelected(true);
		perDate.add(none);
		
		perAuthor.setFont(new Font("Monica", Font.ITALIC, 15));
		perAuthor.setEnabled(false);
		perAuthor.setSelected(true);
		perAuthor.setHorizontalAlignment(0);
		perAuthor.setOpaque(false);
		
		
		
		
		forAllBranches.setFont(new Font("Monica", Font.PLAIN, 15));
		forAllBranches.setHorizontalAlignment(0);
		forAllBranches.setOpaque(false);

		this.setSize(800,500);
		this.CLIM.setSize(800,500);
		this.setLayout(null);
		this.setBackground(new Color(180, 211, 212));
		
		JPanel panelMain = new JPanel(new GridLayout(4,1));
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 700, 400);
		this.add(panelMain);
		
		JLabel title = new JLabel("Parameters :", 0);
		title.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title);

		if(version) {
			result = pluginName;
			
			GridLayout g1 = new GridLayout(1,4);
			g1.setHgap(20);
			JPanel date = new JPanel(g1);
			date.setBackground(Color.white);
			panelMain.add(date);
			
			date.add(none);
			date.add(perDays);
			date.add(perWeeks);
			date.add(perMonths);
			
			GridLayout g2 = new GridLayout(1,2);
			g1.setHgap(60);
			JPanel options = new JPanel(g2);
			options.setBackground(Color.white);
			panelMain.add(options);
			options.add(perAuthor);
			options.add(forAllBranches);
		} else {
			result = pluginName;
			
			GridLayout g = new GridLayout(4,1);
			g.setVgap(30);
			panelMain.setLayout(g);
			
			GridLayout g1 = new GridLayout(1,2);
			g1.setHgap(60);
			JPanel panelID = new JPanel(g1);
			panelID.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
			panelID.setBackground(Color.white);
			
			JLabel subtitle1 = makeBeautifulLabel("Project ID :");
			panelID.add(subtitle1);
			panelID.add(projectId);
			panelMain.add(panelID);
			
			JPanel panelToken = new JPanel(g1);
			panelToken.setBackground(Color.white);
			
			JLabel subtitle2 = makeBeautifulLabel("Private Token :");
			panelToken.add(subtitle2);
			panelToken.add(privateToken);
			panelMain.add(panelToken);
		}
		
		GridLayout g3 = new GridLayout(1,2);
		g3.setHgap(50);
		JPanel button = new JPanel(g3);
		button.setBackground(Color.white);
		button.add(back);
		button.add(submit);
		panelMain.add(button);
		
		back.addActionListener((event) -> {
			CLIM.changeToCliPlugin();
		});
		
		submit.addActionListener((event) -> {
			//this.dispose();
			try {
				submitMethode();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
		}});
	}
	
	
	public void DateIsSelected() {
		perAuthor.setEnabled(true);
		perAuthor.setFont(new Font("Monica", Font.PLAIN, 15));
	}
	
	public void NoDate() {
		perAuthor.setSelected(true);
		perAuthor.setEnabled(false);
		perAuthor.setFont(new Font("Monica", Font.ITALIC, 15));
	}
	
	public JRadioButton makeBeautifulRadioButton(String s) {
		JRadioButton res = new JRadioButton(s);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		res.setHorizontalAlignment(0);
		res.setOpaque(false);
		res.addActionListener((event) -> DateIsSelected());
		perDate.add(res);
		return res;
	}
	
	
	public JButton makeABeautifulButton(String name) {
		JButton b = new JButton(name);
		b.setBackground(new Color(180, 211, 212));
		return b;
	}
	
	public JLabel makeBeautifulLabel(String s) {
		JLabel res = new JLabel(s, 0);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		return res;
	}
	
	
	public void submitMethode() throws IOException, URISyntaxException{
		if(version) {
			if(perAuthor.isEnabled() && perAuthor.isSelected()) result += "PerAuthor";
			
			if(perDays.isSelected()) result += "PerDays";
			else if (perWeeks.isSelected()) result += "PerWeeks";
			else if(perMonths.isSelected()) result += "PerMonths";
			
			
			if(forAllBranches.isSelected()) result += "ForAllBranches";
			CLIM.addPlugin(result);
		}
		
		else {
			CLIM.addPlugin(result);
			CLIM.addPrivateToken(privateToken.getText());
			CLIM.addProjectID(projectId.getText());

		}
		
		CLIM.changeToMenuLast();

	}

	
}

package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class CliMenuParameter extends JFrame {
	private String [] result;
	private int version;
	
	private JButton back = makeABeautifulButton("BACK");
	private JButton submit = makeABeautifulButton("SUBMIT");

	//CheckBox and RadioButton that will appear when the selected plugin have more parameter
	private ButtonGroup perDate = new ButtonGroup();
	private JRadioButton perDays = makeBeautifulRadioButton("PerDays");
	private JRadioButton perWeeks = makeBeautifulRadioButton("PerWeeks");
	private JRadioButton perMonths = makeBeautifulRadioButton("PerMonths");
	
	private JCheckBox perAuthor = makeBeautifulCheckBox("PerAuthor");
	private JCheckBox forAllBranches = makeBeautifulCheckBox("ForAllBranches");
	
	//Texte Area needed to be fill For plugins using API
	private JTextField projectId = new JTextField();
	private JTextField privateToken = new JTextField();

	
	public CliMenuParameter(String pluginName, int ver) {
		version = ver;

		this.setTitle("GenGit Scan");
		this.setAlwaysOnTop(true);
		this.setSize(800,500);
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(new Color(180, 211, 212));
		this.setResizable(false);
		
		JPanel panelMain = new JPanel(new GridLayout(4,1));
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 700, 400);
		this.getContentPane().add(panelMain);
		
		JLabel title = new JLabel("Parameters :", 0);
		title.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title);

		if(version == 1) {
			result = new String[1];
			result[0] = pluginName;
			
			GridLayout g1 = new GridLayout(1,3);
			g1.setHgap(20);
			JPanel date = new JPanel(g1);
			date.setBackground(Color.white);
			panelMain.add(date);
			
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
			result = new String[3];
			result[0] = pluginName;
			
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
			this.dispose();
			new CliMenuPlugin();
		});
		
		submit.addActionListener((event) -> {
			this.dispose();
			try {
				submitMethode();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
		}});
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JRadioButton makeBeautifulRadioButton(String s) {
		JRadioButton res = new JRadioButton(s);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		res.setHorizontalAlignment(0);
		res.setBackground(new Color(0,0,0,0));
		perDate.add(res);
		return res;
	}
	
	public JCheckBox makeBeautifulCheckBox(String s) {
		JCheckBox res = new JCheckBox(s);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		res.setHorizontalAlignment(0);
		res.setBackground(new Color(0,0,0,0));
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
		if(version == 1) {
			if(perAuthor.isSelected()) result[0] += "PerAuthor";
			
			if(perDays.isSelected()) result[0] += "PerDays";
			else if (perWeeks.isSelected()) result[0] += "PerWeeks";
			else if(perMonths.isSelected()) result[0] += "PerMonths";
			
			
			if(forAllBranches.isSelected()) result[0] += "ForAllBranches";
		}
		
		else {
			result[1] = "--privateToken=" + privateToken.getText();
			result[2] = "--projectId=" + projectId.getText();

		}
		
		CLILauncher.launch(result);

	}

	
}

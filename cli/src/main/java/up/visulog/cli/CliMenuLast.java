package up.visulog.cli;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CliMenuLast extends JPanel{
	private CLIMenu CLIM;
	
	public CliMenuLast(CLIMenu CLIM) {
		this.CLIM = CLIM;
		
		this.setBackground(new Color(180, 211, 212));
		
		
		JButton execute = new JButton("execute");
		JButton addNewPlugin = new JButton("addNewPlugin");
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
		this.add(addNewPlugin);
		this.add(execute);
	}
	
}

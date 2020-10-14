package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

// a library that allows you to read an inputReader (for example an FileReader or an InputStreamReader)
import java.io.BufferedReader;

//a library that allows you to read a File, creates an FileReader 
//A FileReader alone is useless, we need to use it in a BufferedReader
import java.io.FileReader;

// a library that allows you to write to files
import java.io.FileWriter;

//When we have a java error when we open a file or write to a file, it creates an IOException
//So we need to catch it, that's why I use the couple try,catch
import java.io.IOException;

import java.nio.file.FileSystems;

//libraries that allow you to find files thanks to their path
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Optional;

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toHTML());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                        case "--addPlugin":
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits")) plugins.put("countCommits", new PluginConfig() {
                            });

                            break;
                        case "--loadConfigFile":
                        	//Format command: --loadConfigFile=Path to file where you want load the command
                        	//Example: ./gradlew run --args='--loadConfigFile=../config.txt'
                        	//this command loads the command that is in ../config.txt
                        	return makeConfigFromCommandLineArgs(loadConfig(pValue));
                        	
                        case "--justSaveConfigFile":
                        	//Format command: --justSaveConfigFile=Path to file where you want save the command
                        	//Example: ./gradlew run --args='--addPlugin=countCommits --justSaveConfigFile=../config.txt'
                        	//this command saves "--addPlugin=countCommits" in ../config.txt
                        	String command = saveConfig(args,pValue);
                        	System.out.println("Command: " + command + ", is saved");
                        	System.exit(0);
                        default:
                            return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }
    
    //this function save the command in the file whose path is "path"
    private static String saveConfig(String[] args, String path) {
    	String content = "";
    	for(int i = 0; i < args.length; i++) {
    		if(!args[i].equals("--justSaveConfigFile=" + path)) content += args[i] + " ";
    	}
    	content = content.substring(0, content.length() - 1);
    	try {
    		FileWriter file = new FileWriter(path);
    		file.write(content);
    		file.close();
    		return content;
    	} catch(IOException e) {
    		throw new RuntimeException("Error SaveConfig", e);
    	}
    }
    
    private static String[] loadConfig(String path) {
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(Paths.get(path).toFile()));
    		String line = reader.readLine();
    		String[]args = line.split(" ");
    		return args;
    	}catch(IOException e) {
    		throw new RuntimeException("Error loadConfig",e);
    	}
    }
    

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}

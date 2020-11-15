package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;

// a library that allows you to read an inputReader (for example an FileReader or an InputStreamReader)
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import java.util.Scanner;

public class CLILauncher {
	private String[] pluginWithoutAPI = {"countCommits","countCommitsForAllBranchs","countMergeCommits","countCommitsPerMonths","countCommitsPerMonthsForAllBranchs",
										"countCommitsPerWeeks","countCommitsPerWeeksForAllBranchs","countCommitsPerDays","countCommitsPerDaysForAllBranchs",
										"countLinesDeleted","countLinesDeletedForAllBranchs","countLinesAdded","countLinesAddedForAllBranchs","countLinesAddedPerDays",
										"countLinesAddedPerDaysForAllBranchs","countLinesAddedPerWeeks","countLinesAddedPerWeeksForAllBranchs","countLinesAddedPerMonths",
										"countLinesAddedPerMonthsForAllBranchs","countLinesDeletedPerDays","countLinesDeletedPerDaysForAllBranchs","countLinesDeletedPerWeeks",
										"countLinesDeletedPerWeeksForAllBranchs","countLinesDeletedPerMonths","countLinesDeletedPerMonthsForAllBranchs","countContribution",
										"countLinesAddedPerAuthorPerDays","countLinesAddedPerAuthorPerDaysForAllBranchs","countLinesAddedPerAuthorPerWeeks",
										"countLinesAddedPerAuthorPerWeeksForAllBranchs","countLinesAddedPerAuthorPerMonths","countLinesAddedPerAuthorPerMonthsForAllBranchs",
										"countLinesDeletedPerAuthorPerDays","countLinesDeletedPerAuthorPerDaysForAllBranchs","countLinesDeletedPerAuthorPerWeeks","countLinesDeletedPerAuthorPerWeeksForAllBranchs",
										"countLinesDeletedPerAuthorPerMonths","countLinesDeletedPerAuthorPerMonthsForAllBranchs"};
	private String[] pluginWithAPI = {"countComments","getMembers","getExtensions","countIssues"}
	public boolean in(String p,String[] arr){
		for(String s:arr){
			if(p.equals(s)) return true;
		}
		return false;
	}
    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toHTML());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath("../");
        var plugins = new HashMap<String, PluginConfig>();
        String pPrivateToken = "";
        int pProjectId = -1;
        boolean API = false;
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                        case "--addPlugin":
                        	if(in(pValue,pluginWithoutAPI)){
								plugins.put(pValue, new PluginConfig() {});
								break;
							}
                        	else if(in(pValue,pluginWithAPI)){
								plugins.put(pValue, new PluginConfig() {});
								API = true;
								break;
							}
                        	break;
   
                        case "--load":
                        	//Format command: --load=name of the config
                        	//Example: ./gradlew run --args='--load=test'
                        	//this command loads the command "test" that is in ../config.txt
                        	return makeConfigFromCommandLineArgs(loadConfig(pValue));
                        	
                        case "--save":
                        	//Format command: --save=name of the config
                        	//Example: ./gradlew run --args='--addPlugin=countCommits --save=test'
                        	//this command saves "--addPlugin=countCommits" in ../config.txt
                        	String command = saveConfig(args,pValue);
                        	System.out.println("Command: " + command + ", is saved");
                        	System.exit(0);	
                        case "--privateToken":
                            pPrivateToken = pValue;
                            break;
                        case "--projectId":
                            pProjectId = Integer.parseInt(pValue);
                            break;
                        default:
                            return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        if(API && (pProjectId==-1 || pPrivateToken.equals(""))){
            return Optional.empty();
        }
        return (API)?Optional.of(new Configuration(pPrivateToken,plugins,pProjectId)):Optional.of(new Configuration(gitPath, plugins));
    }
    
    //this function save the command in the file whose path is "path"
    private static String saveConfig(String[] args, String name) {
    	String content = "";
    	for(int i = 0; i < args.length; i++) {
    		if(!args[i].equals("--save=" + name)) content += args[i] + " ";
    	}
    	if(content.length() != 0) {
    		content = content.substring(0, content.length() - 1);
    	}
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader("../config.txt"));
    		String line;
    		String oldContent = "";
    		while((line = reader.readLine()) != null) {
    			Scanner sc = new Scanner(line);
    			if(sc.hasNext()) {
    				String n = sc.next();
    				if(!n.equals(name)) {
    					oldContent += line + "\n";
    				}
    				else System.out.println("Warning! old configuation :" + name + " overwitted!");
    			}
    			sc.close();
    		}
    		FileWriter file = new FileWriter("../config.txt");
    		file.write(oldContent + name + " " + content + "\n");
    		file.close();
    		return content;
    	} catch(IOException e) {
    		throw new RuntimeException("Error SaveConfig", e);
    	}
    }
    
    private static String[] loadConfig(String name) {
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(Paths.get("../config.txt").toFile()));
    		String line;
    		while((line = reader.readLine()) != null) {
    			Scanner sc = new Scanner(line);
    			if(sc.hasNext() && sc.next().equals(name)) {
    				sc.close();
    				break;
    			}
    			sc.close();
    		}
    		if(line == null) {
    			//error message
    			System.out.println("Config not found, you should save it first using: \n./gradlew run --args=' Your command --save="+name+"'");
    			String [] fill= {"--"};
    			return fill;
    		}
    		line = line.substring(name.length() + 1);
    		String[]args = line.split(" ");
    		return args;
    	}catch(IOException e) {
    		throw new RuntimeException("Error loadConfig",e);
    	}
    }
    

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.out.println("Different options: ");
        System.out.println("--addPlugin allows you to add new plugins");
        System.out.println("--loadConfigFile allows you to load options from a file");
        System.out.println("--justSaveConfigFile save command line options to a file instead of running the analysis");
        System.exit(0);
    }
}

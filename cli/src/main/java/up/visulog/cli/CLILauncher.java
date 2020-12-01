package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.awt.Desktop;
// a library that allows you to read an inputReader (for example an FileReader or an InputStreamReader)
import java.io.*;
//a library that allows you to read a File, creates an FileReader
//A FileReader alone is useless, we need to use it in a BufferedReader

// a library that allows you to write to files

//When we have a java error when we open a file or write to a file, it creates an IOException
//So we need to catch it, that's why I use the couple try,catch
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;

//libraries that allow you to find files thanks to their path
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class CLILauncher {

	private static boolean graphicOver = false;
	private static String argument[] = new String[1];
	
	public static void setArgument(String A, int i) {
		argument[i] = A;
	}
	
	public static void setGraphicOver(boolean B) {
		graphicOver = B;
	}
	
    public static void main(String[] args) throws IOException, URISyntaxException {
    	if(args.length == 0) {
    		new CliMenuPlugin();
    		while(!graphicOver){
    			try {
					Thread.sleep(1000);
					System.out.println("X");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		System.out.println(argument[0]);
   			launch(argument);
    	}
    	else launch(args);
    }
    
    public static void launch(String [] args) throws IOException, URISyntaxException {
	    var config = makeConfigFromCommandLineArgs(args);
	    if (config.isPresent()) {
	        var analyzer = new Analyzer(config.get());
			var results = analyzer.computeResults();
			var wg = new WebGen();
			results.toHTML(wg);
			wg.write();
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().open(new File("../htmlResult/index.html"));
			}
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
                        	switch(pValue) {
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:
                            
                                case "countCommits":
                            		plugins.put("countCommits", new PluginConfig() {});
                            		break;
                                case "countCommitsForAllBranches":
                            		plugins.put("countCommitsForAllBranches", new PluginConfig() {});
                            		break;
                            		
                            	case "countMergeCommits":
                            		plugins.put("countMergeCommits", new PluginConfig() {});
                            		break;
                            	case "countComments":
                            		plugins.put("countComments", new PluginConfig() {});
                            		API = true;
                            		break;
                            	case "getMembers":
                            		plugins.put("getMembers", new PluginConfig() {});
                            		API = true;
                            		break;
								case "getExtensions":
									plugins.put("getExtensions", new PluginConfig() {});
									API = true;
									break;
                            	case "countCommitsPerMonths":
                            		plugins.put("countCommitsPerMonths", new PluginConfig() {});
                            		break;
                            	case "countCommitsPerMonthsForAllBranches":
                            		plugins.put("countCommitsPerMonthsForAllBranches", new PluginConfig() {});
                            		break;
                            	case "countCommitsPerWeeks":
                            		plugins.put("countCommitsPerWeeks", new PluginConfig() {});
                            		break;
                            	case "countCommitsPerWeeksForAllBranches":
                            		plugins.put("countCommitsPerWeeksForAllBranches", new PluginConfig() {});
                            		break;
                            	case "countCommitsPerDays":
                            		plugins.put("countCommitsPerDays", new PluginConfig() {});
                            		break;
                            	case "countCommitsPerDaysForAllBranches":
                            		plugins.put("countCommitsPerDaysForAllBranches", new PluginConfig() {});
                            		break;
                            		
                            	case "countIssues":
									plugins.put("countIssues", new PluginConfig() {});
									API = true;
									break;
                            		
                            	case "countLinesDeleted":
                            		plugins.put("countLinesDeleted", new PluginConfig() {});
                            		break;
                            	case "countLinesDeletedForAllBranches":
                            		plugins.put("countLinesDeletedForAllBranches", new PluginConfig() {});
                            		break;
                            	case "countLinesAdded":
                            		plugins.put("countLinesAdded", new PluginConfig() {});
                            		break;
                            	case "countLinesAddedForAllBranches":
                            		plugins.put("countLinesAddedForAllBranches", new PluginConfig() {});
                            		break;
                            		
                            	case "countLinesAddedPerDays":
                            	    plugins.put("countLinesAddedPerDays", new PluginConfig() {});
                            	    break;
                            	case "countLinesAddedPerDaysForAllBranches":
                           	     	plugins.put("countLinesAddedPerDaysForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerWeeks":
                            	    plugins.put("countLinesAddedPerWeeks", new PluginConfig() {});
                            	    break;
                            	case "countLinesAddedPerWeeksForAllBranches":
                           	     	plugins.put("countLinesAddedPerWeeksForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerMonths":
                            	    plugins.put("countLinesAddedPerMonths", new PluginConfig() {});
                            	    break;
                            	case "countLinesAddedPerMonthsForAllBranches":
                           	     	plugins.put("countLinesAddedPerMonthsForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerDays":
                            	    plugins.put("countLinesDeletedPerDays", new PluginConfig() {});
                            	    break;
                            	case "countLinesDeletedPerDaysForAllBranches":
                           	     	plugins.put("countLinesDeletedPerDaysForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerWeeks":
                            	    plugins.put("countLinesDeletedPerWeeks", new PluginConfig() {});
                            	    break;
                            	case "countLinesDeletedPerWeeksForAllBranches":
                           	     	plugins.put("countLinesDeletedPerWeeksForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerMonths":
                            	    plugins.put("countLinesDeletedPerMonths", new PluginConfig() {});
                            	    break;
                            	case "countLinesDeletedPerMonthsForAllBranches":
                           	     	plugins.put("countLinesDeletedPerMonthsForAllBranches", new PluginConfig() {});
                           	     	break;            	     	
                            	case "countLinesAddedPerAuthorPerDays":
                           	     	plugins.put("countLinesAddedPerAuthorPerDays", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerAuthorPerDaysForAllBranches":
                           	     	plugins.put("countLinesAddedPerAuthorPerDaysForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerAuthorPerWeeks":
                           	     	plugins.put("countLinesAddedPerAuthorPerWeeks", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerAuthorPerWeeksForAllBranches":
                           	     	plugins.put("countLinesAddedPerAuthorPerWeeksForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerAuthorPerMonths":
                           	     	plugins.put("countLinesAddedPerAuthorPerMonths", new PluginConfig() {});
                           	     	break;
                            	case "countLinesAddedPerAuthorPerMonthsForAllBranches":
                           	     	plugins.put("countLinesAddedPerAuthorPerMonthsForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerDays":
                           	     	plugins.put("countLinesDeletedPerAuthorPerDays", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerDaysForAllBranches":
                           	     	plugins.put("countLinesDeletedPerAuthorPerDaysForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerWeeks":
                           	     	plugins.put("countLinesDeletedPerAuthorPerWeeks", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerWeeksForAllBranches":
                           	     	plugins.put("countLinesDeletedPerAuthorPerWeeksForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerMonths":
                           	     	plugins.put("countLinesDeletedPerAuthorPerMonths", new PluginConfig() {});
                           	     	break;
                            	case "countLinesDeletedPerAuthorPerMonthsForAllBranches":
                           	     	plugins.put("countLinesDeletedPerAuthorPerMonthsForAllBranches", new PluginConfig() {});
                           	     	break;
                            	case "countContribution":
                            		plugins.put("countContribution", new PluginConfig() {});
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
            	String path = arg;
            	if(!arg.startsWith("/")) path = "../" + path;
            	if(!isGitDirectory(path)) displayHelpAndExit();
                gitPath = FileSystems.getDefault().getPath(path);
            }
        }
        if(API && (pProjectId==-1 || pPrivateToken.equals(""))){
            return Optional.empty();
        }
        return (API)?Optional.of(new Configuration(pPrivateToken,plugins,pProjectId)):Optional.of(new Configuration(gitPath, plugins));
    }
    
    private static boolean isGitDirectory(String path) {
    	File file = new File(path);
    	if(!file.exists()) {
    		if(!path.startsWith("/")) path = path.substring(3);
    		System.out.println("Error to find the file " + path);
    		return false;
    	}
    	
    	if(!file.isDirectory()) {
    		if(!path.startsWith("/")) path = path.substring(3);
    		System.out.println("the file : " + path + " is not a directory !");
    		return false;
    	}
    	
    	File[] files = file.listFiles();
    	for(File f : files) {
    		if(f.getName().equals(".git")) {
    			return true;
    		}
    	}
    	if(!path.startsWith("/")) path = path.substring(3);
		System.out.println("the file : " + path + " is not a git directory !");
		return false;
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
        System.out.println("Different options: ");
        System.out.println("--addPlugin=nameOfPlugin allows you to add new plugins");
        System.out.println("--load=nameOfOption allows you to load options from a file");
        System.out.println("--save=nameOfOption save command line options to a file instead of running the analysis");
        System.exit(0);
    }
}

package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
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
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits")) plugins.put("countCommits", new PluginConfig() {
                            });
                            if (pValue.equals("countMergeCommits")) plugins.put("countMergeCommits", new PluginConfig() {
                            });
                            if (pValue.equals("countCommitsPerMonths")) plugins.put("countCommitsPerMonths", new PluginConfig() {
                            });
                            if (pValue.equals("countCommitsPerDays")) plugins.put("countCommitsPerDays", new PluginConfig() {
                            });
                            if (pValue.equals("countCommitsPerWeeks")) plugins.put("countCommitsPerWeeks", new PluginConfig() {
                            });
                            if (pValue.equals("countComments")){
                                plugins.put("countComments", new PluginConfig() {
                                });
                                API = true;
                            }
                            if(pValue.equals("getMembers")){
                                plugins.put("getMembers", new PluginConfig() {
                                });
                                API = true;
                            }
                            if(pValue.equals("countIssues")){
                                plugins.put("countIssues", new PluginConfig() {
                                });
                                API = true;
                            }
                            break;
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            break;
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

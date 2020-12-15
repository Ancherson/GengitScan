package up.visulog.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
  * This class is the core of the configuration
  * in order to analyze the data we get from git
  */
public class Configuration {

    private Path gitPath;
    private final Map<String, PluginConfig> plugins;
    private String privateToken;
    private int idProject;
    private int idIssue;

    /**
     * This constructor was made for plugins that use git command to get Result
     * @param gitPath The path of the git repository in local
     * @param plugins plugins that are going to be executed
     */
    public Configuration(Path gitPath, Map<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins);
    }

    /**
     * this constructor was made for plugins that use API
     * @param privateToken privateToken in order to auth to the git repository that you want to analyse
     * @param plugins plugins that are going to be executed
     * @param idProject id of the project that you want to analyse
     * @param idIssue id of issue that you want to analyse (it was made if you wanted to get comments in only one issue)
     */
    public Configuration(String privateToken,Map<String, PluginConfig> plugins,int idProject,int idIssue){
        this.privateToken = privateToken;
        this.idProject = idProject;
        this.plugins = Map.copyOf(plugins);
        this.idIssue = idIssue;
    }

    /**
     * This is the main constructor because we can use both plugins that use API and others with command
     * @param gitPath The path of the git repository in local
     * @param privateToken privateToken in order to auth to the git repository that you want to analyse
     * @param plugins plugins that are going to be executed
     * @param idProject id of the project that you want to analyse
     */
    public Configuration(Path gitPath,String privateToken,Map<String, PluginConfig> plugins,int idProject){
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins);
        this.privateToken = privateToken;
        this.idProject = idProject;
    }

    /**
     * This constructor was made for plugins that use API
     * @param privateToken privateToken in order to auth to the git repository that you want to analyse
     * @param plugins plugins that are going to be executed
     * @param idProject id of the project that you want to analyse
     */
    public Configuration(String privateToken,Map<String, PluginConfig> plugins,int idProject){
        this(privateToken,plugins,idProject,-1);
    }

    /**
     * return the id of the issue that we want to analyse
     * @return the id of the issue that we want to analyse
     */
    public int getIdIssue() {
        return idIssue;
    }

    /**
     * return the id of the project that we want to analyse
     * @return the id of the project that we want to analyse
     */
    public int getIdProject() {
        return idProject;
    }

    /**
     * return a map of plugins that we re going to execute
     * @return a map of plugins that we re going to execute
     */
    public Map<String, PluginConfig> getPlugins() {
        return plugins;
    }

    /**
     * return your private token as String that you use to auth in a project
     * @return your private token as String that you use to auth in a project
     */
    public String getPrivateToken() {
        return privateToken;
    }

    /**
     * return the path of the git repository in your local pc
     * @return the path of the git repository in your local pc
     */
    public Path getGitPath() {
        return gitPath;
    }

    /**
     * return a map of plugins that we re going to execute
     * @return a map of plugins that we re going to execute
     */
    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }
}

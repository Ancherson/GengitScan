package up.visulog.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configuration {

    private Path gitPath;
    private final Map<String, PluginConfig> plugins;
    private String privateToken;
    private int idProject;
    private int idIssue;
    public Configuration(Path gitPath, Map<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins);
    }
    public Configuration(String privateToken,Map<String, PluginConfig> plugins,int idProject,int idIssue){
        this.privateToken = privateToken;
        this.idProject = idProject;
        this.plugins = Map.copyOf(plugins);
        this.idIssue = idIssue;
    }
    public Configuration(String privateToken,Map<String, PluginConfig> plugins,int idProject){
        this(privateToken,plugins,idProject,-1);
    }

    public int getIdIssue() {
        return idIssue;
    }

    public int getIdProject() {
        return idProject;
    }

    public Map<String, PluginConfig> getPlugins() {
        return plugins;
    }

    public String getPrivateToken() {
        return privateToken;
    }
    public Path getGitPath() {
        return gitPath;
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }
}

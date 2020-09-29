#Documentation des fichiers JAVA de Config 

-------------------------------------------

##CLASSE Configuration

Cette classe contient :

-Champs :
    - gitPath de type Path
    L'interface Path permet d'acceder à un fichier ou un dossier en particulier en tracant soit son chemin relatif, soit son chemin absolu.
    - plugins de type Map<String, PluginConfig> dont le type de Keys maintenu 
    par map est String et le Value est l'interface PluginConfig, determine dans 
    le package up.visulog.config

-Constructeur :
    - prend en argument : 1. gitPath de type Path
                          2. plugins de type Map<String,PluginConfig>  

-Methode : 
    1. Path getGitPath()
    renvoie gitPath
    2. Map<String, PluginConfig> getPluginConfigs()
    renvoie plugins

##CLASSE PluginConfig

Cette interface reste à définir mais devrait être de type Map **To do**

##CLASSE TestConfiguration

Devra contenir la classe static main pour effectuer le test lorsque la classe config ne contiendra pas que des getters **To do**
    

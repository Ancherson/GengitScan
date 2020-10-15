# Documentation Des Fichiers Java Du Dossier Analyser

---------------------------------------------------------

## INTERFACE AnalyserPlugin

Cette interface contient:

- une autre Interface Result qui contient :
  - 2 Methodes : String getResultAsString() et String getResultAsHtmlDiv()
  
- 2 Methodes : 
  - void run()  
    qui execute le plugin
		  
  - Result getResult()  
    qui renvoie le resultat de l'analyse
	
Ces Methodes seront à définir dans les autres classes qui implémentent cette interface
	
------------------------------------------------------------

## CLASSE AnalyserResult

Cette classe contient :

- Champs :
  - subResult  
    une liste de resultats de type Result (List<AnalyzerPlugin.Result>)
    
- Constructeur :
  - Prend en argument la Liste de résultat
 	
- Methodes :
  - List<AnalyzerPlugin.Result> getSubResults()  
    renvoie la liste de resultats
 	  
  - String toString()  
    renvoie la liste subResults sous la forme d'un String
 	  
  - String toHTML()  
    renvoie la liste subResults sous la forme d'un String avec des balises html
    
--------------------------------------------------------------

## CLASSE CountCommitsPerAuthorPlugin

Implemente l'interface **AnalyserPlugin**, donc est aussi de type AnalyserPlugin

Cette Classe contient : 

- une Autre Classe static **Result** qui implemente la interface Result contenue dans l'interface AnalyserPlugin :
  - Elle contient :
    - Champs : commitsPerAuthor qui est une HashMap(String, Integer)  
     Une HashMap est une structure de données qui lie a chaque VALUE une KEY  
     Dans ce cas là les KEY sont les nom des Auteurs des commits et les VALUE sont le nombre de Commits associés à l'Auteur
    
   - Methode : 
     - Map<String, Integer> getCommitsPerAuthor()  
       renvoie commitsPerAuthor
  
  - Methodes Interfaces :
    - String getResultAsString()  
      renvoie commitsPerAuthor sous la forme d'un String
      
    - String getResultAsHtmlDiv()  
      renvoie commitsPerAuthor sous la forme d'une liste html
     
- Champs :
  - configuration de type Configuration  
    La configuration contient le chemin vers les fichier a analyser
    
  - result de type Result
  
- Constructeur : 
  - Prend en argument la configuration de type Configuration

- Methode :
  - Result processLog(List<Commit> gitLog) 
    prend en argument une liste de de commits et renvoie un Result qui contient la HashMap
    associant les auteurs des commits avec leur nombre de commits
   
- Methode Interface :
  - void run() 
    mets dans son attribut result le resultat de *processLog()*, la liste de Commits mis en argument de *processlog()* est récupérer grace a
    configuration qui contient le chemin vers les fichiers a analyser
 	  
  - Result getResult() 
    si y'a rien dans result execute run, puis renvoie result
 
--------------------------------------------------------------

## CLASSE Analyser

Cette Classe contient : 

- Champs : 
  - config de type Configuration
  - result de type AnalyserResult

- Constructeur :
  - Prend en Argument config

- Methode : 
  - Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) 
    Optional est un type qui permet d'encapsuler un autre type, cette encapsuleur permet de renvoyer Optional Vide et différent de null
    Actuellement, il ne regarde que pluginName et si il correspond à "countCommit" il creer un CountCommitPerAuthorPlugin avec 
    la configuration de l'Analyser mis en argument du constructeur  
    pluginConfig ne sert à rien actuellement
 	  
  - AnalyzerResult computeResults()
    Parcourt d'abord la Hashmap de plugins contenue dans config, puis creer les plugins correspondant grace a makePlugin et les stocke dans 
    une liste. Ensuite il execute tous les plugins et stocke les resultats dans la liste de resultats contenue dans AnalyserResult

- TO DO :
  - Trouver un moyen d'executer tous les plugin en meme temps
  - Trouver un moyen pour que la liste de Plugins ne soient "hardcoded" dans Configuration
  
--------------------------------------------------------------

## CLASSE TestCountCommitsPerAuthorPlugin

Cette classe teste la classe CountCommitsPerAuthorPlugin

--------------------------------------------------------------

## RESUME

Lors de la création d'un Analyser, on met une config (de type Configuration) en argument, qui contient une liste de plugin et le chemin vers le fichier a analyser. Sa fonction *computeResult()* va parcourir la liste de plugin qui est dans configuration et creee une liste de plugin qu'il creee grace a *makeplugin()* qui envoie la configuration au constructeur du plugin. Actuellement il n'y a qu'un seul plugin qui est *countCommits*. Ensuite il les execute tous et stocke leur resultat dans la liste de resultat contenue dans son attribut AnalyserResult.
    
 

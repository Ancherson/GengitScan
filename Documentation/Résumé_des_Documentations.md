# Présentation du projet
**Nom du projet : GenGit Scan**

**Nom du groupe : Les Khagans**

**Liste des membres (8) :**
- Ismail Badaoui
- Jules Cherion
- Alexandre Leymarie
- Etienne Nedjal
- Yoan Rougeolle
- Victor Spehar
- Elody Tang
- Xuewei Zhang

--------------------------------------------------------------------------

# Documentation des dossiers

#### Dossier GitRawData
Il s'occupe de parcourir _les logs des commits_. Il crée ensuite des objectifs de type Commit qui pourront être traîté par le reste du programme.

- 3 classes (Commit, BuildCommit, TesteCommit)
- 5 attributs (id, author, date, description, mergedFrom)

- **Problème 1 : 3 TODOs et 1 FIXME**
- **Problème 2 : Le dossier a été modifiée dans une branche fonctionnelle (10-try-out-jgit)**
     - La classe BuildCommit a été supprimée.
     - La classe Commit a été modifiée (pas d'attribut mergeFrom et changement des méthodes).
     - Utilisation de la bibliothèque jGit.
     
- Chemin de la documentation du dossier : Documentation/documentation_gitrawdata.md
- Chemin de la documentation de la branche : Documentation/Documentation_Branches/Documentation_try-out-jgit.md
     
-------------------------------------------------------------------------
#### Dossier Config
Il crée des objets de type Configuration

- 3 classes (Configuration, BuildConfiguration, TesteConfiguration)
- 2 attributs (Path gitPatch, Map<String, PluginConfig> plugins)

Définition de Path : Interface qui permet d'accéder à un fichier ou à un document en traçant son chemin relatif ou absolu.

- **Problème 1 : 1 TODO**
- Chemin de la documentation du dossier : Documentation/Documentation_Config.md

-------------------------------------------------------------------------
#### Dossier Analyser
On appelle le constructeur de Analyser avec un paramètre : config (de type Configuration).
Lors de la création d'un objet de type _Analyser_, la fonction _computeResult()_ va parcourir la liste de _plugins_ et va créer une nouvelle liste de _plugins_ grâce à sa fonction _makeplugin()_. Cette dernière va envoyer _config_ au construteur de Plugin.

- Une interface AnalyserPlugin
- 2 classes (Analyser, AnalyserResult)
- 2 attributs (Configuration config, AnalyserResult result)
- 2 Classe/Plugin déjà faits (CountCommitsParAuthorPlugin extends AnalyserPlugin, TestCountCommitsPerAuthorPlugin)
     - compte le nombre de commits par personne.
     - pour exécuter : ./gradlew run --args='. --addPlugin=countCommits'
     
- **Problème 1 : 2 TODOs**
- Chemin de la documentation sur le dossier : Documentation/Documentation_Analyser.md

-------------------------------------------------------------------------

# Documentation des branches 

Il y a deux documentations de branches.
- L'une **Documentation_try_out_jgit** a déjà été expliquée plus haut.
- La deuxième : **Documentation_parse_git_log_**
     - L'issue correspondante a été closed (pas vraiment d'utilité).
     - Chemin de la documentation de la branche : Documentation/Documentation_Branches/Documentation_parse_git_log.md
     
-------------------------------------------------------------------------

# Documentation sur les codes de Git

Il y a un fichier de documentation sur tous les codes simples de Git.
- **instructions.md**

Vous pourrez aussi y trouver des informations sur le clonage, le forkage et l'exécution du projet.

Chemin de la documentation : Documentation/instructions.md


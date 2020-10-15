# Documentation sur les fichier du répertoire Cli

-------------------------------------------------------------------
## La classe CLILauncher 

Cette classe contient 2 methodes et 1 main :

### makeConfigFromCommandLineArgs :

Elle prend en argument un tableau de String. 

- Elle gère les options ajoutées en arguments lors du lancement en allant chercher les plugins correspondants.
- Si il n'y a pas d'arguments alors elle lance le programme en [default].

Elle utilise pour ça 3 bibliothèques: 
- java.nio.file.FileSystems : permet d'accéder à des fichier et de récuperer le "path" du fichier ou du répertoire.
- java.util.HashMap : permet de créer un "couple" [(clef, valeur)].
- java.util.Optional : permet de  stocker une valeur [null] ou [non-null] de plein de [type] différents.




-------------------------------------------------------------------

### displayHelpAndExit :

Une fonction qui indique que la commande (le paramètre) rentrée n'est pas la bonne.

Elle liste ensuite la liste de toutes les commandes (les paramètres) existantes avec leurs syntaxes et quitte.



-------------------------------------------------------------------

### main 

créer une [var] (variable prenant le type que le compilateur considère être le bon) avec l'Option entrée en argument.

- test si il y a une command qui a été tapée en argument :
	- si oui lance l'analyse avec l'Option donnée.
	- sinon indique une erreur avec *displayHelpAndExit*
	
	
-------------------------------------------------------------------


## La classe TestCLILauncher 

Elle contient une seul méthode :

### testArgumentParser 

Test 2 cas de la méthode *makeConfigFromCommandLineArgs* avec :
- un cas avec un paramètre en arguments existant
- un cas avec un paramètre en arguments non-existant

Pour cela elle utilise *assertFalse* et *assertTrue* qui servent à faire une assertion sans utilisé de [if]

Cela permet de rendre le code plus lisible.

Si il ne se passe pas ce qui était prévu alors un message d'erreur sera _print_ afin de prévenir l'utilisateur d'un problème.


-------------------------------------------------------------------

## le fichier Kotlin Script "build.gradle.kts" 

c'est un script qui lance le build du dossier Cli en mettant _CLILauncher_ en classe principale
et charge les autres fichier nécessaire en tant que "projet" qui sont _analyser_ *config* _gitrawdata_.

-------------------------------------------------------------------


## Résumé

Ce répertoire est donc celui de lancement où se trouve les classes qui permettent de lancer le programme avec ou sans argument.

	
	
	
	


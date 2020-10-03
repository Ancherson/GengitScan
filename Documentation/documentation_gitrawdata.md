# Documentation des fichiers .JAVA dans le dossier GITRAWDATA
---------------------------------------------------------------

**GITRAWDATA s'occupe de parcourir les _logs_ des commits. Il crée ensuite des objectifs de type Commit (facile à manipuler) qui pourront être traîté par le reste du programme.**

## CLASSE Commmit
_dans le fichier : visulog/gitrawdata/src/main/java/up/visulog/gitrawdata_

Cette classe permet de générer des objets de type Commit à partir de _log_.

- Il possède les attributs suivants :
     - String id;
     - String date;
     - String author;
     - String description;
     - String mergedFrom;

- Pour appeler son constructeur :

```
Commit(String id, String author, String date, String description, String mergedFrom)
```

- Il possède les méthodes suivantes :
     - parseLogFromCommand(Path) : création d'un _processBuilder_, appel la méthode _builder.start()_ et s'il n'y a pas d'erreur, renvoie un type _List<Commit>_
     - Optional<Commit> parseCommit(BufferedReader input) : parcours d'un objet de type _log_ et renvoie un objet _Commit_
     - @override toString()
     
**Dans ce fichier, il y a 3 TODOS et 1 FIXME.**

## CLASSE CommitBuilder
_dans le fichier : visulog/gitrawdata/src/main/java/up/visulog/gitrawdata_

Cette classe permet de construire des objets de type _Commit_.

- Il possède les attributs suivants :
     - String id,
     - String author,
     - String date,
     - String description,
     - String mergedFrom,
     
- Pour appeler son contructeur :
```
CommitBuilder(String id)
```

- Il possède les méthodes suivantes :
     - des _setters_ pour les attributs "author", "date", "description" et "mergeFrom"
     - createCommit() qui crée juste des objets de type _Commit_


## CLASSE testeCommit
_dans le fichier : ~/visulog/gitrawdata/src/test/java/up/visulog/gitrawdata_

Ce fichier est juste une classe de Test. Il simule la création de commit.

# Résumé des fichiers présents dans GITRAWDATA
**3 fichiers présents, dont une classe Commit qui gère les objets de type _Commit_, une classe CommitBuilder qui crée des objets de type _Commit_, une classe testeCommit qui fait les tests.**

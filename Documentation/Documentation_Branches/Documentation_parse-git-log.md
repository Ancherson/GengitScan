# Documentation de la branche 6-parse-git-log

1. **Initialement crée pour l'issue intitulée : _"Parse "git log" output into a list of POJO (each item contains the data about a single commit)" qui est maintenant "closed"_**

L'objectif de cette issue était d'analyser la sortie "git log" dans une liste de POJO (chaque élément contient les données sur un seul commit.),_Git Log_ étant une commande de Git pour voir tous les commits faits sur le projet en question.
Dans la description de l'issue du dépôt de source, l'auteur a écrit que l'objet d'un commit unique devait contenir les attributs suivants : un titre, un auteur, une date et une description. Mais que les utilisateurs pouvaient aussi joindre + de données (comme la liste des fichiers modifiés, ou même un description complète de la sortie de la commande "git diff [nom_du_fichier_]".

Dans les commentaires, il y a écrit qu'on pouvait utiliser les librairies de JGit.
La description de Jgit est expliquée ici : Documentation/Documentation_Branches/Documentation_try-out-jgit.md
La branche 10-test-try-out-JGit est a ainsi été crée ppour tester JGit.
Ici, JGit peut être très utile : il peut mettre tous les _logs_ sous formes d'objets.

Comme l'issue initiale a été décomposée, les auteurs ont fermés cette issue.
Elle a été décomposée de cette manière :
ISSUE 10 : Test-try-out-JGit (la description de la branche en question a été décrite dans un autre fichier)
ISSUE 18 : Exception when parsing a merge commit (closed, la branche en question est celle-là : 18-exception-with-parsing-a-merge-commit)

L'issue 18 a été _closed_. Mais la branche a été _merge_.
Parce que la sortie de la commande _git log_ fonctionnait, on l'utilise de cette façon-là, sur une branche :

```$ git log

commit c27ed6939d5e631234bebc4414e143188aa3327a (HEAD -> documentation, origin/documentation)
Author: (author) <(author's mail>
Date:   (date)

    commit's message

commit 107ce6d901a6fddd2b53be0ea92a1d115e60bf29
:

```

2. **Définition de POJO**

POJO = "Plain old Java Object"
C'est une classe avec des attributs. Rien de plus. L'objectif d'un POJO ne contient ni 'extends' ni 'implements' et se contente de ses déclarations propores. (C'est une classe qui ne se prend pas la tête.) Son objectif : juste contenir des données.

```
Exemple de POJO : 

public class Commit {
  public String author;
  public String date;
  public String title;
  public String message;
}

```

# Résumé de cette documentation
L'issue initiale a déjà été closed, et son test a réussi.
Il faut juste se renseigner sur JGit qui pourrait être assez intéréssant à utiliser.

Dans la branche créée pour cette issue, rien n'a été modifié. Mais maintenant, on sait qu'on peut voir la liste des commits crées avec la commande _git log_. Et qu'il faut qu'on modifie le code pour compter les résultats qui en sortent (comptage du nombre de commits).





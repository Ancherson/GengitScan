**Ce document a été créee pour les membres du gourpe. Il sert à retrouver les commandes de Git rapidement.**


# Les commandes de Git

### Installation de Git

1. Mettre à jour sa machine : `sudo apt update & sudo apt upgrade` 

2. Ouvrir un terminal et écrire : `sudo apt-get install git-all`


### Paramètres de Git

1. Écrire dans un terminal :
```
$ git config --global user.name "*name*"
$ git config --global user.email *email*
```

### Première utilisation

1. Vérifier l'état des fichiers : `git statut`

2. Placer de nouveaux fichiers sous suivi de version : `git add *nomDuFichier*`

3. Visualiser ce qui a été modifié mais pas encore indexé : `git diff`

4. Valider les modifications : `git commit -m "*message*"`

5. Déplacer des fichiers : `git mv *nomOrigine* *nomCible*`

6. Créer une nouvelle branche : `git branch *nomBranche*`

7. Fusionner la branche d'une issue spéciale : `git merge iss*nombreIssue*`

8. Récupérer la dernière version avant de push : `git pull`

9. Pousser une branche dans un serveur : `git push origin (branche)`

10. Changer de branche depuis le dépot local : `git checkout _nomdelabranche_`


# Cloner et compiler le projet

⚠️ Attention, cloner un projet est différent de télécharger un projet !!! ⚠️

### Commandes principales

1. Installer Java 10, 11, 12 ou 13.
Les anciennes versions de Java ne sont pas supportées.


2. Faire les dernières mises à jour de votre appareil.
Écrire sur un terminal : `sudo apt update & sudo apt upgrade`

3. Cloner le dépot de source de GitHub.

![alt text](https://zupimages.net/up/20/38/c0qs.png)

Vous pouvez choisissez les deux liens (HTTPS ou SSH).

- Pour une utilisation simple :
    - Ouvrir un terminal et écrire :
    ```
    git clone https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog.git
    ```

    ou

    ```
    git clone git@gaufre.informatique.univ-paris-diderot.fr:badaoui/visulog.git
    ```
    - Les fichiers se téléchargeront automatiquement.

4. Ouvrir un terminal.
- Se diriger vers le fichier 'home' avec la commande : `cd visulog`

5. Toujours sur le terminal, écrire : `./gradlew build`

Hourra ! La compression est terminée.

### Si des problèmes persistent...

Voici une liste des problèmes qui a pu se passer durant votre compression :

- Plus de réseaux internet : Vous devez disposer obligatoirement d'une connexion internet, car des fichiers vont être téléchargés.

- Vous avez supprimé l'un des fichiers par erreur : Vous devez supprimer les fichiers qu'il vous reste. Puis recommencer les démarches ci-dessus en veillant à ce qu'aucun fichier ne soit déplacé ou supprimé.

- Assurez-vous que la version de Java dont vous disposez est compatible avec la commande 'built'. Tapez 'java' sur votre terminal et regardez ce qui s'affiche, si "aucune commande" apparaît, il se peut que Java n'est pas été proprement installé, ou que la version est incompatible.
    - Rappel : Java 10, 11, 12 et 13 sont les Java compatibles.


# Fork le projet

Un fork est une copie d'un dépôt original qu'on place dans un autre espace pour pouvoir y attribuer des modifications sans affecter le projet original. 

### Commandes principales

1. Sur la page du projet principal, à droite des boutons, cliquer le bouton "Fork".

![alt text](https://docs.gitlab.com/ee/user/project/repository/img/forking_workflow_fork_button.png)

2. Choisir un nom de projet.

Le fork a été crée.

⚠️ Depuis GitLab 12.6, lorsque les propriétaires de projets réduisent la visibilité d'un projet, cela supprime les relations entre un projet et ses copies. ⚠️


# Exécuter le projet

Le projet peut actuellement être exécuté avec gradle en utilisant la commande : `./gradlew run --args='arguments'`
Le résultat variera en fonction des arguments.

Certaines commandes doivent fonctionner avec une API et des commandes spéciales. Ces dernières seront spécifiées avec une icône comme celle-ci (API).
Certaines commandes peuvent être utilisées sur la branche courante mais aussi sur toutes les branches. Il suffira d'ajouter "ForAllBranches" aux commandes. Celles qui ne pourront pas être utilisées sur toutes les branches seront marquées d'une icône (X).
Avant de tester les plugins, vous devez effectuer les commandes suivantes :

- Linux :
```
sudo apt install curl
```

- MacOS
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" < /dev/null 2> /dev/null
brew install curl
```
Vous pouvez maintenant compiler et exécuter les commandes suivantes.


--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
# Liste des arguments acceptés et leurs résultats
------------------------------------------------------------------------------


## STATISTIQUES GENERALES :

1. Liste des membres (API) (X) :
```
./gradlew run --args="--addPlugin=getMembers --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```


## ACTIVITES PAR DATE :

2. Nombre de Commits par Jour, Semaine et Mois :
```
./gradlew run --args="--addPlugin=countCommitsPerDays"
./gradlew run --args="--addPlugin=countCommitsPerWeeks"
./gradlew run --args="--addPlugin=countCommitsPerMonths"
```

3. Nombre de lignes ajoutées/supprimées par Jour, Semaine et Mois :
```
./gradlew run --args="--addPlugin=countLinesAddedPerDays"
./gradlew run --args="--addPlugin=countCommitsAddedPerWeeks"
./gradlew run --args="--addPlugin=countCommitsAddedPerMonths"

./gradlew run --args="--addPlugin=countLinesDeletedPerDays"
./gradlew run --args="--addPlugin=countCommitsDeletedPerWeeks"
./gradlew run --args="--addPlugin=countCommitsDeletedPerMonths"
```

## ACTIVITES PAR AUTEUR :

4. Nombre de Commits :
```
./gradlew run --args="--addPlugin=countCommits"
```

5. Nombre de Merge Commits (X) :
```
./gradlew run --args="--addPlugin=countMergeCommits"
```

6. Nombre de lignes ajoutées/supprimées :
```
./gradlew run --args="--addPlugin=countLinesAdded"
./gradlew run --args="--addPlugin=countLinesDeleted"
```

7. Pourcentage de contribution (X) :
```
./gradlew run --args="--addPlugin=countContribution"
```

8. Nombre de commentaires (API) (X):
```
./gradlew run --args="--addPlugin=countComments --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

9. Nombre d'issues assignées (API) (X) :
```
./gradlew run --args="--addPlugin=countIssues --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

## ACTIVITES PAR DATE ET PAR AUTEUR :

10. Nombre de lignes ajoutées/supprimées par Jour, Semaine et Mois et par Auteur :
```
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerDays"
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerWeeks"
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerMonths"

./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerDays"
./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerWeeks"
./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerMonths"
```

11. Nombre de Commits par Jour, Semaine et Mois et par Auteur :
```
TODO : À compléter dès que le plugin est fait
```

## FICHIERS ET EXTENSIONS :

12. Pourcentage de chaque extensions (API) (X):
```
./gradlew run --args="--addPlugin=getExtensions --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

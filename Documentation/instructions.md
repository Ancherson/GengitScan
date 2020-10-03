# Présentation du projet

(à détailler + tard)

## Liste des membres (8)

- Ismail Badaoui
- Jules Cherion
- Alexandre Leymarie
- Etienne Nedjal
- Yoan Rougeolle
- Victor Spehar
- Elody Tang
- Xuewei Zhang

------
------
------


# Instructions

## Les codes de Git

Résumé de tous les codes de GIT pouvant nous aidant à la réalisation de ce projet :

### Installation de Git

1. Mettre à jour sa machine.
```
sudo apt update & sudo apt upgrade
```

2. Ouvrir un terminal et écrire :

```
sudo apt-get install git-all
```

### Paramètres de Git

1. Écrire dans un terminal :
```
$ git config --global user.name "*name*"
$ git config --global user.email *email*

```

### Première utilisation

1. Vérifier l'état des fichiers :
```
$ git status
```

2. Placer de nouveaux fichiers sous suivi de version :
```
$ git add *nomDeFichier*
```

3. Visualiser ce qui a été modifié mais pas encore indexé :
```
$ git diff
```

4. Valider les modifications :
```
$ git commit -m _"message"_
$ git commit --amend -m _"newmessage"_
```

5. Déplacer des fichiers :
```
$ git mv *nom_origine* *nom_cible*
```

6. Créer une nouvelle branche :
```
$ git branch testing
```

7. Fusionner la branche d'une issue spéciale :
```
$ git merge iss_nombreIssue_
```

8. Récupérer la dernière version avant de push :
```
$ git pull
```

9. Pousser une branche dans un serveur :
```
$ git push origin (branche)
```

10. Changer de branche depuis le dépot local
```
$ git checkout _nomdelabranche_
```

## La présentations d'un fichier .MD

### La syntaxe

![alt text](https://zupimages.net/up/20/39/j97g.png)

## Cloner et compiler le projet

⚠️ Attention, cloner un projet est différent de télécharger un projet !!! ⚠️

### Commandes principales

1. Installer Java 10, 11, 12 ou 13.
Les anciennes versions de Java ne sont pas supportées.


2. Faire les dernières mises à jour de votre appareil.
Écrire sur un terminal :
```
sudo apt update & sudo apt upgrade.
```

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
- Se diriger vers le fichier 'home' avec la commande :
```
cd visulog
```

5. Toujours sur le terminal, écrire :
```
./gradlew build
```

Hourra ! La compression est terminée.

### Si des problèmes persistent...

Voici une liste des problèmes qui a pu se passer durant votre compression :

- Plus de réseaux internet : Vous devez disposer obligatoirement d'une connexion internet, car des fichiers vont être téléchargés.

- Vous avez supprimé l'un des fichiers par erreur : Vous devez supprimer les fichiers qu'il vous reste. Puis recommencer les démarches ci-dessus en veillant à ce qu'aucun fichier ne soit déplacé ou supprimé.

- Assurez-vous que la version de Java dont vous disposez est compatible avec la commande 'built'. Tapez 'java' sur votre terminal et regardez ce qui s'affiche, si "aucune commande" apparaît, il se peut que Java n'est pas été proprement installé, ou que la version est incompatible.
    - Rappel : Java 10, 11, 12 et 13 sont les Java compatibles.


## Fork le projet

Un fork est une copie d'un dépôt original qu'on place dans un autre espace pour pouvoir y attribuer des modifications sans affecter le projet original. 

### Commandes principales

1. Sur la page du projet principal, à droite des boutons, cliquer le bouton "Fork".

![alt text](https://docs.gitlab.com/ee/user/project/repository/img/forking_workflow_fork_button.png)

2. Choisir un nom de projet.

Le fork a été crée.

⚠️ Depuis GitLab 12.6, lorsque les propriétaires de projets réduisent la visibilité d'un projet, cela supprime les relations entre un projet et ses copies. ⚠️


## Exécuter le projet

(à détailler + tard)







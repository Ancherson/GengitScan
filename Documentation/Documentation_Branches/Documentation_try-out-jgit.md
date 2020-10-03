#Documentation Branche 10-try-out-jgit

##BUT:  

Tout d'abord cette branche a ete cree pour l'issue 10 du projet de base qui consistait a teste jgit.  
  
jgit est bibliothèque d'interface de java permettant de faciliter l'analyse des outputs de git. Cette bibliothèque peut nous tres utile mais d'apres un des commentaire de l'issue, cette bibliotheque peut etre complique a utilise puisqu'il necessite une comprehension de quelques concepts d'implementation ( "Only con is that it is somewhat low level and requires understanding few implementation concepts.").

L'issue est dans la colonne _done_ dans l'ancien projet.

##CHANGEMENT PAR RAPPORT A LA BRANCHE DEVELOP

La fonction _compare_ de gitlab est tres etrange puisqu'elle indique des changements par rapport à la branche develop qui ne sont pas, ou alors je ne sais l'utiliser).

Les principaux changements se trouve dans la partie gitrawdata du projet:

Il n'y a plus que la classe Commit.java (Plus de CommitBuilder) et Commit.java est totalement différent de celui qui est dans la branche _develop_.

###Description de la classe Commit:

- Champs :

   - les mêmes que dans ceux de develop mais le champs mergedFrom n'y est plus.

- Constructeur :

   - Le même que celui de develop sauf que mergedFrom n'y est pas.
   
- Methodes :

   - String toString()  
     Même que dans develop sauf que plus de mergedFrom.
     
   - static Commit commitOfRevCommit (AnyObjectId id, RevCommit rCommit)  
     Un RevCommit est un objet Commit de la bibliotheque **jgit**.  
     Un AnyObjectId represente l'id du commit.  
     Permet de convertir un RevCommit de la bibliothèque **jgit** en notre propre Objet de type Commit.
     
   - static String stringOfTime(long time, TimeZone tz)  
     Permet de renvoyer un String a partir d'un temps et d'une Zone temps
     
   - public static Commit parse (Repository repo, AnyObjectId id)  
     Cette methode cree d'abord un RevCommit a partir de repo et id, puis utilise la methode _commitOfRevCommit()_ pour revoyer un Commit.
     
##Conclusion

Cette bibliotheque peut nous être tres utile puisque elle est, je trouve, assez simple d'utilisation, assez claire. Par contre si on veut l'utiliser il faudra completer la classe Commit, notamment une fonction qui peut generer une liste de Commit.



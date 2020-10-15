#  Documentation des fichiers build

Quand on build le projet avec la commande "./gradlew build", un dossier nommé "build" est généré dans chaque dossier contenant un fichier "build.gradle.kts".

Ces dossiers contiennent notamment les fichiers class, et les résultats des tests sous format HTML.

Les résultats des tests se trouvent dans certains dossiers build à l'adresse build/report/tests/test.

Les tests sont effectués grâce au framework JUnit et se trouvent dans les dossiers src/test.
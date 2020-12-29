# Visulog

*Tool for analysis and visualization of git logs*

## Presentation

Visulog a tool for analyzing contributions from the members of a team working a a same given project hosted on a git repository. Its goal is to assist teachers for individual grading of students working as a team.

This tool can:

- compute a couple of relevant indicators such as:
  - number of lines or characters added/deleted/changed
  - number of commits
  - number of merge commits
- analyze the variations of these indicators in time: for instance sum then in a week, compute a daily average or an average in a sliding window, ...
- visualize the indicators as charts (histograms, pie charts, etc.) embedded in a generated web page.

## Already existing similar tools

- [gitstats](https://pypi.org/project/gitstats/) 

## Technical means

- The charts are generated by a third party library (maybe a Java library generating pictures, or a javascript library which dynamically interprets the data).
- The data to analyze can be obtained using calls to the git CLI. For instance "git log", "git diff --numstat", and so on.

## Architecture

Visulog contains the following modules:

- data types for storing raw data directly extracted from git history, with relevant parsers
- a generator of numerical series (for the indicators mentioned above)
- a generator of web pages
- a command line program that calls the other modules using the provided command line parameters
- a shared module for configuration object definitions

## Usage

### Building the project

1. clone the repository
    ```
    git clone git@gaufre.informatique.univ-paris-diderot.fr:adegorre/visulog.git
    ```
   or
    ```
    git clone https://gaufre.informatique.univ-paris-diderot.fr/adegorre/visulog.git
    ```
2. Enter the project folder
    ```
    cd visulog
    ```
3. Only if you are on a SCRIPT computer (in one of the TP rooms):
    ```
    source SCRIPT/envsetup
    ```
    This will setup the GRADLE_OPTS environment variable so that gradle uses the SCRIPT proxy for downloading its dependencies. It will also use a custom trust store (the one installed in the system is apparently broken... ).
4. run gradle wrapper (it will download all dependencies, including gradle itself)
    ```
    ./gradlew build
    ```
    
### Running the software

Currently, it can be run through gradle too. In order to pass program arguments, you need to pass them behind `--args`:
```
./gradlew run --args="[path to your git project] --addPlugin=Plugin1 --addPlugin=Plugin2"
```
If there is no path, it runs in this project's directory.
If it's run without arguments, it will launch the GUI which displays all possible arguments.

Some commands need to work with an API and special commands. The latter will be specified with an icon like this [(API)](README.md#privatetoken-and-project-id).
Some commands can be used on the current branch but also on all branches. Just add "ForAllBranches" to the commands. Those that cannot be used on all branches will be marked with an icon (X).
Before testing the plugins, you need to do the following commands:

- Linux :
```
sudo apt install curl
```

- MacOS
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" < /dev/null 2> /dev/null
brew install curl
```
You can now compile and execute the following commands.


### Manual of the software

The following command displays the manual of the software :
```
./gradlew run --args="--help"
```

#### GENERAL STATISTICS

1. List of members [(API)](README.md#privatetoken-and-project-id) (X) :

```
./gradlew run --args="--addPlugin=getMembers --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

#### ACTIVITIES PER DATE :

2. Number of commits per Days, Weeks and Months :

```
./gradlew run --args="--addPlugin=countCommitsPerDays"
./gradlew run --args="--addPlugin=countCommitsPerWeeks"
./gradlew run --args="--addPlugin=countCommitsPerMonths"
```

3. Number of lines added/deleted per Days, Weeks and Months :

```
./gradlew run --args="--addPlugin=countLinesAddedPerDays"
./gradlew run --args="--addPlugin=countLinesAddedPerWeeks"
./gradlew run --args="--addPlugin=countLinesAddedPerMonths"

./gradlew run --args="--addPlugin=countLinesDeletedPerDays"
./gradlew run --args="--addPlugin=countLinesDeletedPerWeeks"
./gradlew run --args="--addPlugin=countLinesDeletedPerMonths"
```

### ACTIVITIES BY AUTHOR :

4. Number of commits :

```
./gradlew run --args="--addPlugin=countCommits"
```

5. Number of merge commits (X) :

```
./gradlew run --args="--addPlugin=countMergeCommits"
```

6. Number of lines added/deleted :

```
./gradlew run --args="--addPlugin=countLinesAdded"
./gradlew run --args="--addPlugin=countLinesDeleted"
```

7. Contribution percentages (X) :

The contribution is calculated in comparison to the number of lines of an author. The line belongs to the last author that modified it.

```
./gradlew run --args="--addPlugin=countContribution"
```

8. Number of comments [(API)](README.md#privatetoken-and-project-id) (X):

```
./gradlew run --args="--addPlugin=countComments --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

9. Number of assigned issues [(API)](README.md#privatetoken-and-project-id) (X) :

```
./gradlew run --args="--addPlugin=countIssues --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

### ACTIVITIES PER DATE AND BY AUTHOR :

10. Number of lines added/deleted per Days, Weeks, Months and by Author :

```
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerDays"
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerWeeks"
./gradlew run --args="--addPlugin=countLinesAddedPerAuthorPerMonths"

./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerDays"
./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerWeeks"
./gradlew run --args="--addPlugin=countLinesDeletedPerAuthorPerMonths"
```

11. Number of commits per Days, Weeks, Months and by Author :

```
./gradlew run --args="--addPlugin=countCommitsPerAuthorPerDays"
./gradlew run --args="--addPlugin=countCommitsPerAuthorPerWeeks"
./gradlew run --args="--addPlugin=countCommitsPerAuthorPerMonths"
```

### FILES AND EXTENSIONS :

12. Percentage of each extensions [(API)](README.md#privatetoken-and-project-id) (X):

```
./gradlew run --args="--addPlugin=getExtensions --privateToken=1m1pdKszBNnTtCHS9KtS --projectId=1618"
```

### LOAD AND SAVE COMMANDS :

13. --load= (Loads the selected commands (test in the example) into a file ../config.txt)
(Format command: --load=name of the config)
Example : 
```
./gradlew run --args='--load=test'
```

14. --save= (Save the commande "--addPlugin=(name of command)" in ../config.txt)
(Format command: --save=name of the config)
Example : 
```
./gradlew run --args='--addPlugin=countCommits --save=test'
```

-----------------------------------------------------------------------------------------------------------------------------
#  PrivateToken and Project ID

1. **How to get the project ID ?**

![Project ID](https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog/raw/a8a33523c3a68afcf405a4fb400127e06a6b54bf/images/ID.png)

2. **How to generate the private Token ?**

![Private Token 1](https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog/raw/a8a33523c3a68afcf405a4fb400127e06a6b54bf/images/PrivateToken1.png)

![Private Token 2](https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog/raw/a8a33523c3a68afcf405a4fb400127e06a6b54bf/images/PrivateToken2.png)

![Private Token 3](https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog/raw/a8a33523c3a68afcf405a4fb400127e06a6b54bf/images/PrivateToken3.png)

![Private Token 4](https://gaufre.informatique.univ-paris-diderot.fr/badaoui/visulog/raw/a8a33523c3a68afcf405a4fb400127e06a6b54bf/images/PrivateToken4.png)


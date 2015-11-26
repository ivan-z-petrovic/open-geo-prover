# Local Project Development and Maintenance #

# Prerequisites #
## Java SDK ##
In order to develop OpenGeoProver project there must be installed at least Java 1.5 environment on local machine.

Following Java environment variables must be set to point to proper installed Java SDK:

  * JAVA\_HOME
  * CLASSPATH

For example:
  * JAVA\_HOME = C:\Program Files (x86)\Java\jdk1.5.0\_22
  * CLASSPATH = C:\Program Files (x86)\Java\jdk1.5.0\_22\jre\bin

(_In Windows OS environment variables are found under Control Panel > System > Advanced System Settings_)

#### Java SDK to use ####
The plan is to use one of recent Java versions, but in fact it depends on branch of this project. E.g. branch for GeoGebra will be developed with Java 1.5.0\_22. It will be clearly stated in corresponding Wiki page for branches which version of Java to use for developing of certain branch.

_Note_: Java releases can be found on official [Java site](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

## Eclipse IDE ##
It is up to a developer which IDE to use for developing this project. We suggest [Eclipse IDE](http://www.eclipse.org/downloads/). There are various plug-ins for Eclipse that could make development process comfortable. One of them is [metrics plug-in](http://metrics.sourceforge.net/) that provides project statistics (number of lines of code, methods, classes, interfaces etc.).

## Subversion ##
As a Version Control System (VCS) for this project on Google Code repository we use Subversion (SVN). There are various SVN toolkits to install in local machine in order to use SVN to access this project on Google Code repository.

One of them is [Subversive - SVN Team Provider](http://www.eclipse.org/subversive/), an Eclipse plug-in which pretty looks like Eclipse's CVS (Concurrent Versions System). A brief installation instructions for Subversive SVN can be found on [following location](http://www.eclipse.org/subversive/documentation/gettingStarted/aboutSubversive/install.php).

# Checkout of project in local working environment #
This is description for Eclipse IDE with Subversive SVN plug-in installed.

  1. Create new SVN repository:
    * For example use https://open-geo-prover.googlecode.com/svn/trunk for URL of trunk of main development baseline.
    * Use your own Google Code credentials for the Authentication
    * Note: According to [Google Code FAQ](http://code.google.com/p/support/wiki/FAQ#Accounts) user must have Google account in order to make code changes to project on this repository.
  1. Checkout OpenGeoProver project from Google Code repository into new Eclipse Java project:
    * Go to SVN repository and from right click menu select "Find/Checkout As..." item. From "Check Out As" pop-up screen select "Check out as a project configured using the New Project Wizard" and from that wizard select new Java project.
    * Configure Java Build path in Eclipse for new Java project:
      * In Source tab select source directories of the project (it is **src** sub-directory in project's baseline directory).
      * From Source tab browse for "Default output folder" (build folder) - select/create bin under OpenGeoProver directory.
      * Go to Libraries tab and select jre1.5.0\_22 as a system JRE for eclipse project (select it from installed JREs on local machine).
      * Under Libraries tab create new User library and add:
        * all project's 3rd party libs in it,
        * ant.jar from Apache Ant lib from eclipse plug-ins, and
        * two plugin.jar files from installed Java environment: one from **jre1.5.0\_22\lib** and another from **jdk1.5.0\_22\jre\lib**.
    * After configuration of Build path check whether configuration of Java Compiler matches required Java SDK and change default settings if necessary.
    * Configure Ant for the new Java project:
      * from Eclipse go to Run > External Tools > External Tools Configuration, on the left side select build.xml file for OpenGeoProver project and in JRE tab from the right side select "Run in the same JRE as the workspace".
  1. Also checkout wiki project as well to follow changes in wiki pages and add resources (like images) to wiki project on Google Code repository.

# Committing new source code changes to Google Code repository #
To be able to make changes on repository a developer has to:
  1. Create Google account to be able to participate in code changes on Google Code repository.
  1. Create an issue on OpenGeoProver project on Google Code to describe the bug that has to be fixed or improvement/enhancement that has to be implemented, with all necessary details like branch where the change has to be implemented. Notify the project owners when issue is created and discuss about solution that is planed to be applied.
  1. Use SVN to commit code changes from local machine to the repository, that correspond to created issue document. Select proper branch and add corresponding comment about the change, and put issue number and its very short description as first lines of comment. Before commit, synchronize local project with the repository to avoid conflicts and breaking previous code changes from repository.
  1. After commit, update the issue on project from Google Code, change its status to Fixed and notify project owners and the creator of the issue.
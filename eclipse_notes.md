# Notes for work within Eclipse IDE #

Here are listed some important development notes that developers less experienced in [Eclipse IDE](http://www.eclipse.org/) could not know but which are sometimes very useful or necessary to know.

## Starting Java to use more of heap memory ##
Sometimes Java application could have great demands for fresh memory to allocate new objects. If this happens in very short time frame, Java run time exception "Out of memory" could come up.

To increase the size of Java heap available for certain Java application it is necessary to add following arguments to JVM (java interpreter) when running the application:

_-Xms64m -Xmx512m_

where the first parameter specifies initial and the second specifies maximal Java heap size in megabytes (and these numbers can be changed appropriately).

In application that runs from Eclipse, these arguments are added in "Run configuration" window in Arguments tab (section for "VM arguments").

Also, in Eclipse this could be initialized for each application started from Eclipse by setting these arguments in **eclipse.ini** file (in directory for Eclipse program) below "-vmargs" line. Better do not change this file if not sure or if you don't want same consumption of heap for all applications.

For more information about changing size of Java heap read on-line resource [on following external link](http://baskarfelix.wordpress.com/2008/08/06/out-of-memory-errorjava-heap-space/).

## Remote debugging in Eclipse ##
In order to debug Java application (built with debug notes) remotely from Eclipse, it is necessary to add following parameters of JVM (parameters of java interpreter) in command line for starting that application:

_-Xdebug -Xrunjdwp:transport=dt\_socket,server=y,address="8095"_

(Here **8095** is port number on which debugger connects to running application and can be any appropriate number.)

On the other side, in Eclipse, there must be created a debugging session on localhost that connects to the application on the port stated in debugging JVM parameters (8095 in the above example).

More details can be found [here](http://www.ibm.com/developerworks/opensource/library/os-eclipse-javadebug/index.html).

## Enabling debugger for external project added to existing Eclipse workspace ##
To do this it is necessary to open "Debug configuration" dialog, select the desired application and in "Classpath" tab select the "User Entries" and open "Advanced..." sub-dialog, then select "Add Folders" and browse for directory with classes. Also in "Main" tab search for main class.

## Setting working directory in Eclipse ##
If java application uses working directory as a starting reference point for work with files (i.e. if works with relative paths), it is important to set the correct working directory for that application. If application is executed out of Eclipse it is easily achieved by calling **java** command from the desired directory. In Eclipse, the working directory can be set in "Run configuration" dialog, in "Arguments" tab, for "Working directory" option "Other" is chosen and then directory is selected from "File System".

## Eclipse Subversive topics ##

### Changing directory structure ###
Before making and committing any change in directory structure (like deleting file or directory, adding new file/directory or renaming) it is necessary to update the project to the latest revision in order for SVN commit to succeed. Also it is good practice to maintain the project up to the latest revision.

### Ignoring some resources in synchronization ###
To reduce the noise when performing project's synchronization with the repository, it is good to add some resources to SVN ignore-list.

To do this go to Java Code perspective in Eclipse, right click on desired resource, "Team > Add to svn:ignore" and add that resource by its name. It can be done for many resources like **build**, **bin**, **input**, **output**, **log**, **lib**, **documentation/javadoc** folders etc.

If the ignored resource exists in repository, when performing "Team synchronize" action, it will be shown when there are changes of ignored resource, but the list of changes will be empty (since it is ignored). If the ignored resource is not present on repository location, it won't be shown when doing project synchronization.

### Making new branch ###
Before making new branch first of all commit all changes and clean and remove all unnecessary resources (log, lib, build, output, ...). Then update the entire project to the latest revision. Then go to "Team > Branch...", set the name of new branch and appropriate comment/description and hit OK button.

After the branch has been created, check it out to new local directory in new Java project as explained in [Local development Wiki page](local_project_development#Checkout_of_project_in_local_working_environment.md), just choose the trunk of the new branch for the path of project's repository. When configuring Ant for new branch, it is not necessary to create configuration - it will be automatically created when build is run first time; just update the JRE tab as explained in local development wiki page. In build.xml file change the name of project to correspond to new branch in order to easily distinguish two Ant build files from different trunks/branches.

### Merging ###
There is a brief description on [Eclipse Subversive documentation](http://www.eclipse.org/subversive/documentation/teamSupport/merge_dialog.php) about merging, but one important note for merge operation is that the resource which is merged has to match the same that resource from the repository location. For example, if you right click on **src** directory on local workspace, then in Merge dialog in URL edit filed you must choose same that directory from the repository location (e.g. `https://open-geo-prover.googlecode.com/svn/branches/geogebra_ogp/OpenGeoProver/src`).
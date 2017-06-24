# Swallow
Efficient flow scheduling system in data-intensive clusters

# Download Project
1. Download from [https://github.com/kimihe/Swallow](https://github.com/kimihe/Swallow) or use `git clone git@github.com:kimihe/Swallow.git`.

2. Extract the zip file to a convenient location:

* On **Linux** and **OSX** systems, open a terminal and use the command unzip `Swallo-master.zip`. Note: On **OSX**, if you unzip using Archiver, you also have to make the sbt files executable:

```
 $ chmod u+x ./sbt
 $ chmod u+x ./sbt-dist/bin/sbt
```

* On **Windows**, use a tool such as File Explorer to extract the project.

# Running the example
* In a console, change directories to the top level of the unzipped project.

For example, if you used the default project name, `Swallo-master`, and extracted the project to your root directory, from the root directory, enter: `cd Swallo-master`

* Enter `./sbt` on **OSX/Linux** or sbt.bat on **Windows** to start `sbt.bat`.

sbt downloads project dependencies. The `>` prompt indicates sbt has started in interactive mode.

* At the sbt prompt, enter `run`.

sbt builds the project and compiles archived package, something like this: 

```
Multiple main classes detected, select one to run:

 [1] com.lightbend.akka.sample.AkkaQuickstart
 [2] com.lightbend.akka.sample.MyLocalActor
 [3] com.lightbend.akka.sample.MyMasterActor
 [4] com.lightbend.akka.sample.MyRemoteActor
 [5] com.lightbend.akka.sample.MyTestActor2

Enter number: 3
```
Enter `3` and select one and `MyMasterActor` will run.

# Intellij IDEA
You can also run this project in **Intellij IDEA**, just open from the root directory.

# More
This project is still in development, welcome any contributions to help to make it better.  :)

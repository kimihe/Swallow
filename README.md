# Swallow Guide
Efficient flow scheduling system in data-intensive clusters, based on [Akka](http://akka.io/).  
**Development Language:** `Scala`.  

# Prerequisites
* JDK 8.
* sbt 0.13.13 or higher ([download here](http://www.scala-sbt.org/download.html)).

# How To Download
1. Download from [https://github.com/kimihe/Swallow](https://github.com/kimihe/Swallow) or use `git clone git@github.com:kimihe/Swallow.git`.

2. Extract the zip file to a convenient location:

* On **Linux** and **OSX** systems, open a terminal and use the command unzip `Swallo-master.zip`. 
* On **Windows**, use a tool such as File Explorer to extract the project.

# Running The Project
* In a console, change directories to the top level of the unzipped project. For example, if you used the default project name, `Swallo-master`, and extracted the project to your root directory, from the root directory, enter: `cd Swallo-master/swallow`.

* In the above directory, enter `sbt compile` to compile the source codes and enter `sbt run` to execute the program. sbt will download project dependencies, build the project and compile the archived package. Output looks like this: 

```
Multiple main classes detected, select one to run:

 [1] examples.ExampleClusterApp
 [2] examples.ExampleMaster
 [3] examples.ExampleReceiver
 [4] examples.ExampleSender
 [5] examples.KMActorAggregation
 [6] examples.KMClusterApp
 [7] examples.KMLocalActor
 [8] examples.KMMasterActor
 [9] examples.KMRemoteActor

Enter number: 
```
* For example: enter `1` and select to run `[1] examples.ExampleClusterApp `. Then, you can start three new terminals to run `[2] examples.ExampleMaster`, `[4] examples.ExampleSender` and `[3] examples.ExampleReceiver`. 

* These four processes simulate a simplest distributed scheduling system. In this example, all of them are run on the same machine, you can modify the `application.conf` in directory `resources` to respectively configure their IP address and communication port. Run them on different machines and the same behaviour will be shown.

# Intellij IDEA
You can also organize this project with **Intellij IDEA**, just open from the root directory `Swallo-master/swallow`.

# More
This project is still in development, welcome any contributions to help make it better.  **:)**

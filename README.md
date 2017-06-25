# Swallow
Efficient flow scheduling system in data-intensive clusters.  
**Development Language:** `Scala`.

# Download Project
1. Download from [https://github.com/kimihe/Swallow](https://github.com/kimihe/Swallow) or use `git clone git@github.com:kimihe/Swallow.git`.

2. Extract the zip file to a convenient location:

* On **Linux** and **OSX** systems, open a terminal and use the command unzip `Swallo-master.zip`. 
* On **Windows**, use a tool such as File Explorer to extract the project.

# Running the example
* In a console, change directories to the top level of the unzipped project. For example, if you used the default project name, `Swallo-master`, and extracted the project to your root directory, from the root directory, enter: `cd Swallo-master`

* Enter `sbt compile` to compile the source codes and enter `sbt run` to execute the program. sbt downloads project dependencies,  building the project and compiling archived package, output like this: 

```
Multiple main classes detected, select one to run:

 [1] KMActorAggregation
 [2] KMLocalActor
 [3] KMMasterActor
 [4] KMRemoteActor

Enter number: 3
```
Enter `3` and select `KMMasterActor ` to run.

# Intellij IDEA
You can also run this project in **Intellij IDEA**, just open from the root directory.

# More
This project is still in development, welcome any contributions to help make it better.  :)

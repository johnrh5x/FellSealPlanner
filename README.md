# FellSealPlanner

FellSealPlanner is a small Java application for, as the name suggests, planning character builds in Fell Seal: Arbiter's Mark.  It allows the user to specify one or more of the following, a primary job, a secondary job, two passive abilities, and a counter ability, and returns (i) the prerequisites needed in order to unlock each job, (ii) the prerequisites needed to unlock the job associated with any specified abilities, and (iii) the combined set of prerequisites for all selected and implied jobs.

This repository contains source files, data files, and an executable jar file.  Files are provided as-is.  FellSealGUI contains the main method and the GUI for the application.  FellSeal handles the data.  The application expects the data files to be in the same directory as the class files and will terminate itself if they aren't.

To run the application without compiling it yourself: (i) install Java on your machine, (ii) download the jar file, (iii) open a terminal window, (iv) navigate to the jar file's location, and (v) type "java -jar FellSealPlanner.jar".  I created the class files with OpenJDK 8, and so they should be compatible with any easily-obtainable version of the Java runtime environment.

John Harris
February 3, 2023

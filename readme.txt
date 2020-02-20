Name: Navya Rajashekar Sogi
UTA ID: 1001753085
Programming Language Used: Java(Version "13.0.2")

Prerequisites: If you are having trouble running the above command, you should either need to set JAVA_HOME from the terminal or use the java from your JDK installation folder(Ex: /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin).
City names are case-sensitive. Ex: ‘Bremen’ and ‘bremen’ would not be the same. New York would be New_York. You need to choose as is from the input text file.

Running Instructions:
1) Unzip the folder(assignment1_nxs3085.zip) and place it in your local computer. 
2) In the extracted folder(assignment1_nxs3085), find folder Task1. Use cd command to set your directory to the extracted folder(assignment1_nxs3085/Task1).
3) Inside Task1, you should see the file.
         find_route.java
4) Compile the java program using the following command line:
         javac find_route.java
5) Run the java program using the following command line:
         java find_route input1.txt <Source> <Destination> h_kassel.txt
         Example: java find_route input1.txt Bremen Kassel (Uninformed search)
             java find_route input1.txt Bremen Kassel h_kassel.txt (Informed Search)
6) If you pass the correct number of arguments, you should see the expected output in the console. Otherwise, the program would instruct you to pass in the correct arguments to run.

Alternate way to run the program:(Through Eclipse)
1) Unzip the folder(assignment1_nxs3085.zip) and place it in your local computer. 
2) In the extracted folder(assignment1_nxs3085), find folder Task1. Use command line to set your directory to the extracted
folder(assignment1_mxs3085/Task1).
3) Inside Task1, you should see the below file.
         find_route.java
4) Open Eclipse IDE. Create a new project say assignment1. Right click on the project name and create a new package say route. Copy find_route.java from Task1 to the newly created package - route(Copy Paste). Include the package name if required in the program. Save the program.
5) Go to Run Configurations by right clicking on the java file - find_route.java. A dialog box opens. Go to Java Application, give a new name to the configuration say findRoute, browse the find_route.class for the Main Class(route.find_route should show up).
6) Go to Arguments in the same dialog box and under Program Arguments, pass the following arguments. 
    input1.txt <Source> <Destination> h_kassel.txt
    Example: input1.txt Bremen Kassel
             input1.txt London Kassel h_kassel.txt


Code Structure:
1) This program will need the below arguments to run:
     A) input file path
     B) Source City
     C) Destination City
     D) Heuristic file(optional)
2) Store the data read from input file and heuristic file into an ArrayList PlaceDetails.
3) Initialize cityInfoFringe of origin city to the fringe with parent as null.
4) findAdjacentNodes() method is invoked and the value in the 0th position(say x) in the fringe is popped.
5)findAdjacentNodes() method finds the children of x and adds to the fringe. Now x will be removed from the fringe and added to visitedCityData.
6) The fringe will be sorted 
    If its an uninformed search, the fringe will be sorted with the cumulative cost and 
    If it is informed search then the fringe will be sorted by the cumulative cost + heuristic value
7) Again the least element is passed to the function to find its adjacent cities.
8) If the fringe is empty there is no path from origin city to destination city and the returned value is infinite.
9) If the current popped value from the fringe is the destination city, then the function would exit and computeFinalPath() method is invoked.
10) computeFinalPath() method is used to find optimal path by backtracking the visitedCityData.
11)generatedNodes gives the number of nodes generated (nodes added to the fringe).
12)expandedNodes gives the number of nodes expanded (length of visited nodes).
13)memoryNodes gives the max number of nodes stored in memory.


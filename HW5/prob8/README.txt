1. Both [.Java] and [.jar files with .class] are included

2. If you want to compile the .Java file to .jar by yourself, 
upload the .java file to your local hdfs. Otherwise, don't upload the .java
and upload .jar files with .class and skip to step 4.

3. Compile the program and make a jar file out of it with the following commands: 
hadoop com.sun.tools.javac.Main filename.java 
jar cf anyname.jar filename*.class 

4. Upload the web-Google.txt to your hdfs

5. Run the program with web-Google.txt as the input file
hadoop jar filename.jar filename directorytoweb-Google.txt directoryofoutput

Note: anyname means you can put any name for it, it doesnt matter.
filename means you have to have the same name as whatever name you specified. 
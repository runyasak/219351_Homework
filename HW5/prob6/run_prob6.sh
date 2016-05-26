export HADOOP_CLASSPATH=/usr/lib/jvm/java-1.8.0-openjdk-amd64/lib/tools.jar
hadoop com.sun.tools.javac.Main WordCount_6.java
jar cf wc_6.jar WordCount_6*.class
hadoop jar wc_6.jar WordCount_6 /user/input2/ jloutput3

export HADOOP_CLASSPATH=/usr/lib/jvm/java-1.8.0-openjdk-amd64/lib/tools.jar
hadoop com.sun.tools.javac.Main TwoLinksNodeCount.java
jar cf wc.jar TwoLinksNodeCount*.class
hadoop jar wc.jar TwoLinksNodeCount /user/input/ jloutput3

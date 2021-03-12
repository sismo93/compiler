all:
	javac -cp src/ src/Main.java
	jar cfe dist/part3.jar Main -C src .
	javadoc -private -html5 src/*.java -d doc/javadoc

clean:
	rm src/*.class


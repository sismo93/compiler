all:
	javac -cp src/ src/Main.java
	jar cfe dist/Part1.jar Main -C src .
	javadoc src/Main.java -d doc/javadoc
	for testFile in test/*.alg ; do \
		echo "\nTest file:" $$testFile ; \
		java -jar dist/Part1.jar $$testFile ; \
		 \
	done

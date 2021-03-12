all:
	javac -cp src/ src/Main.java
	jar cfe dist/part2.jar Main -C src .
	javadoc src/*.java -d doc/javadoc
testing:
	for testFile in test/*.alg ; do \
		echo "\n Test File :" $$testFile ; \
		java -jar dist/part2.jar $$testFile; \
		echo "" ; \
	done
pdf:
	for testF in more/*.tex ; do \
		lualatex  -output-directory="more" $$testF ; \
	done

clean:
	rm src/*.class



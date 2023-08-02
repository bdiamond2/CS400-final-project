run: compile
	@java QSAGameDriver
compile:
	@javac QSAGameDriver.java
test:
	@javac -cp .:junit5.jar QSAGameTest.java -Xlint
	@java -jar junit5.jar --class-path . --scan-classpath
clean:
	@rm *.class

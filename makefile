JFLAGS = -g
DEPENDENCIES = -cp ".:./lib/json-simple-1.1.1.jar"
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) ${DEPENDENCIES} $*.java

CLASSES = \
	stream/ClientThread.java \
    stream/EchoClient.java \
	stream/EchoServerMultiThreaded.java \
	stream/GUI.java \
	stream/Logger.java \
	stream/MulticastThread.java \
	stream/Room.java \
	stream/SenderServer.java


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) stream/*.class

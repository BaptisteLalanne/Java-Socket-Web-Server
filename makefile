JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

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

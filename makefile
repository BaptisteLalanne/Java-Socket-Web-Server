JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	stream/ClientThread.java \
    stream/EchoClient.java \
    stream/EchoServer.java \
	stream/EchoServerMultiThreaded.java \
	stream/GUI.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) stream/*.class

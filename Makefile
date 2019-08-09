# Option de compilation
JFLAGS = -g
JVFLAGS = -cp
JC = javac
JV = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(DIR_SRC)*.java

# Dossier
PROGNAME = Bataille Navale
VERSION = 1.0
DIR_SRC = src/
DOXYFILE = documentation/Doxyfile

# Définition des classes
CLASSES = \
    src/Bateau.java \
	src/Acceuil.java \
	src/Case.java \
	src/Game.java \
	src/Grille.java \
	src/IA.java \
	src/Regles.java \
	src/Navale.java


classes: $(CLASSES:.java=.class)

clean:
	$(RM) src/*.class

doc: $(DOXYFILE)
	cd documentation && doxygen && cd ..

run:
	$(JV) $(JVFLAGS) src/ Navale

import javafx.application.Application;
import javafx.stage.Stage;

/*! \mainpage Bataille Navale - JAVAFX - Documentation
 *
 * \section intro_sec Introduction
 *
 * Bienvenue sur la documentation du programme Bataille Navale.
 *
 * Cette documentation a été réalisée dans le cadre du cours de réalisation de programme de Mme Balmas en deuxième année de licence Informatique à 
 * l'Université Paris 8.
 * Cette documentation recense l'ensemble des descriptifs des différentes classes et fonctions qui composent ce programme, ainsi qu'un descriptif
 * des attributs et variables qui les composent.
 *
 *
 * \section install_sec Installation
 *
 * \subsection tools_subsec Outils requis 
 * - Java Runtime Environment : http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
 *
 * \subsection running Executer le programme
 * Dans un terminal, accédez au répertoire du programme, et tappez les commandes suivantes :
 * @code
 * $ make
 * $ make run
 * @endcode
 *
 * \subsection cleaning Effacer les fichiers temporaires 
 * Pour effacez les fichier temporaires tapez :
 *
 * @code
 * $ make clean 
 * @endcode
 *
 * <BR><BR>
 */


/**Classe contenant le main et la fenetre principale du jeu*/
public class Navale extends Application {

    /**Fonction 'main' du programme*/
    public static void main(String[] args) {
        Application.launch(Navale.class, args);
    }

    @Override
    /**Fonction 'start' du programme, initialisant la fenetre principale*/
    public void start(Stage jeu) 
    {
        jeu.setTitle("Bataille Navale"); 
        Acceuil acceuil = new Acceuil(jeu);
     
        jeu.setScene(acceuil.Accueil);
        jeu.show();

    }
}


import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.*;

///Classe initialisant les règles du jeu
public class Regles extends Parent{
    
    public Regles(Scene view){
        
	Text t = new Text("\t\t\t\t\t\t\t\tLES REGLES\n\n\n"
			  +"Mode Solo : Le mode solo possède trois difficultée : Facile, Moyen, et Difficile.\n Le joueur doit détruire"
			  +"l'intégralitée des bateaux de la grille en un nombre de coup maximum\n défini par la difficultée choisie :\n"
			  +"- Facile = 80 coups.\n"
			  +"- Moyen = 50 coups.\n"
			  +"- Difficile = 30 coups.\n\n"
			  +"Mode Joueur contre Joueur : Ce mode oppose deux joueurs humain entre eux. Chacun son tour,\n"
			  +"chaque joueur joue et essaie de détruire l'intégralité des bateaux de l'adversaire avant que\n"
			  +"l'adversaire ne détruise les siens. Si un des joueurs découvre un bateau, il garde\n"
			  +"la main au prochain tour.\n\n"
			  +"Mode Joueur contre Android : Le mode joueur contre Android oppose le joueur a l'ordinateur.\n"
			  +"Le concept est le meme que pour le mode joueur contre joueur, cependant il sera beaucoup\n"
			  +"plus humiliant de perdre contre un ordinateur =) !!\n\n"
			  +"\t\t\t\t\t\t\t\t A vous de jouez !");
        t.setTranslateX(95);
        t.setTranslateY(80);
        t.setFont(Font.font("Brushed script", FontWeight.BOLD, 13));
        t.setFill(Color.BLACK);
        
     
        
        Group print = new Group();
        print.getChildren().add(t);
       
        view = new Scene(print, 900,460);
         this.getChildren().add(print);
             
    }
    
}

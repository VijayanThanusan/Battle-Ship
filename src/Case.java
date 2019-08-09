
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**Classe implémentant un objet case contenue dans une grille*/
public class Case extends Parent{

    /**Variable de type Rectangle, permettant de dessiner une case */
     public Rectangle forme;
    /**variable recevant 'TRUE' si la case fait partie d'un bateau, et 'FALSE' dans le cas contraire*/
     public boolean boat; 
    /**Variable permettant de savoir de savoir si la case en question a déja été jouée */
     public boolean visited; 
    /**Variable permettant de savoir si oui ou non l'on peut cliquer sur la case*/
     public boolean can_i_click; 
   
    /**Constructeur de la classe 'Case' : Ce constructeur créé un objet case en initialisant tous ses attributs.
     *l'argument 'click' passé en paramètre correspond à si oui ou non l'on peut cliquer sur la grille 
     *(exemple : 'click' pour la grille de l'IA sera initialisé à 'FALSE')
     */
    public Case(boolean click){
        this.boat = false;
        this.visited = false;
        this.can_i_click = click; 
        
        forme = new Rectangle(40,40, Color.WHITE);
        forme.setStroke(Color.BLACK);
        forme.setOpacity(0.7);
        this.getChildren().add(forme);//ajoute la forme a  l'objet case
        

        //this.setTranslateX(posX);//positionnement de la case sur le joueur
        //this.setTranslateY(posY);
        
        if(can_i_click == true)
        {
           
            this.setOnMouseClicked(new EventHandler<MouseEvent>(){// click sur case

                @Override
                public void handle(MouseEvent e)
                {
                    //si la case n'a pas déja été visité
                    if(visited == false)
                    {
                        //si pas de bato dans la case
                        if(boat == false)
                        {
                            forme.setFill(Color.CORNFLOWERBLUE);//si case vide 
                            visited = true; // on spécifie que la case a déja été visité

                            //timeline qui est utilisé lorsque l'on joue contre l'IA pour que l'IA puisse jouer lorsque l'on a joué
                            if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_IA)
                            {
                                Timeline pause = new Timeline(
                                new KeyFrame(
                                  Duration.millis(1000),
                                  actionEvent -> Game.ia.play()
                                    )
                                );
                                pause.play();
                            }
                        }
                        //si un bato dans la case
                        else 
                        {
                            forme.setFill(Color.RED);//si bateau    
                            visited = true; 
                            if(Game.mode_de_jeu == Game.Mode.SOLO)
                                     Game.joueur_solo.score();
                                
                            //ici si on clik on analyse quel mode de jeu est lancé et en fonction de celui-ci on affiche les scores des grilles correspondantes
                            if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_JOUEUR)
                            {
                                Game.joueur1.score();
                                Game.joueur2.score();
                            }

                            if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_IA)
                            {
                                Game.joueur_mode_IA.score(); // grille du joueur en mode Joueur contre IA
                            }
                        }
                        
                        //Si on est en mode solo alors on décrémente le nombre de coup restant, et on test si on a gagné ou perdu
                        if(Game.mode_de_jeu == Game.Mode.SOLO)
                        {
                            Game.coup_restant--;
                            Game.afficher_coup_restant();
                            Game.joueur_solo.test_win_or_loose();
                            
                        }
                        //si on joue contre un joueur, pareil on test si l'un des deux joueurs a gagné ou perdu
                        if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_JOUEUR)
                        {
                            Game.joueur1.test_win_or_loose();
                            Game.joueur2.test_win_or_loose();
                        }
                        //si on joue contre l'IA, on teste si on a gagné ou perdu
                        if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_IA)
                            Game.joueur_mode_IA.test_win_or_loose();
                    }
                }
            });        
        }
    }
    
    /**Fonction retournant 'TRUE' si il y a un bateau et 'FALSE' si il n'y en a pas*/
    public boolean getState(){
        return boat;
    }
    /**Fonction initialisant une case bateau à 'TRUE' ou à 'FALSE'*/
    public void setState(boolean z){
        this.boat=z;
    }
}



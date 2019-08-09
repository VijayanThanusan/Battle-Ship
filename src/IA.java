
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/*
  La classe IA permet a l'ordinateur de jouer : 
  Fonctionnement :
  
  Lorsque le joueur joue, l'IA a son tour et doit donc jouer.
  Premierement, elle commence par placer un coup aléatoire, afin de trouver une case rouge (bateau), tant qu'un bateau n'a pas été trouvé, 
  elle place des coups aléatoires.
  lorsqu'elle trouve une case rouge alors on enregistre la case trouvé dans le tablo "record_place_of_boat et on initialise la variable 
  finish_boat a true afin que l'IA sache qu'un bateau a été trouvé et qu'il faut faire une recherche minutieuse. Une fois ceci fait, 
  on appelle une autre fonction qui va passer a l'analyse des voisins. elle va donc repertorier la liste des voisins sur lesquelles on peut cliquer  ,et les tester tour apres tour.
  une fois qu'un voisin a été trouvé, on passe a une autre fonction, et on analyse en longueur ou en hauteur en fonction de la direction des cases
  trouvé, une fois que la direction dans la quelle on a continué tombe sur une case bleu donc ou il n'y a pas de bato, l'IA initialise la variable
  "sens_opposé" a true, afin que le placement des cases se face dans l'autre sens."
*/

/** Classe implémentant une intelligence artificielle, ainsi que sa façon de jouer*/
public class IA {
    
    /**Variable de type 'Grille' pour la grille de l'IA*/
    public Grille grille ;
    /**Variables acceuilant les coordonnées aléatoires en absicce de l'IA*/
    public int random_shootI;
    /**Variables acceuilant les coordonnées aléatoires en ordonnée de l'IA*/
    public int random_shootJ; // coup aléatoire sur J
    /**Variable servant à savoir si l'IA a ou non touché un bateau, ceci déterminera sa façon de jouer par la suite*/
    public boolean touched_boat;
    /**Tableau enrengistrant les coordonnées d'un bateau si il a été trouvé*/
    public int[][] record_place_of_boat;
    /**Variable permettant d'éfféctuer une analyse dans le sens de la lignée des bateaux trouvés si la valeur de celle-ci est à 1*/
    public boolean sens_opposé = false; 
    /**Variable permettant d'éfféctuer une analyse dans le sens opposé du sens de la lignée précédente*/
    public int other_sens = 0; 
    /**Variable permettant de savoir si oui ou non nous l'IA est dans une procédure de finition d'un bateau déja endomagé*/
    public boolean finish_the_boat = false; 
     
    /**Constructeur de la classe IA, qui initialise la grille de l'IA*/
    public IA() 
    {
        grille = new Grille(570,90, false);
        record_place_of_boat = new int[1][2];
    }
    
    /**Fonction qui affiche les bato adverse sur la grille de l'IA
     *Nos bateaux étant placé sur la grille de l'IA, cette fonction sert à les rendres visible.
     */
    public void print_adversary_boat()
    {
        for (int i = 0; i < this.grille.grilleH; i++) 
        {
            for (int j = 0; j < this.grille.grilleW; j++) 
            {
                if(this.grille.cases[i][j].boat == true)
                    this.grille.cases[i][j].forme.setFill(Color.GREY);
            }
        }
    }
    
    /**Fonction qui renvoie une valeur aléatoire comprise entre min inclu et max exclu.
     *Cette fonction sera utilisé dans le cadre du coup alétoire pour l'IA.
     */
    public int random(int min, int max)
    {
        return (int)(Math.random() * (max - min)) + min;
    }
    
    /**Fonction principale de la classe IA :
     *La fonction va en premier temp choisir une case aléatoire parmis toutes les cases de la grille disponible,
     *si elle tombe sur une case vide elle passe son tour, si elle tombe sur un bateau, alors elle enregistre sa position,
     *continue à jouer, et passe à l'éxamen des cases voisines afin de compléter le bateau en question.
    */
    public void play()
    {      
        //si coup précédent était un bato alors le prochain coup sera plus réfléchi
         if(this.touched_boat == true)
        {
            if(!continue_with_same_boat())
            {
                if(!shot_with_neighbor())
                    random_shot();
            }
        }
        //si au coup précédent pas de bato touché, alors l'IA tape un coup aléatoire
        else
         {
             System.out.println("RANDOM");
             random_shot();
         }
         
        //Analyse du coup joué 
        if(this.grille.cases[random_shootI][random_shootJ].boat == true)
        {
            this.grille.cases[random_shootI][random_shootJ].visited = true;
            this.grille.cases[random_shootI][random_shootJ].forme.setFill(Color.RED);
            this.touched_boat = true;
            
            this.grille.score();
            
            //si le coup tapé est un bato et que nous somme pas déja dans une procédure de finition de bato, alors on enregistre le bato en question
            if(finish_the_boat != true)
            {
                this.record_place_of_boat[0][0] = random_shootI;
                this.record_place_of_boat[0][1] = random_shootJ;
            }
            
            this.grille.test_win_or_loose();
            
            // Timeline pour retarder le coup de l'IA d'une seconde et rappeler la fonction play car un bato a été trouvé et l'IA garde la main au prochain tour
            Timeline pause = new Timeline(
                                    new KeyFrame(
                                      Duration.millis(1000),
                                      actionEvent -> play()  
                                    )
                                );
                                pause.play();
        }
        //si on tombe sur une case vide, alors on affiche une croix, et on modifie les valeurs des variables de la case
        else
        {
            if(finish_the_boat == true)
                sens_opposé = true;
   
            this.grille.cases[random_shootI][random_shootJ].visited = true;
            this.grille.cases[random_shootI][random_shootJ].forme.setFill(Color.CORNFLOWERBLUE);
            this.grille.test_win_or_loose();
        }
    }
    
    /**Fonction permettant d'éxaminer les cases voisines si une case seule d'un bateau a été trouvée,
     *(c'est à dire sans autre batteau dans le voisinage).
     *La fonction analyse donc les cases jouables dans le voisinage du batteau trouvée, et choisie une case aléatoirement parmis celle-ci
     */
    public boolean shot_with_neighbor()
    {
            int number_possibilities = 0; // variable pour le nombre de possibilité alentours
            int[][] next_possibilities = new int[4][2]; //tablo ki enregistre les voisins ou on peut cliker
            int x = this.record_place_of_boat[0][0]; // position I du bato trouvé
            int y = this.record_place_of_boat[0][1]; // position J du bato trouvé
            int rand_possibilities; 

           //premiere étape est de tester si il y a des cases de dispo sur les côté
            if(x-1 >= 0 && this.grille.cases[x-1][y].visited == false) 
            {
                number_possibilities ++;
                next_possibilities[number_possibilities-1][0] = x-1;
                next_possibilities[number_possibilities-1][1] = y;
            }
            if(x+1 <= Grille.grilleH-1 && this.grille.cases[x+1][y].visited == false)
            {
                number_possibilities ++;
                next_possibilities[number_possibilities-1][0] = x+1;
                next_possibilities[number_possibilities-1][1] = y;
            }
            if(y-1 >= 0 && this.grille.cases[x][y-1].visited == false) 
            {
                number_possibilities ++;
                next_possibilities[number_possibilities-1][0] = x;
                next_possibilities[number_possibilities-1][1] = y-1;
            }
            if(y+1 <= Grille.grilleW-1 && this.grille.cases[x][y+1].visited == false)
            {
                number_possibilities ++;
                next_possibilities[number_possibilities-1][0] = x;
                next_possibilities[number_possibilities-1][1] = y+1;
            }

            //si on a des case disponible a coté alors on en choisi une parmis elle aléatoirement
            if(number_possibilities != 0)
            {
                rand_possibilities = random(0, number_possibilities);
                random_shootI = next_possibilities[rand_possibilities][0];
                random_shootJ = next_possibilities[rand_possibilities][1];
                System.out.println("NEIGHBBOR RANDOM "); 
                return true;
            }
         return false;
    }
    
    /**Fonction jouant une case aléatoire parmis toutes les cases disponible de la grille*/
    public void random_shot()
    {
        do
            {
                this.random_shootI = random(0,this.grille.grilleH);
                this.random_shootJ = random(0,this.grille.grilleW);
            }while(this.grille.cases[random_shootI][random_shootJ].visited == true);
	
	sens_opposé = false;
	other_sens = 0;
    }
    
    /**Fonction permettant à l'IA de finir un bateau qu'elle aurait découvert :
     *si deux bateaux ont été trouvés sur la meme lignée, alors la fonction par dans un sens de cette lignée afin de compléter le bateau.
     *Si l'IA est bloqué lors de ce premier passage, par une case vide par exemple, ou le bord de la grille, alors la variable
     *'other_sens' est initialisé à 'TRUE' afin de repartir dans le sens opposé, et l'IA complète donc le bateau en question en repartant
     *dans le sens opposé.
     *
     *Une fois la procédure terminée, la fonction 'play' se charge de réinitialisé la variable touched_boat à 'false' afin que l'IA
     *reprenne ses coups aléatoires.
     */
    public boolean continue_with_same_boat()
    {
        boolean neighbor_boat = false;
        int x = this.record_place_of_boat[0][0]; // position I du bato trouvé
        int y = this.record_place_of_boat[0][1]; // position J du bato trouvé
        int j;
        
        //si un voisin est un bato
        if(sens_opposé == false)
        {
                //---en haut-----------------------------------------------------------------
                if(x-1 >= 0 && this.grille.cases[x-1][y].boat == true && this.grille.cases[x-1][y].visited == true)
                {
                    j=x;
                    while(j-1 >= 0 )
                    {
                        if(this.grille.cases[j-1][y].visited == false)
                        {
                            random_shootI = j-1;
                            random_shootJ = y;
                            System.out.println("HAUT");
                            other_sens = 1;
                            finish_the_boat = true;
                            return true;
                        } 
                        if(j-1 < 0 || this.grille.cases[j-1][y].visited == true && this.grille.cases[j-1][y].boat == false)
                        {
                            sens_opposé = true;
                            other_sens = 1;
                            break;
                        }/*
                        if(this.grille.cases[j-1][y].visited == true && this.grille.cases[j-1][y].boat == true)
                        {
                            other_sens = 1;
                            finish_the_boat = true;
                        }*/
                        j--;
                    }
                }
                //---en bas-----------------------------------------------------------------
                if(x+1 <= Grille.grilleH-1 && this.grille.cases[x+1][y].boat == true && this.grille.cases[x+1][y].visited == true)
                {
                    j=x;
                    while(j+1 <= Grille.grilleH-1 )
                    {
                        if(this.grille.cases[j+1][y].visited == false)
                        {
                            random_shootI = j+1;
                            random_shootJ = y;
                            System.out.println("BAS");
                            other_sens = 2;
                            finish_the_boat = true;
                            return true;
                        } 
                        if( j+1 >= Grille.grilleH || this.grille.cases[j+1][y].visited == true && this.grille.cases[j+1][y].boat == false)
                        {
                            sens_opposé = true;
                            other_sens = 2;
                            break;
                        }/*
                        if(this.grille.cases[j+1][y].visited == true && this.grille.cases[j+1][y].boat == true)
                        {
                            sens_opposé = true;
                            other_sens = 2;
                            finish_the_boat = true;
                        }*/
                        j++;
                    }
                }
                //---a gauche----------------------------------------------------------------
                if(y-1 >= 0 && this.grille.cases[x][y-1].boat == true && this.grille.cases[x][y-1].visited == true)
                {
                    j=y;
                    while(j-1 >= 0 )
                    {
                        if(this.grille.cases[x][j-1].visited == false)
                        {
                            random_shootI = x;
                            random_shootJ = j-1;
                            System.out.println(" GAUCHE");
                            other_sens = 3;
                            finish_the_boat = true;
                            return true;
                        } 
                        if( j-1 < 0 || this.grille.cases[x][j-1].visited == true && this.grille.cases[x][j-1].boat == false)
                        {
                            sens_opposé = true;
                            other_sens = 3;
                            break;
                        }/*
                        if(this.grille.cases[x][j-1].visited == true && this.grille.cases[x][j-1].boat == true)
                        {
                            sens_opposé = true;
                            other_sens = 3;
                            finish_the_boat = true;
                        }*/
                        j--;
                    }
                }
                //----a droite---------------------------------------------------------------
                if(y+1 <= Grille.grilleW-1 && this.grille.cases[x][y+1].boat == true && this.grille.cases[x][y+1].visited == true)
                {
                    j=y;
                    while(j+1 <= Grille.grilleW-1 )
                    {
                        if(this.grille.cases[x][j+1].visited == false)
                        {
                            random_shootI = x;
                            random_shootJ = j+1;
                            System.out.println("DROITE");
                            other_sens = 4;
                            finish_the_boat = true;
                            return true;
                        } 
                        if(j+1 >= Grille.grilleW || this.grille.cases[x][j+1].visited == true && this.grille.cases[x][j+1].boat == false)
                        {
                            sens_opposé = true;
                            other_sens = 4;
                            break;
                        }/*
                        if(this.grille.cases[x][j+1].visited == true && this.grille.cases[x][j+1].boat == true)
                        {
                            sens_opposé = true;
                            other_sens = 4;
                            finish_the_boat = true;
                        }*/
                        j++;
                    }
                }
        }
        System.out.println("opposé = "+ other_sens);
        //-------------------------------------------------------------------------------
        //en bas
        if(sens_opposé == true)
        {
                if(other_sens == 1)
                {
                    j=x;
                    while(j+1 <= Grille.grilleH-1 )
                    {
                        if(this.grille.cases[j+1][y].visited == false)
                        {
                            random_shootI = j+1;
                            random_shootJ = y;
                            System.out.println("opposé BAS");
                            return true;
                        } 
                        if(j+1 >= Grille.grilleH || (this.grille.cases[j+1][y].visited == true && this.grille.cases[j+1][y].boat == false))
                        {
                            this.touched_boat = false; // optionnel 
                            this.finish_the_boat = false;
                            this.other_sens = 0;
                            this.sens_opposé = false;
                            random_shot();
                            return true;
                        }
                        j++;
                    }
                }
                //------------------------------------------------------------------------------
                // en haut
                if(other_sens == 2)
                {
                    j=x;
                    while(j-1 >= 0)
                    {
                        if(this.grille.cases[j-1][y].visited == false)
                        {
                            random_shootI = j-1;
                            random_shootJ = y;
                            System.out.println("opposé HAUT");
                            return true;
                        } 
                        if(j-1 < 0 || (this.grille.cases[j-1][y].visited == true && this.grille.cases[j-1][y].boat == false))
                        {
                            this.touched_boat = false;
                            this.finish_the_boat = false;
                            this.other_sens = 0;
                            this.sens_opposé = false;
                            random_shot();
                            return true;
                        }
                        j--;
                    }
                }
                //----------------------------------------------------------------------------------
                if(other_sens == 3)
                {
                    j=y;
                     while(j+1 <= Grille.grilleW-1 )
                    {
                        if(this.grille.cases[x][j+1].visited == false)
                        {
                            random_shootI = x;
                            random_shootJ = j+1;
                            System.out.println("opposé GAUCHE");
                            return true;
                        } 
                         if(j+1 >= Grille.grilleW || (this.grille.cases[x][j+1].visited == true && this.grille.cases[x][j+1].boat == false))
                        {
                            this.touched_boat = false;
                            this.finish_the_boat = false;
                            this.other_sens = 0;
                            this.sens_opposé = false;
                            random_shot();
                            return true;
                        }
                        j++;
                    }
                }
                //---------------------------------------------------------------------------------

                if(other_sens == 4)
                {
                    j=y;
                    while(j-1 >= 0 )
                    {
                        if(this.grille.cases[x][j-1].visited == false)
                        {
                            random_shootI = x;
                            random_shootJ = j-1;
                            System.out.println("opposé DROITE");
                            return true;
                        } 
                        if(j-1 < 0 || (this.grille.cases[x][j-1].visited == true && this.grille.cases[x][j-1].boat == false))
                        {
                            this.touched_boat = false;
                            this.finish_the_boat = false;
                            this.other_sens = 0;
                            this.sens_opposé = false;
                            random_shot();
                            return true;
                        }
                        j--;
                    }
                }
        }
        
         if(finish_the_boat == true)
                sens_opposé = true;
       return false;
    }
    
}
















































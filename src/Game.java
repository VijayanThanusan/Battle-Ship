
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;

/*
    FONCTIONNEMENT DE LA CLASSE GAME :

    La classe Game est la classe principale du jeu, elle regroupe les principale fonctions qui vont faire tourner le jeu.
    Il y a principalement trois fonctions qui s'y trouve :
    - Mode Solo
    - Mode Joueur contre Joueur
    - Mode Joueur contre IA

    1) Le mode solo nous permet de joueur seul : on choisi au départ une difficultée parmis trois difficultée, 
    en fonction de la difficultée choisi, le joueur devra terminer la partie en un certain nombre de coup limité
    2) Le mode Joueur contre Joueur nous permet de jouer contre un autre joueur humain : chacun joue a sont tour jusqu'a
    ce que l'un des joueurs ai trouvé tous les bato de l'adversaire.
    3) Le mode Joueur contre IA nous permet de jouer contre l'ordinateur : le joueur commence a jouer, suivi de l'IA 
    si le joueur gagne alors sont tour continue et il reclic encore juska ce qu'il perd, il en est de même pour l'IA ..

    Ces trois modes sont au départs tous chargé dans la classe Acceuil, et lorsque l'on clique sur notre choix dans l'acceuil, il nous
    redirige directement sur la scene en question
    Pour ce faire il y a donc trois scènes de type "Scene" et trois groupe de type "Group" car il y a trois modes diffférent
    Chaque mode est censé avoir une contenu différent donc on ne peut pas afficher les trois modes de jeu dans une seule scene et 
    dans un seul groupe.
    Chaque groupe contient donc les éléments graphiques correspondant a son mode de jeu réspéctif.
    
    Chacune de ses trois fonctions possèdent leur variables réspéctives qui sont déclaré en tant qu'attributs dans la classe Game,
    il sont déclaré en tant qu'attribut et non déclaré dans leur propre fonction car, ces variables sont en mode static certaines
    d'entre elles seront utilisé mais dans d'autre classe, tels que dans la classe Case par exemple ou on affiche le score de la
    grille a chaque clik sur une case.
    Parmis ses variables on trouve principalement les grilles de chaque mode ; en tout 5 grille sont initialisée :
    - La grille "joueur_solo" pour le mode solo
    - Deux grilles "joueur1" et "joueur2" pour les deux joueur du mode "Joueur contre Joueur"
    - Une grille "joueur_mode_IA"  pour la grille du joueur dans le mode "Joueur contre IA"
    - Et enfin une variable de type IA dans la quelle la grille est déja initialisé dans la classe elle même.
    Plusieurs grille ont été créé et non une seule ou deux pour évité les erreurs de contenu, et pour bien différencier chaque grille ...
    cela est certes un peu plus encombrant question mémoire, mais plus compréhensible pour le code, on différencie bien de la sorte
    quelle grille appartenant à quel mode de jeu ...

    Enfin une fonction reset permet de réinitialisé la grille passé en paramètre et y replace des bato, afin de pouvoir rejouer.
*/

/**Classe principale du programme, regroupant les différents mode de jeu ainsi que les variables principales du programme*/
public class Game 
{
    /**Les scenes pour chaque fenetre*/
    public Scene parti_vs_IA, parti_vs_joueur, parti_solo, difficulty_screen ;
    /**Les groupes pour chaque fenetre*/
    public static Group groupe_solo, groupe_joueur_vs_IA, groupe_joueur_vs_joueur, difficulty_group_solo;
    /**Enumeration listant les différents mode de jeu*/
    enum Mode {SOLO, JOUEUR_VS_JOUEUR, JOUEUR_VS_IA} ; 
    /**Variable de type enumeration*/
    public static Mode mode_de_jeu; 
    /**Variable de l'IA*/
    public static IA ia; 
    /**Création des grilles pour chaqe mode : 5 grille au total pour tous le programme */
    public static Grille joueur_solo, joueur1, joueur2, joueur_mode_IA;
    /**Variable contenant le nombre de coup_restant pour le mode solo*/
    public static int coup_restant;
    /**Variable contenant le nombre de coup de d"part en fonction de la difficultée choisie pour le mode solo*/
    public static int coup_depart;
    /**Variable de type 'Text' pour afficher le nombre de coup restant dans le mode solo*/
    public static Text text_coup_restant;
    
    
    /**Constructeur de la classe Game : ce constructeur initialise l'ensemble les différentes scènes des différents mode de jeu du programme*/
    public Game(Stage jeu)
    {
        //Initialisation des variables principale pour le jeu
        text_coup_restant = new Text(); // texte affichant le nombre de coup restant pour le mode solo
        coup_restant = 0; // variable stockant le nombre de coup restant pour le mode solo
        
        groupe_joueur_vs_IA = new Group(); // group pour le mode joueur contre IA
        groupe_solo = new Group(); // group pour le mode solo
        difficulty_group_solo = new Group(); // group affichant les boutons de difficultée pour le mode solo
        groupe_joueur_vs_joueur = new Group();// group pour le mode joueur contre joueur
        
        difficulty_screen = new Scene(difficulty_group_solo, 1024, 597, Color.WHITE); //scene pour les choix de difficulté du mode solo
        parti_vs_IA = new Scene(groupe_joueur_vs_IA, 1024, 597, Color.WHITE);  // scene pour le mode joueur contre IA
        parti_vs_joueur = new Scene(groupe_joueur_vs_joueur, 1024, 597, Color.WHITE);  // scene pour le mode joueur contre joueur
        parti_solo = new Scene(groupe_solo, 1024, 597, Color.WHITE);  // scene pour le mode solo
    }
    
    /**Fonction qui affiche les trois bouton de difficulté lorsque l'on accède au mode solo, et initialise le nombre de coup de départ
     *dans l'attribut 'int coup_depart'.
     *- "Facile" = 80 coups
     *- "Moyen" = 50 coups
     *- "Difficile" = 30 coups
     */
    public void difficulty_screen(Stage jeu)
    {
        Text text = new Text();
        ImageView fond_jeu = new ImageView(new Image(getClass().getResource("images/jeu.jpeg").toExternalForm()));
        HBox difficulty_button = new HBox(); //creation d'une VBox pour alinger les boutonsVbox
        difficulty_button.setSpacing(80);//espace entre boutonsVbox
        difficulty_button.setTranslateX(325); // position de la Vbox
        difficulty_button.setTranslateY(270);
        
        text.setFont(Font.font("Brushed script", FontWeight.BOLD, 35));
        text.setFill(Color.WHITE);
        text.setX(difficulty_button.getTranslateX() );
        text.setY(difficulty_button.getTranslateY()-70);
        text.setOpacity(0.8); 
        text.setText("Choisissez la difficultée");
        
        //on créé les boutons difficultée, si on clik sur un bouton on initialise les variables a une certaine difficultée
                Button facile = new Button("Facile");
                facile.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e)
                    {
                        coup_depart = 80;
                        coup_restant = 80;
                        afficher_coup_restant();
                        jeu.setScene(parti_solo);
                    }
                });
                facile.setMaxWidth(100); //definition de la taille du bouton
                facile.setFont(Font.font("Cambria", 17));
                facile.setOpacity(0.8);

                Button moyen = new Button("Moyen");
                moyen.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e)
                    {
                        coup_depart = 50;
                        coup_restant = 50;
                        afficher_coup_restant();
                        jeu.setScene(parti_solo);
                    }
                });
                moyen.setMaxWidth(100); //definition de la taille du bouton
                moyen.setFont(Font.font("Cambria", 17));
                moyen.setOpacity(0.8);

                Button difficile = new Button("Difficile");
                difficile.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e)
                    {
                        coup_depart = 30;
                        coup_restant = 30;
                        afficher_coup_restant();
                        jeu.setScene(parti_solo);
                    }
                });
                difficile.setMaxWidth(100); //definition de la taille du bouton
                difficile.setFont(Font.font("Cambria", 17));
                difficile.setOpacity(0.8);
                
                Button retour = new Button("Retour");
                retour.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e)
                    {
                        jeu.setScene(Acceuil.Accueil);
                    }
                });
                retour.setMaxWidth(100); //definition de la taille du bouton
                retour.setFont(Font.font("Cambria", 17));
                retour.setTranslateX(474);
                retour.setTranslateY(400);
                retour.setOpacity(0.8);
                
        
        difficulty_group_solo.getChildren().add(fond_jeu); 
        difficulty_group_solo.getChildren().add(text);
        difficulty_group_solo.getChildren().add(retour);
        difficulty_button.getChildren().addAll(facile, moyen, difficile);
        difficulty_group_solo.getChildren().add(difficulty_button); 
    }
  
    /**Fonction initialisant le mode solo en fonction de la difficultée choisie.
     *Dans ce mode, le joueur possède une grille, et doit trouver l'intégralité des bateaux cachés (Les bateaux sont placés aléatoirements)
     *avec un nombre de coup définit par la difficultée choisie au départ.
     *Le joueur a la possibilité de réinitialisé le mode.
     */
    public void mode_joueur_solo(Stage jeu)
    {
        joueur_solo = new Grille(200,90,true);
        ImageView fond_jeu = new ImageView(new Image(getClass().getResource("images/jeu.jpeg").toExternalForm()));
        
        Bateau bateau = new Bateau(0);
        bateau.initBateau(joueur_solo.cases);
        
        //bouton reset pour réinitialisé la grille du joueur et le compteur
        Button reset = new Button("Reset");
        reset.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur_solo, groupe_solo);
            }
        });
        reset.setMaxWidth(100); //definition de la taille du bouton
        reset.setTranslateX((parti_vs_IA.getWidth()/2) - 136);
        reset.setTranslateY(10);
        reset.setFont(Font.font("Cambria", 17));
        reset.setOpacity(0.8);
        
        //boutons retour pour revenir à l'acceuil a partir des parties
        Button retour = new Button("Retour");
        retour.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur_solo, null);
                jeu.setScene(difficulty_screen);
            }
        });
        retour.setMaxWidth(100); //definition de la taille du bouton
        retour.setTranslateX((parti_vs_IA.getWidth()/2) - 140);
        retour.setTranslateY(50);
        retour.setFont(Font.font("Cambria", 17));
        retour.setOpacity(0.8);
        
        groupe_solo.getChildren().add(fond_jeu); 
        groupe_solo.getChildren().add(joueur_solo);
        groupe_solo.getChildren().add(reset);
        groupe_solo.getChildren().add(retour);
        groupe_solo.getChildren().add(text_coup_restant);
        
    }
    
    /**Fonction qui affiche le nombre de clique que l'on peut encore éffectuer pour le mode solo*/
    static void afficher_coup_restant()
    {
        text_coup_restant.setFont(Font.font("Brushed script", FontWeight.BOLD,25 ));
        text_coup_restant.setFill(Color.WHITE);
        text_coup_restant.setX(640); 
        text_coup_restant.setY(190); //120
        //text_coup_restant.setOpacity(0.8); 
        text_coup_restant.setStroke(Color.BLACK);
        text_coup_restant.setText("Détruisez les bateaux \n"
                +"ennemis en "+ coup_depart +" coups \n"
		+"maximum ! \n\n"
                +"   Coups restant : "+ coup_restant);
        
    }
    
    /** Fonction initialisant le mode joueur contre joueur :
     *Ce mode permet a deux joueurs humains de s'affronter. Chacun des joueurs possédant une grille, (Les bateaux sont placés aléatoirements),
     *le but du jeu est de trouvé l'intégralité des bateaux de l'adversaire avant que celui-ci ne coule tous les notres.
     *Les joueurs ont la possibilitées de réinitialisées la partie.
     */
    public void mode_joueur_vs_joueur(Stage jeu)
    {
        Text nameText = new Text();
        
        joueur1 = new Grille(35,90,true);
        joueur2 = new Grille(570,90,true);
        
        ImageView fond_jeu = new ImageView(new Image(getClass().getResource("images/jeu.jpeg").toExternalForm()));

        Bateau bateau = new Bateau(0);
        bateau.initBateau(joueur1.cases);
        bateau.initBateau(joueur2.cases);
        
        Button reset = new Button("Reset");
        reset.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur1, groupe_joueur_vs_joueur);
                reset(joueur2, groupe_joueur_vs_joueur);
            }
        });
        reset.setMaxWidth(100); //definition de la taille du bouton
        reset.setTranslateX((parti_vs_IA.getWidth()/2) - 36);
        reset.setTranslateY(10);
        reset.setFont(Font.font("Cambria", 17));
        reset.setOpacity(0.8);
        
        //boutons retour pour revenir à l'acceuil a partir des parties
        Button retour = new Button("Retour");
        retour.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur1, null);
                reset(joueur2, null);
                jeu.setScene(Acceuil.Accueil);
            }
        });
        retour.setMaxWidth(100);
        retour.setTranslateX((parti_vs_IA.getWidth()/2) - 40);
        retour.setTranslateY(50);
        retour.setFont(Font.font("Cambria", 17));
        retour.setOpacity(0.8);
        
        nameText.setFont(Font.font("Brushed script", FontWeight.BOLD, 35));
        nameText.setFill(Color.WHITE);
        nameText.setX(35 + 150);
        nameText.setY(50);
        nameText.setOpacity(0.8); 
	nameText.setStroke(Color.BLACK);
        nameText.setText("JOUEUR 1\t\t\t\t\tJOUEUR 2 ");

        groupe_joueur_vs_joueur.getChildren().add(fond_jeu); 
        groupe_joueur_vs_joueur.getChildren().add(nameText); 
        groupe_joueur_vs_joueur.getChildren().add(joueur2);
        groupe_joueur_vs_joueur.getChildren().add(joueur1);
        groupe_joueur_vs_joueur.getChildren().add(reset);
        groupe_joueur_vs_joueur.getChildren().add(retour);
    }
    
    /**Fonction initialisant le mode joueur contre IA :
     *Ce mode permet au joueur de jouer contre l'ordinateur. (Les bateaux sont placés aléatoirements).
     *Le joueur et l'IA possédant chacun leur grille, le but sera tous comme le mode joueur cotre joueur, de coulé l'intégralité des
     *bateaux de l'adversaires avant que celui-ci ne coule les notres.
     *Le joueur a la possibilité de réinitialisé la partie.
     */
    public void mode_joueur_vs_IA(Stage jeu)
    {
        Text nameText = new Text();
        ImageView fond_jeu = new ImageView(new Image(getClass().getResource("images/jeu.jpeg").toExternalForm()));
        joueur_mode_IA = new Grille(35,90,true);
        ia = new IA();

        Bateau bateau = new Bateau(0);
        bateau.initBateau(joueur_mode_IA.cases);
        bateau.initBateau(ia.grille.cases);
        ia.print_adversary_boat();
        
        Button reset = new Button("Reset");
        reset.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur_mode_IA, groupe_joueur_vs_IA);
                reset(ia.grille, groupe_joueur_vs_IA);
                ia.print_adversary_boat();
            }
        });
        reset.setMaxWidth(100); //definition de la taille du bouton
        reset.setTranslateX((parti_vs_IA.getWidth()/2) - 36);
        reset.setTranslateY(10);
        reset.setFont(Font.font("Cambria", 17));
        reset.setOpacity(0.8);
        
        //boutons retour pour revenir à l'acceuil a partir des parties
        Button retour = new Button("Retour");
        retour.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                reset(joueur_mode_IA, null);
                reset(ia.grille, null);
                ia.print_adversary_boat();
                jeu.setScene(Acceuil.Accueil);
            }
        });
        retour.setMaxWidth(100); //definition de la taille du bouton
        retour.setTranslateX((parti_vs_IA.getWidth()/2) - 40);
        retour.setTranslateY(50);
        retour.setFont(Font.font("Cambria", 17));
        retour.setOpacity(0.8);
        
        nameText.setFont(Font.font("Brushed script", FontWeight.BOLD, 35));
        nameText.setFill(Color.WHITE);
        nameText.setX(185);
        nameText.setY(50);
        nameText.setOpacity(0.8); 
	nameText.setStroke(Color.BLACK);
        nameText.setText("JOUEUR\t\t\t\tANDROID ");
        
        groupe_joueur_vs_IA.getChildren().add(fond_jeu);  
        groupe_joueur_vs_IA.getChildren().add(ia.grille);
        groupe_joueur_vs_IA.getChildren().add(joueur_mode_IA);
        groupe_joueur_vs_IA.getChildren().add(retour);
        groupe_joueur_vs_IA.getChildren().add(nameText);
        groupe_joueur_vs_IA.getChildren().add(reset);
       
    }

    /**Fonction reset qui réinitialise la grille dans son 'group' correspondant passé en paramètres.
     *Cette fonction parcours l'intégralitée de la grille, et réinitialise chacun des champs de chaque case.
     *Une fois la grille réinitialisée, une appel de la fonction 'initBateau' est éfféctué afin de replacer des bateaux aléatoirement
     */
    public void reset(Grille grille, Group group)
    {
        for(int i=0; i<grille.grilleH; i++)
        {
            for(int j=0; j< grille.grilleW; j++)
            {
                if(grille.cases[i][j].boat == true || grille.cases[i][j].visited == true)
                {
                    grille.cases[i][j].boat = false;
                    grille.cases[i][j].visited = false;
                    grille.cases[i][j].forme.setFill(Color.WHITE);
                }
                
            }       
        }
        
        Bateau bateau= new Bateau(0);
        bateau.initBateau(grille.cases);
        
        grille.score();
        
        Text reset_text = new Text();
        reset_text.setFont(Font.font("Brushed script", FontWeight.BOLD, 13));
        reset_text.setFill(Color.WHITE);
        reset_text.setX(5);
        reset_text.setY(14);
        reset_text.setOpacity(0.8); 
        reset_text.setText("Grille Réinitialisée avec succès !");
                
        if(group != null)
        {
            Timeline print_reset = new Timeline(
                                new KeyFrame(
                                  Duration.ZERO,
                                  actionEvent ->  group.getChildren().add(reset_text)
                                    ),
                                new KeyFrame(
                                 Duration.millis(2000), actionEvent -> reset_text.setVisible(false)) 
                                );
                                print_reset.play();
        }
      
        //si le mode est solo, alors on doit aussi réinitialisé le nombre de coup restant et le remettre a sa valeur de départ
        if(mode_de_jeu == Mode.SOLO)
        {
            if(coup_depart == 80)
                coup_restant = 80;
            if(coup_depart == 50)
                coup_restant = 50;
            if(coup_depart == 30)
                coup_restant = 30; 
            
            afficher_coup_restant();
        }
        
        //Ceci sert à retirer les images win et game_over lorsque l'on a gagné ou perdu et que l'on clique sur reset pour recommencer
        if(grille.win_picture != null)
            grille.win_picture.setVisible(false);
        if(grille.game_over_picture != null)
            grille.game_over_picture.setVisible(false);
    }
    
    
    
    
}

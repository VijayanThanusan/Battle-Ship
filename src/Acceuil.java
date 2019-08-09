
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


/**Classe regroupant les règles, et les différents modes de jeu*/
public class Acceuil
{
    /** Variable de type 'Scene' initialisant les scènes pour l'acceuil, les règles, et les crédits*/
    public static Scene Accueil, sceneRegle, credits;
    /** Variable de type 'Group' initialisant les group pour l'acceuil, les règles, et les crédits*/
    public Group groupe_accueil, groupe_regles, groupe_credits;

    /**Constructeur de la classe Acceuil
     * Constructeur initialisant plusieurs bouttons afin que le joueur puise accéder aux différentes fonction du programmes
     * - Mode solo
     * - Mode joueur contre joueur
     * - Mode joueur contre IA
     * - Règles du jeu
     * - Crédits
     */
    public Acceuil(Stage jeu)
    {
        groupe_accueil = new Group();
        groupe_credits = new Group();
        groupe_regles = new Group();
        Accueil = new Scene(groupe_accueil, 800, 537);
        sceneRegle = new Scene(groupe_regles, 900, 460, Color.WHITE);
        credits = new Scene(groupe_credits, 800, 537, Color.DARKGREY);
        Regles regles = new Regles(sceneRegle);

        credits(jeu); // fonction qui charge les credits

        Game game = new Game(jeu);
        game.mode_joueur_vs_IA(jeu); // Chargement des trois mode
        game.mode_joueur_vs_joueur(jeu);
        game.mode_joueur_solo(jeu);
        game.difficulty_screen(jeu);

        Button regle = new Button("Règles");  // creation bouton regles
        Button jouer = new Button("Mode Solo"); // creation du bouton jouer
        Button jouerp = new Button("Joueur VS Joueur ");
        Button jouerpc = new Button("Joueur VS Android");
        Button credits = new Button("Crédits");
        Button back = new Button("Retour");
        int taille_boutton = 250;

        ImageView fond_ecran = new ImageView(new Image(getClass().getResource("images/bataille_Gibraltar.jpg").toExternalForm())); //ajout d'image
        ImageView fond_regle = new ImageView(new Image(getClass().getResource("images/regle_fond_1.png").toExternalForm())); // ajout d'image
        ImageView fond_jeu = new ImageView(new Image(getClass().getResource("images/jeu.jpeg").toExternalForm()));
        fond_regle.setFitHeight(450);
        fond_ecran.setOpacity(0.9);
        fond_regle.setOpacity(0.9);

        groupe_regles.getChildren().add(fond_regle);
        groupe_regles.getChildren().add(regles);
        groupe_regles.getChildren().add(back);

        //alignement element graphique
        VBox boutonsVbox = new VBox(); //creation d'une VBox pour alinger les boutonsVbox
        boutonsVbox.setSpacing(25);//espace entre boutonsVbox
        boutonsVbox.setAlignment(Pos.CENTER);//position dans fenetre
        boutonsVbox.setTranslateX(335); // position de la Vbox
        boutonsVbox.setTranslateY(180);

        Text game_title = new Text("BATAILLE NAVALE"); //creation du texte "bataille navale"
        game_title.setFont(Font.font("Brushed script", FontWeight.BOLD, 70));
        game_title.setX(65);
        game_title.setY(80);
        game_title.setFill(Color.WHITE);

        Rectangle rectangle;
        rectangle = new Rectangle(216,300, Color.GREY);
        rectangle.setStroke(Color.WHITE);
        rectangle.setX(boutonsVbox.getTranslateX()-20);
        rectangle.setY(boutonsVbox.getTranslateY()-20);
        rectangle.setOpacity(0.7);

        //boutons jouer solo
        jouer.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                game.mode_de_jeu = Game.Mode.SOLO;
                jeu.setScene(game.difficulty_screen);
            }
        });
        jouer.setMaxWidth(taille_boutton); //definition de la taille du bouton*/
        jouer.setFont(Font.font("Cambria", 17));
        jouer.setOpacity(0.8);

        //boutons jouer contre un joueur
        jouerp.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                game.mode_de_jeu = Game.Mode.JOUEUR_VS_JOUEUR;
                jeu.setScene(game.parti_vs_joueur);
            }
        });
        jouerp.setMaxWidth(taille_boutton); //definition de la taille du bouton*/
        jouerp.setFont(Font.font("Cambria", 17));
        jouerp.setOpacity(0.8);


        //boutons jouer contre l'IA
        jouerpc.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e)
            {
                game.mode_de_jeu = Game.Mode.JOUEUR_VS_IA;
                jeu.setScene(game.parti_vs_IA);
            }
        });
        jouerpc.setMaxWidth(taille_boutton); //definition de la taille du bouton*/
        jouerpc.setFont(Font.font("Cambria", 17));
        jouerpc.setOpacity(0.8);

        //boutons règles
        regle.setOnAction(e -> jeu.setScene(sceneRegle)); //lorsqu'on clique sur le bouton on affiche la fenetre sceneregle
        regle.setMaxWidth(taille_boutton); // definition de la taille du bouton
        regle.setFont(Font.font("Cambria", 17));
        regle.setOpacity(0.8);

        //boutons crédits
        credits.setOnAction(e -> jeu.setScene(Acceuil.credits)); //lorsqu'on clique sur le bouton on affiche la fenetre sceneregle
        credits.setMaxWidth(taille_boutton); // definition de la taille du bouton
        credits.setFont(Font.font("Cambria", 17));
        credits.setOpacity(0.8);

        //boutons retour pour revenir à l'acceuil a partir des règles du jeu
        back.setOnAction(e -> jeu.setScene(Accueil));
        back.setTranslateX(400);
        back.setTranslateY(400);

        boutonsVbox.getChildren().addAll(jouer,jouerp, jouerpc, regle,credits); //ajout des boutonsVbox et du titre dans la Vbox
        groupe_accueil.getChildren().add(fond_ecran); //ajout de l'image dans le groupe accueil
        groupe_accueil.getChildren().add(game_title);
        groupe_accueil.getChildren().add(rectangle);
        groupe_accueil.getChildren().add(boutonsVbox);  // ajout des bouton dans le groupe accueil
    }

    /**Fonction affichant les crédits*/
    public void credits(Stage jeu)
    {
        Text text_credits = new Text();
        Button back = new Button("Retour");

        back.setOnAction(e -> jeu.setScene(Accueil));
        back.setTranslateX(400);
        back.setTranslateY(400);

        text_credits.setFont(Font.font("Brushed script", FontWeight.BOLD, 17));
        text_credits.setFill(Color.WHITE);
        text_credits.setX(240);
        text_credits.setY(160);
        //text_credits.setOpacity(0.8);
        text_credits.setText("Projet Bataille Navale réalisé par : \n\n"
                + "\t\t- Steven Vouidibio\n"
                + "\t\t- Vijayakulanathan Thanushan\n"
                + "\t\t- Mohamed Benomari\n"
                + "\t\t- Mohamed Nehari\n\n"
                + " dans le cadre du cours de Réalisation\n"
                + "     de programmes de Mme Balmas\n\n");

        this.groupe_credits.getChildren().add(text_credits);
        this.groupe_credits.getChildren().add(back);
    }
}


import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**Classe implémentant une grille complète à l'aide d'objets de type 'Case'*/
public class Grille extends Parent{

    /**Tableau à deux dimensions de types 'Case', contenant toutes les cases de la grille*/
    public Case [][] cases;
    /**Position en absicce et en ordonnée de la grille sur la fenetre principale*/
    public int positionX, positionY;
    /**Nombre de case de la grille en longueur et en largeur*/
    public static int grilleH = 10, grilleW = 10;
    /**variable de type 'Text' affichant le score*/
    public Text score = new Text();
    /**Variable servant à compter le nombre de bateau coulé pour savoir si l'on a gagné ou pas*/
    public int nombre_bato_coulé; 
    /**Variable de type 'ImageView' initialisant les images 'win.gif' et 'game_over.png'*/
    public ImageView win_picture, game_over_picture; 
    /**Variable définissant si la grille est faite pour l'IA ou non (ceci car l'on ne doit pas pouvoir cliquer sur la grille de l'IA)*/
    public boolean is_IA_grille ;
    
    /**
    Constructeur de la classe Grille : ce constructeur créé une grille en fonction de trois paramètres : 
    - La position en absicce (int positionX)
    - La position en ordonnée (int positionY)
    - La possibilitée ou non de pouvoir cliquer sur la grille (boolean can_i_click)
    */
    public Grille(int positionX, int positionY, boolean can_i_click){
        
        this.positionX = positionX;
        this.positionY = positionY;
        this.nombre_bato_coulé = 0;
        
        if(can_i_click == true)
            this.is_IA_grille = false;
        else
            this.is_IA_grille = true;
        
        GridPane plateau = new GridPane();
        plateau.setPadding(new Insets(positionY, 0, 0, positionX)); // hauteur , longueur
        plateau.setVgap(0);
        plateau.setHgap(0);     

        this.cases = new Case[this.grilleW][this.grilleH];//Tableau de 10*10 cases        
      
        for(int i = 0; i < this.grilleH; i++)
        {
                for(int j=0; j < this.grilleW; j++)
                { 
                    this.cases[i][j] = new Case(can_i_click);//i=abs,j=ord,0=etat                 
                    plateau.add(this.cases[i][j], j, i); //inverser j et i car dans gridpane le remplissage se fait verticalement par defaut
                 }
        }   
        score();
        this.getChildren().add(plateau);       
   } 
    
    
    /**Fonction qui affiche le score de la grille.
     *Cette fonction parcours l'intégralité de la grille, et stocke dans l'attribut (nombre_bato_coulé) le nombre de bateau qui 
     *a été coulé dans la grille afin de l'afficher par la suite.
     */
    public void score()
    {    
       for(int i=0; i< this.grilleH; i++)
       {
           for(int j=0; j< this.grilleW ; j++)
           {
               if(this.cases[i][j].boat == true && this.cases[i][j].visited == true)
                   nombre_bato_coulé++;
           }
       }
       
        this.score.setFont(Font.font("Brushed script", FontWeight.BOLD, 22));
        this.score.setFill(Color.WHITE);
        this.score.setX(this.positionX  + 23);
        this.score.setY(this.positionY + (cases[0][0].forme.getHeight()*(grilleW+1)) + 15);
        this.score.setOpacity(0.8);
	this.score.setStroke(Color.BLACK);
        this.score.setText(+nombre_bato_coulé+" / 17 Bateau adverse détruit"); 
        
        try{
            this.getChildren().add(this.score);}
        catch(Exception e){};  
        
        this.nombre_bato_coulé = 0;
    }
    
    /**Fonction qui parcoure le contenu de la grille, et qui teste si la grille en question est gagnante ou perdante, 
     *si elle est gagnante on appelle la fonction 'print_win', si elle est perdante on appelle la fonction 'print_game_over'.
     */
    public void test_win_or_loose()
    {
        int bato_trouvé = 0;
        int case_visitée = 0;
        
        //boucle qui va compter le nombre de bato coulé
        for(int i=0; i< this.grilleH; i++)
        {
           for(int j=0; j< this.grilleW ; j++)
           {
               if(this.cases[i][j].boat == true && this.cases[i][j].visited == true)
                   bato_trouvé++;
               if(this.cases[i][j].visited == true)
                   case_visitée++;
           }
        }
        
        //si on est en mode solo, on verifie le nombre de coup restant pour voir si on ne l'a pas dépasser ..
        if(Game.mode_de_jeu == Game.Mode.SOLO)
        {
            if(Game.coup_depart - Game.coup_restant == Game.coup_depart)
                print_game_over();
        }
        
        //si on est en mode joueur contre IA si l'IA gagne alors on aura perdu, il faut donc inverses win et game_over
        if(Game.mode_de_jeu == Game.Mode.JOUEUR_VS_IA)
        {
            if(this.is_IA_grille == true) // si la grille en question apartient bien a l'IA
            {
                if(bato_trouvé == 17)
                    print_game_over();
                if(case_visitée == grilleH*grilleW)
                    print_win();
            }
            return;
        }
        
        //si le nombre de bato coulé trouvé est bien égal au total alors on a gagné ...
        if(bato_trouvé == 17)
            print_win();
        
        //si toute les cases du tableau sont remplie en premier, alors on a gagné
        if(case_visitée == grilleH*grilleW)
            print_win();
    }
    
    /**Fonction servant à afficher l'image 'win.gif' à des coordonnées définies selon le mode de jeux*/
    public void print_win()
    {
        win_picture = new ImageView(new Image(getClass().getResource("images/win.gif").toExternalForm()));
        if(Game.mode_de_jeu == Game.Mode.SOLO)
        {
            win_picture.setX(260);
            win_picture.setY(152);
        }
        else
        {
            win_picture.setX(372);
            win_picture.setY(152);
        }
        this.getChildren().add(win_picture);
    }
    
    /**Fonction servant à afficher l'image 'game_over.png' à des coordonnées définies selon le mode de jeux*/
    public void print_game_over()
    {
        game_over_picture = new ImageView(new Image(getClass().getResource("images/game_over.png").toExternalForm()));
        if(Game.mode_de_jeu == Game.Mode.SOLO)
        {
            game_over_picture.setX(260);
            game_over_picture.setY(152);
        }
        else
        {
            game_over_picture.setX(366);
            game_over_picture.setY(159);
        }
        this.getChildren().add(game_over_picture);
    }
}

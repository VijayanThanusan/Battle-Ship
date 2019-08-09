import javafx.scene.Parent;


/**Classe permetant la création et l'initialisation des bateaux*/
public class Bateau extends Parent {
    ///Taille du bateau (de 2 à 5 cases)
    public int taille;
    ///Vie du bateau
    private int vie;
    ///État du bateau : coulé/non coulé
    public boolean etat;
    ///Bateau placé verticalement ou horizontalement
    public int orientation;


    /**Constructeur de la classe : ce constructeur initialise un bateau en initialisant ses champs,
     *le paramètre 't' définit le nombre de case que comportera le bateau
     */
    Bateau(int t) {
        this.vie = this.taille = t;
        this.etat = false;
        this.orientation = Bateau.rand(1, 2);
    }

    /**Fonction permettant d'initialiser les bateaux du jeu, et faisant appel à la fonction 'placerBateau' afin de les placer dans la
     *grille passée en paramètre
     */
    public void initBateau(Case cases[][]) {
        Bateau b1=new Bateau(5);
        Bateau b2=new Bateau(4);
        Bateau b3=new Bateau(3);
        Bateau b4=new Bateau(3);
        Bateau b5=new Bateau(2);

        b1.placerBateau(b1,cases);
        b2.placerBateau(b2,cases);
        b3.placerBateau(b3,cases);
        b4.placerBateau(b4,cases);
        b5.placerBateau(b5,cases);
    }

    /**Fonction permettant d'obtenir un chiffre aléatoire. Il sera compris entre la valeur Min et la valeur Max, ou égal à l'une d'elles.
     *Les valeures générées seront utilisées comme coordonnées pour les bateaux
     */
    public static int rand(int Min, int Max) {
        return (int)(Math.random() * (double)(Max - Min + 1)) + Min;
    }

    /**Fonction qui permet de placer les bateaux sur le plateau de façon aléatoire. Les coordonnées du bateau et son orientation
     *seront obtenus en utilisant la fonction rand, puis, si l'espace que le bateau doit occupper est vide, il sera placé,
     *sinon d'autres coordonnées seront généré jusqu'à ce que l'on obtienne un emplacement vide
     */
    public void placerBateau(Bateau b, Case cases[][]) {
        int tb = b.taille;
        int ob = b.orientation;

        if (ob == 2) {
            int y;
            int x;

	    x = rand(0,(10-tb)); //coordonnee horizontal
            y = rand(0,9); //coordonnee vertical

	    if(casesVides(b, cases, x, y)==true){  //si case est vide
		for(int i = x ; i<(x+tb) ; i++)//initialise le bateau
		    cases[i][y].setState(true);}//true=bateau present
	    else {
		placerBateau(b, cases);
	    }
	}

	else if(ob==1){ //bateau vertical
	    int y;
            int x;

            x = rand(0,9); //coordonnee horizontal
            y = rand(0,(10-tb)); //coordonnee vertical

	     if(casesVides(b, cases, x, y)){
		 for(int i = y ; i< (y+tb) ; i++)//initialise le bateau
		     cases[x][i].setState(true);}//la case change d'etat
	     else{
		 placerBateau(b, cases);
	     }
	}
    }

    /**Fonction qui s'assure que le bateau soit placé dans des cases vides. Cette fonction vérifiera que les cases
     *qui doivent potentiellement accueuillir le bateau soient libres.
     *Elle renvera 'TRUE' si l'empacement est disponible, et 'FALSE' si il ne l'est pas
     */
    public boolean casesVides(Bateau b, Case c[][], int x, int y){

	int tb= b.taille;
	int j,z;

	if(b.orientation==1){//horizontal

	    //tester les voisins de la premiere cases
	    if(x > 0 && c[x-1][y].getState()==true) //haut
		return false;
	    if(x < 10-1 && c[x+1][y].getState()==true) // bas
		return false;
	    if(y > 0 && c[x][y-1].getState()==true) // droite
		return false;

	    for( j = y ; j<(y+tb) ; j++){

		if(j>=10)
		    return false;

		//voisin haut
		if(x-1 >= 0 && c[x-1][j].getState()==true)
		    return false;
		//voisin bas
                if (x+1 <= 10-1 && c[x+1][j].getState()==true)
		    return false;
		if (c[x][j].getState()==true)
		    return false;
	    }

	    //test de la derniere case
	    j--;
	    if(x > 0 && c[x-1][j].getState()==true) //haut
		return false;
	    if(x < 10-1 && c[x+1][j].getState()==true) // bas
		return false;
	    if(j < 10-1 && c[x][j+1].getState()==true) // droite
		return false;
	}
        else if(b.orientation==2){//vertical

	    //test de la premiere case
	    if(x-1 >= 0 && c[x-1][y].getState()==true) // haut
		return false;
	    if(y < 10-1 && c[x][y+1].getState()==true) // droite
		return false;
	    if(y > 0 && c[x][y-1].getState()==true) // gauche
		return false;


	    for( z = x ; z<(x+tb) ; z++){
		if(z>=10)
		    return false;

		//voisin gauche
		if(y-1 >= 0 && c[z][y-1].getState()==true)
		    return false;

		//voisin droite
                if (y+1 <= 10-1 && c[z][y+1].getState()==true)
		    return false;

                if (c[z][y].getState()==true)
		    return false;
	    }

	    //test de la derniere case
	    z--;
	    if(z+1 <= 10-1 && c[z+1][y].getState()==true) // bas
		return false;
	    if(y < 10-1 && c[x][y+1].getState()==true) // droite
		return false;
	    if(y > 0 && c[x][y-1].getState()==true) // gauche
		return false;
	}
        return true;
    }

}

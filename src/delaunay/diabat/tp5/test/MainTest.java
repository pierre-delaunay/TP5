package delaunay.diabat.tp5.test;

import java.sql.SQLException;

import delaunay.diabat.tp5.model.ChargerGrille;

public class MainTest {

	public static void main(String[] args) throws SQLException {

		/* Tests BDD */
		
		/*
        ChargerGrille s = new ChargerGrille();
        MotsCroisesTP5 mc = s.extraireGrille(ChargerGrille.CHOIX_GRILLE);
        System.out.println(mc.toString());
		*/
		
		/* Tests iterateurMots */
		
		/*
		Character[] tab = new Character[8];
		tab[0] = null;
		tab[1] = 'A';
		tab[2] = 'B';
		tab[3] = null;
		tab[4] = null;
		tab[5] = 'C';
		tab[6] = 'D';
		tab[7] = null;
		
		IterateurMots iter = new IterateurMots(tab);
		
		while(iter.hasNext())
		{
			System.out.println(iter.next() + ", ");
		}	
		*/
		
		/* Tests num aléa avec l'auto increment de la base */
		
		for (int i = 0; i < 10; ++i) {
			int r = (int) (Math.random() * ChargerGrille.MAX_NUM_GRILLE) + ChargerGrille.MIN_NUM_GRILLE;
			System.out.println(r);
		}
		
	}

}

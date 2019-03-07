package delaunay.diabat.tp5.model;

import java.sql.SQLException;

public class BddTest {

	public static void main(String[] args) throws SQLException {

		/*
        ChargerGrille s = new ChargerGrille();
        MotsCroisesTP5 mc = s.extraireGrille(ChargerGrille.CHOIX_GRILLE);
        System.out.println(mc.toString());
		*/

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
		
	}

}

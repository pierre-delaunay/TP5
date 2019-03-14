package delaunay.diabat.tp5.model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ChargerGrille {

    private Connection maConnexion;
    public static final int CHOIX_GRILLE = 10;
    private static final String TABLE_GRILLE = "TP5_GRILLE";
    private static final String TABLE_MOT = "TP5_MOT";
    public static final int MIN_NUM_GRILLE = 1;
    public static final int MAX_NUM_GRILLE = 11;

    public ChargerGrille() {
		try { maConnexion = connectionMySQL() ; }
		catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * Génération d'un numéro de grille aléatoire
     * @return int aléatoire (entre 1 et 11 ici)
     * Peut poser problème en cas de suppression de grilles
     */
    public int getNumGrilleAlea() {
    	return (int) (Math.random() * MAX_NUM_GRILLE) + MIN_NUM_GRILLE;
    }
    
    /**
     * Paramètres de connexion à la base
     * @return Connection
     * @throws SQLException
     */
    public static Connection connectionMySQL() throws SQLException {
    	// Setup Fac
        // String url = "jdbc:mysql://anteros.istic.univ-rennes1.fr/base_bousse";
    	
    	// Setup local
        String url = "jdbc:mysql://localhost/base_delaunay";
  
        try {
        	Class.forName ("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return DriverManager.getConnection(url, "root", "");
    }

    /**
     * Extraction de la grille depuis la base
     * @param Connection connect
     * @param int grille, numéro de la grille souhaitée
     * @return MotsCroisesTP5
     * @throws SQLException
     */
    public static MotsCroisesTP5 extraireBD(Connection connect, int grille) throws SQLException
    {
    	MotsCroisesTP5 mc = null;

        String sqlQuery = "SELECT * FROM " + TABLE_GRILLE + " WHERE num_grille = ?";
        PreparedStatement pstmt = connect.prepareStatement(sqlQuery);
        pstmt.setInt(1, grille);
        ResultSet setGrilles = pstmt.executeQuery();

        if (setGrilles.first()) {

        	int hauteur = setGrilles.getInt("hauteur");
        	int largeur = setGrilles.getInt("largeur");
        	mc = new MotsCroisesTP5(hauteur, largeur);

            sqlQuery = "SELECT * FROM " + TABLE_MOT + " WHERE num_grille = ?";
            pstmt = connect.prepareStatement(sqlQuery);
            pstmt.setInt(1, grille);
            ResultSet setMots = pstmt.executeQuery();

            while (setMots.next()) {
                int col = setMots.getInt("colonne");
                int lig = setMots.getInt("ligne");

                String solution = setMots.getString("solution");
                boolean horizontal = setMots.getBoolean("horizontal");
                mc.setDefinition(lig, col, horizontal, setMots.getString("definition"));

                if (horizontal) {
                    for(int i = 0; i < solution.length(); ++i){
                        mc.setSolution(lig, col + i, solution.toUpperCase().charAt(i));
                    }
                }
                else {
                    for(int i = 0; i < solution.length(); ++i){
                        mc.setSolution(lig + i, col, solution.toUpperCase().charAt(i));
                    }
                }
            }
        }

        //pstmt.close(); pstmt = null;
        //connect.close(); connect = null;
    	return mc;
    }


    /**
     * Récupération des grilles de la base pour les ajouter dans le menu
     * @return Map<Integer, String>, clé : clé primaire de la grille, valeur : nom de la grille
     */
    public Map<Integer, String> listeGrilles() {
    	
    	Map<Integer, String> map = new HashMap<>();	
    	String sqlQuery = "SELECT * FROM " + ChargerGrille.TABLE_GRILLE;
    	
        try {
            Statement stmt = maConnexion.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            
            while (rs.next()) {
                map.put(rs.getInt("num_grille"), rs.getString("nom_grille"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return map;
    }
    
    /**
     * Fournit une instance du modèle (MotsCroisesTP5)
     * à partir du numéro de grille
     * @param numChoix
     * @return MotsCroisesTP5
     * @throws SQLException
     */
    public MotsCroisesTP5 extraireGrille(int numChoix) throws SQLException {

    	return extraireBD(maConnexion, numChoix);
    }
}

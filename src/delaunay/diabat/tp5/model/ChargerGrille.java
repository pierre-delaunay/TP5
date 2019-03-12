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
    
    public int getNumGrilleAlea() {
    	return (int) (Math.random() * MAX_NUM_GRILLE) + MIN_NUM_GRILLE;
    }
    
    public static Connection connectionMySQL() throws SQLException {
        //String url = "jdbc:mysql://anteros.istic.univ�rennes1.fr/base_pdelaunay";
        String url = "jdbc:mysql://localhost/base_delaunay";
        // "user_18012555", "0900332hs"

        try {
        	Class.forName ("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return DriverManager.getConnection(url, "root", "");
    }

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
    
    public MotsCroisesTP5 extraireGrille(int numChoix) throws SQLException {

    	return extraireBD(maConnexion, numChoix);

    }
}

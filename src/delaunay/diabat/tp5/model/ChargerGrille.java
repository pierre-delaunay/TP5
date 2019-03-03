package delaunay.diabat.tp5.model;

import java.sql.*;

public class ChargerGrille {
	
    private Connection maConnexion;
    public static final int CHOIX_GRILLE = 10;
    private static final String TABLE_GRILLE = "TP5_GRILLE";
    private static final String TABLE_MOT = "TP5_MOT";
    
    public ChargerGrille() {
    	
		try { maConnexion = connectionMySQL() ; }
		catch (SQLException e) { e.printStackTrace(); }
		
    }
    
    public static Connection connectionMySQL() throws SQLException {
        String url = "jdbc:mysql://anteros.istic.univ­rennes1.fr/base_bousse";
        // String url = "jdbc:mysql://localhost/bd_tp5";
        
        try { 
        	Class.forName ("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return DriverManager.getConnection(url, "user_pdelaunay", "");
    }
    
    public static MotsCroisesTP5 extraireBD(Connection connect, int grille) throws SQLException
    {
    	MotsCroisesTP5 mc = null;
    	
        String sqlQuery = "SELECT * FROM " + TABLE_GRILLE + " WHERE num_grille = ?";
        PreparedStatement pstmt = connect.prepareStatement(sqlQuery);
        pstmt.setInt(1, grille);
        ResultSet setGrilles = pstmt.executeQuery();

        pstmt.close(); pstmt = null;
        
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
                        mc.setSolution(lig, col + i, solution.charAt(i));
                    }
                }
                else {
                    for(int i = 0; i < solution.length(); ++i){
                        mc.setSolution(lig + i, col, solution.charAt(i));
                    }
                }   
            }
        }
        
        pstmt.close(); pstmt = null;
        connect.close(); connect = null;
    	return mc;
    }
    
    
    public MotsCroisesTP5 extraireGrille(int numChoix) throws SQLException {
    	
    	return extraireBD(maConnexion, CHOIX_GRILLE);
    	
    }
}

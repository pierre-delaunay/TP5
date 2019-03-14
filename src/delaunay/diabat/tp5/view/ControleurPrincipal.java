package delaunay.diabat.tp5.view;

import java.util.Map;

import delaunay.diabat.tp5.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class ControleurPrincipal {

	private ChargerGrille loader;
	private ControleurTP5 ctrl;

    public void setControleur(ControleurTP5 ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    private BorderPane monBorderPane;
    
    @FXML
    private Menu liste;

    @FXML
    public MenuItem exit;

    @FXML
    private MenuItem aleatoire;

    @FXML
    public void initialize() {
    	
    	this.loader = new ChargerGrille();
    	
    	Map<Integer, String> map = this.loader.listeGrilles();
    	
        for (Integer cle : map.keySet()) {
            String nomGrille = map.get(cle);
            MenuItem item = new MenuItem(nomGrille);

            item.setOnAction( (e) -> { this.nouvelleGrille(cle); } );
            this.liste.getItems().add(item);
        }
    }

    /**
     * Génération d'une grille aléatoire
     */
    @FXML
    private void grilleAleatoire() {
    	
		this.nouvelleGrille(this.loader.getNumGrilleAlea());
    }

    /**
     * Nouvelle grille
     * @param numGrille, numéro de la grille voulue par le joueur
     */
    @FXML
    private void nouvelleGrille(int numGrille) {

        try {
            ctrl.setMotsCroises(this.loader.extraireGrille(numGrille));
            ctrl.initGP();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    /**
     * Fermeture de la fenêne courante
     * après un clique sur le MenuItem Quitter
     */
	@FXML
    private void exit() {
        System.exit(0);
    }

}

package delaunay.diabat.tp5.view;

import java.io.IOException;
import java.util.Map;

import delaunay.diabat.tp5.MainTP5;
import delaunay.diabat.tp5.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    @FXML
    private void grilleAleatoire() {

		this.nouvelleGrille(this.loader.getNumGrilleAlea());
    }

    @FXML
    private void nouvelleGrille(int numGrille) {

        try {
            ctrl.setMotsCroises(this.loader.extraireGrille(numGrille));
            ctrl.initDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

	@FXML
    private void exit() {
        System.exit(0);
    }

}

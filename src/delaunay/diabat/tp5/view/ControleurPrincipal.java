package delaunay.diabat.tp5.view;

import java.io.IOException;

import delaunay.diabat.tp5.MainTP5;
import delaunay.diabat.tp5.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ControleurPrincipal {

	private ChargerGrille loader;
	private ControleurTP5 ctrl;

    public void setControleur(ControleurTP5 ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    private Menu liste;

    @FXML
    public MenuItem exit;

    @FXML
    private MenuItem random;


    @FXML
    public void initialize() {
    	this.loader = new ChargerGrille();
    }

    @FXML
    private void random() {

    	// Entier aléa entre 1 et 11
		int r = (int) (Math.random() * 11) + 1;

		this.nouvelleGrille(r);

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

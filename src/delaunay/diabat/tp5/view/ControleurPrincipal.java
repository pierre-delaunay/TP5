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
    private MenuItem about;
    
    @FXML
    public void initialize() {

    }

    @FXML
    private void random() {
    
    }
    
    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void modalAbout() {

		try {
	    	FXMLLoader fxmlLoader = new FXMLLoader(MainTP5.class.getResource("view/VueAbout.fxml"));
	    	
	    	Parent root1;
			root1 = (Parent) fxmlLoader.load();
	    	Stage stage = new Stage();
            Image icone = new Image("file:images/istic.png");
            stage.getIcons().add(icone);
            stage.setTitle("À propos");
	    	stage.setScene(new Scene(root1));  
	    	stage.show();   	
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
}

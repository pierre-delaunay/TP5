package delaunay.diabat.tp5.view;

import delaunay.diabat.tp5.model.MotsCroisesTP5;
import delaunay.diabat.tp5.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ControleurTP5 {

    private MotsCroisesTP5 motsCroises;

    @FXML
    private GridPane monGridPane;

    @FXML
	private void initialize() {

    	motsCroises = MotsCroisesFactory.creerMotsCroises2x3();

    	for (Node n : monGridPane.getChildren())
    	{
    	   if (n instanceof TextField)
    	   {
		    	TextField tf = (TextField) n ;
		    	//int lig = ((int) n.getProperties().get("gridpane­row")) + 1;
		    	//int col = ((int) n.getProperties().get("gridpane­column")) + 1;
		    	int lig = monGridPane.getColumnIndex(n) + 1;
		    	int col = monGridPane.getRowIndex(n) + 1;


    	   }
    	}


	}
}

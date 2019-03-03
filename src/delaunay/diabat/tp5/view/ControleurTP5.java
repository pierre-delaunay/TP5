package delaunay.diabat.tp5.view;

import delaunay.diabat.tp5.model.ChargerGrille;
import delaunay.diabat.tp5.model.MotsCroisesTP5;
import delaunay.diabat.tp5.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ControleurTP5 {

    private MotsCroisesTP5 mc;

    @FXML
    private GridPane monGridPane;

    @FXML
	private void initialize() {

    	mc = MotsCroisesFactory.creerMotsCroises2x3();
    	
    	/*
        ChargerGrille loader = new ChargerGrille();
        
        try {
            this.mc = loader.extraireGrille(ChargerGrille.CHOIX_GRILLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        initDB();
        
        */
    	
    	initTF();
	}
  
    private void initDB() {
    	
        TextField modele = (TextField) this.monGridPane.getChildren().get(0);
        this.monGridPane.getChildren().clear();

        for (int lig = 1; lig <= this.mc.getHauteur(); ++lig) {
            for (int col = 1; col <= this.mc.getLargeur(); ++col) {
                if (!this.mc.estCaseNoire(lig, col)) {
                    TextField tf = new TextField();

                    tf.setPrefWidth(modele.getPrefWidth());
                    tf.setPrefHeight(modele.getPrefHeight());

                    for (Object cle : modele.getProperties().keySet()) {
                        tf.getProperties().put(cle, modele.getProperties().get(cle));
                    }

                    this.monGridPane.add(tf, col - 1, lig - 1);
                }
            }
        }
        
        initTF();
    }
    
    private void initTF() {
    	
    	for (Node n : monGridPane.getChildren())
    	{
    	   if (n instanceof TextField)
    	   {
		    	TextField tf = (TextField) n ;
		    	int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
		    	int col = ((int) n.getProperties().get("gridpane-column")) + 1;

		    	
		    	// Bindings
		    	
		    	tf.textProperty().bindBidirectional(mc.propositionProperty(lig, col));
		    	
		    	// Tooltips

                String defHoriz = this.mc.getDefinition(lig, col, true);
                String defVert = this.mc.getDefinition(lig, col, false);
                
                if (defHoriz != null && defVert != null) {
                    tf.setTooltip(new Tooltip(defHoriz + " / " + defVert));
                }
                else if (defHoriz != null) {
                    tf.setTooltip(new Tooltip(defHoriz));
                }
                else if (defVert != null) {
                	tf.setTooltip(new Tooltip(defVert));
                }
                    
                // Events
                tf.setOnMouseClicked((e) -> {
                    this.clicCase(e);
                });
    	   }
    	}	   	
    }
    
	@FXML
	public void clicCase(MouseEvent e) {
		
    	if (e.getButton() == MouseButton.MIDDLE)
    	{
	    	// C'est un clic "central"
	    	TextField cs = (TextField) e.getSource();
	    	int lig = GridPane.getRowIndex(cs);
            int col = GridPane.getColumnIndex(cs);
            this.mc.reveler(lig, col);
            
    	}
	}
	
}

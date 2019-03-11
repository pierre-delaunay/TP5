package delaunay.diabat.tp5.view;

import delaunay.diabat.tp5.model.ChargerGrille;
import delaunay.diabat.tp5.model.GrilleGen;
import delaunay.diabat.tp5.model.IterateurMots;
import delaunay.diabat.tp5.model.MotsCroisesTP5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import delaunay.diabat.tp5.*;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ControleurTP5 {

	public enum Direction {HORIZONTAL, VERTICAL}
	
    private MotsCroisesTP5 mc;
    private TextField tfCourant = null;
    private GrilleGen<TextField> grilleAux;
    private Direction directionCourante = Direction.HORIZONTAL;
    
    @FXML
    private GridPane monGridPane;

    @FXML
	private void initialize() {

        ChargerGrille loader = new ChargerGrille();
       
        try {
            this.mc = loader.extraireGrille(ChargerGrille.CHOIX_GRILLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initDB();
	}

    public void initDB() {

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

    public void initTF() {
    	
    	// Grille auxiliaire
    	this.grilleAux = new GrilleGen<>(this.mc.getHauteur(), this.mc.getLargeur());
    	
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

                // Listener : la case selectionnee devient la case courante
                tf.focusedProperty().addListener((obsValue, oldValue, newValue) -> {
                    if (newValue) {
                        tfCourant = tf;
                        tfCourant.getStyleClass().add("courant");
                    }

                    if (oldValue) { tf.getStyleClass().remove("courant"); }
                });

                // Listener : verification de la valeur dans la case avec une regex
                tf.textProperty().addListener((obsValue, oldValue, newValue) -> {
                	
                	String regex = "[A-Z]";
                	Pattern p = Pattern.compile(regex);
                	
                    if (newValue.matches(regex)) {
                    	System.out.println("match");
                    	agrandirCase();
                    	this.deplacerCase(tf, false);
                    } else {
                    	this.tfCourant.setText("");
                    }
                });

                // Listener : verification de la longueur
                tf.lengthProperty().addListener((obsValue, oldValue, newValue) -> {

                	if (tf.getText().length() >= 1 && newValue.intValue() > oldValue.intValue() ) {
                		// trim() supprime les espaces initiaux
                		tf.setText(tf.getText().trim().substring(0, 1));
                	}

                });

                // Events : revele la case avec un clic molette
                tf.setOnMouseClicked((e) -> {
                    this.clicCase(e);
                });

                // Events : frappe clavier sur la case courante
                tf.setOnKeyPressed((e) -> {
                    this.frappeClavier(e);
                });
                
                this.grilleAux.setValue(lig, col, tf);

    	   }
    	}
    }

	private void frappeClavier(KeyEvent e) {
		// TODO Auto-generated method stub
        KeyCode code = e.getCode();
        TextField tf = (TextField) e.getSource();

        switch (code) {
        	case BACK_SPACE :
                tfCourant.setText("");
                this.deplacerCase(tfCourant, true);
                break;
                
        	case UP :
                this.directionCourante = Direction.VERTICAL;
                this.deplacerCase(tf, true);
                break;

        	case DOWN :
                this.directionCourante = Direction.VERTICAL;
                this.deplacerCase(tf, false);
                break;

        	case LEFT :
                this.directionCourante = Direction.HORIZONTAL;
                this.deplacerCase(tf, true);
                break;

        	case RIGHT :
                this.directionCourante = Direction.HORIZONTAL;
                this.deplacerCase(tf, false);
                break;

        	case ENTER :
        		revelerSolution(tf);
        		break;
        		
        	default:
        		break;
        }
	}

	private void revelerSolution(TextField tf) {
		
		boolean horizontal = (this.directionCourante == Direction.HORIZONTAL);
		
	}

	private void deplacerCase(TextField tfSource, boolean reverse) {
		
    	int lig = ((int) tfSource.getProperties().get("gridpane-row")) + 1;
    	int col = ((int) tfSource.getProperties().get("gridpane-column")) + 1;
    	
    	if (this.directionCourante == Direction.HORIZONTAL) {
    		tfSource = this.grilleAux.getNextValue(lig, reverse? --col : ++col, true, reverse);
    	} else if (this.directionCourante == Direction.VERTICAL) {
    		tfSource = this.grilleAux.getNextValue(reverse? --lig : ++lig, col, false, reverse);
    	}
    	
    	tfSource.requestFocus();
	}


	private void agrandirCase() {
		// TODO Auto-generated method stub
		 ScaleTransition transition = new ScaleTransition(Duration.seconds(1), tfCourant);

		 transition.setFromX(0.2);
		 transition.setFromY(0.2);

		 transition.setToY(1);
		 transition.setToX(1);

		 transition.setAutoReverse(true);
		 transition.play();

	}

	@FXML
	public void clicCase(MouseEvent e) {

    	if (e.getButton() == MouseButton.MIDDLE)
    	{
	    	// C'est un clic "central"
	    	TextField tf = (TextField) e.getSource();
            int lig = ((int) tf.getProperties().get("gridpane-row")) + 1;
            int col = ((int) tf.getProperties().get("gridpane-column")) + 1;
            
            tf.getStyleClass().remove("erreur");
            tf.getStyleClass().remove("correct");
            this.mc.reveler(lig, col);

    	}
	}

	public void setMotsCroises(MotsCroisesTP5 mc) {
		// TODO Auto-generated method stub
		this.mc = mc;
	}

}

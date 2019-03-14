package delaunay.diabat.tp5.view;

import delaunay.diabat.tp5.model.ChargerGrille;
import delaunay.diabat.tp5.model.GrilleGen;
import delaunay.diabat.tp5.model.IterateurMots;
import delaunay.diabat.tp5.model.MotsCroisesTP5;

import java.util.regex.Pattern;

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
    
	public void setMotsCroises(MotsCroisesTP5 mc) {
		this.mc = mc;
	}
	
	/**
	 * Initialisation du contrôleur
	 * Récupération d'une grille aléatoire à partir de la base
	 */
    @FXML
	private void initialize() {

        ChargerGrille loader = new ChargerGrille();
       
        try {
            this.mc = loader.extraireGrille(loader.getNumGrilleAlea());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initGP();
	}
    
    /**
     * Initialisation du GridPane
     */
    public void initGP() {

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

    /**
     * Initialisation des TextField
     */
    public void initTF() {
    	
    	// Grille auxiliaire qui contiendra les pointeurs vers les textfields
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

                // Events : revele la case avec un clic molette/milieu
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

    /**
     * Gestion des différentes frappes claviers
     * @param KeyEvent e, touches directionnelles/Entrée/Effacer
     */
	private void frappeClavier(KeyEvent e) {
        KeyCode code = e.getCode();
        TextField tf = (TextField) e.getSource();

        switch (code) {
        	case BACK_SPACE :
                tfCourant.setText("");
                // reverse vaut true ici : déplacement en arrière
                this.deplacerCase(tfCourant, true);
                break;
                
        	case UP :
                this.directionCourante = Direction.VERTICAL;
                // reverse vaut true ici : déplacement inverse au sens de lecture vertical
                this.deplacerCase(tf, true);
                break;

        	case DOWN :
                this.directionCourante = Direction.VERTICAL;
                this.deplacerCase(tf, false);
                break;

        	case LEFT :
                this.directionCourante = Direction.HORIZONTAL;
                // reverse vaut true ici : déplacement inverse au sens de lecture horizontal
                this.deplacerCase(tf, true);
                break;

        	case RIGHT :
                this.directionCourante = Direction.HORIZONTAL;
                this.deplacerCase(tf, false);
                break;

        	case ENTER :
        		verifierSolution(tf);
        		break;
        		
        	default:
        		break;
        }
	}

	/**
	 * Traitement de la touche Entrée
	 * Colorie en vert les cases dans lesquelles la lettre proposée coïncide avec la solution
	 * Colorie en rouge les propositions erronées
	 * @param TextField tf
	 */
	private void verifierSolution(TextField tf) {
		
		int num, length;
		
		boolean horizontal = (this.directionCourante == Direction.HORIZONTAL);
		
		if (horizontal) {
			num = ((int) tf.getProperties().get("gridpane-row")) + 1;
			length = this.mc.getLargeur();
		} else {
			num = ((int) tf.getProperties().get("gridpane-column")) + 1;
			length = this.mc.getHauteur();
		}
		
		IterateurMots it = this.grilleAux.iterateurMots(horizontal, num);
		
		int i = 1;
		
        while (it.hasNext() && i <= length) {
        	
            TextField current = (TextField) it.next();
            
            if (!this.mc.estCaseNoire(horizontal ? num : i, horizontal ? i : num) &&
                current.getText().equals(this.mc.getSolution(horizontal ? num : i, horizontal ? i : num) + "")) {
            	current.getStyleClass().add("correct");
            } else {
                current.getStyleClass().add("erreur");
            }

            ++i;
        }	
	}

	/**
	 * Déplacements entre les cases
	 * @param tfSource, case source
	 * @param reverse, true dans le cas d'un déplacement dans le sens inverse, false sinon
	 */
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


	/**
	 * Agrandissement de la case courante suite à une frappe
	 * La lettre frappée apparaît en s'agrandissant
	 */
	private void agrandirCase() {
		 ScaleTransition transition = new ScaleTransition(Duration.seconds(1), tfCourant);

		 transition.setFromX(0.2);
		 transition.setFromY(0.2);

		 transition.setToY(1);
		 transition.setToX(1);

		 transition.play();
	}

	/**
	 * Traitement de la frappe sur la molette
	 * @param MouseEvent e
	 */
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

}

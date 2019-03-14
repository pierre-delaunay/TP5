package delaunay.diabat.tp5.model;

/**
 * PRGA - TP2, classe modifiée pour le TP5
 * @author Pierre D. , Alexis D.
 * @since 2019-03-07
 * @version 2.0.1
 */
public class GrilleGen <T> {

	private int lig;
	private int col; 
	private T tab[][] ; 
	
	public GrilleGen(int lig, int col) {
		 this.lig= lig; 
		 this.col = col; 
		 this.tab = (T[][]) new Object [lig][col]; 	
	 }
	
	public T[][] getTab () {
		return this.tab;
	}
	
	public int getLig() {
		return this.lig;
	}
	
	public int getCol() {
		return this.col;
	}
	
    public T getValue(int lig, int col) {
    	assert coordCorrectes(lig, col) : "Coordonnees incorrectes";
        return this.tab[lig-1][col-1];
    }
    
    public void setValue(int lig, int col, T value) {
    	assert coordCorrectes(lig, col) : "Coordonnees incorrectes";
    	this.tab[lig-1][col-1] = value;   	
    }
    
	public boolean coordCorrectes(int lig, int col) {
		return (lig <= this.getLig() && col <= this.getCol() && lig >= 1 && col >= 1);
	}
	
	/**
	 * Fournit la prochaine valeur non nulle
	 * @param int lig, indice de ligne
	 * @param int col, indice de colonne
	 * @param boolean horizontal, true horizontal, false vertical
	 * @param boolean reverse, true dans le sens contraire, false sinon
	 * @return T
	 */
	public T getNextValue(int lig, int col, boolean horizontal, boolean reverse) {
		assert coordCorrectes(lig, col) : "Coordonnees incorrectes";
		
		T v = this.getValue(lig, col);
		
		if (horizontal) {
			while (col <= this.getCol() && v == null) {
				if (reverse) {
					--col;
				} else {
					++col;
				}
				v = this.getValue(lig, col);
			}
		} else {
			while (lig <= this.getLig() && v == null) {
				if (reverse) {
					--lig;
				} else {
					++lig;
				}
				v = this.getValue(lig, col);
			}			
		}
		return v;
	}
	
	/**
	 * Fournit un itérateur (iterateurMots) pour la ligne ou colonne choisie
	 * @param boolean horizontal
	 * @param int num
	 * @return IterateurMots
	 */
	public IterateurMots iterateurMots (boolean horizontal, int num) {
		
		 if(horizontal) { 
			 return new IterateurMots(this.tab[num-1]);
		 }
		 else 
		 { 	 
			 Object[] tab = (T[]) new Object[this.getLig()];
			 
	         for (int i = 0; i < this.getLig(); ++i) {
	        	 tab[i] = this.getValue(i + 1, num);
	         }
	       
			 return new IterateurMots(tab);
		 }
	}
}

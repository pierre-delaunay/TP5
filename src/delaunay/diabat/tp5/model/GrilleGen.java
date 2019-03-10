package delaunay.diabat.tp5.model;

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
	
	public IterateurMots iterateurMots (boolean horizontal, int num) {
		
		 if(horizontal) { 
			 IterateurMots itCol = new IterateurMots(tab[num]);
			 return itCol;
		 }
		 else 
		 { 	 
			 IterateurMots itLig = new IterateurMots(tab);
			 return itLig;
		 }	
	}
	
}

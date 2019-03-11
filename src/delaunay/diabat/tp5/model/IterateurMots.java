package delaunay.diabat.tp5.model;

import java.util.Iterator;

/**
 * PRGA - TP2
 * @author Pierre D. , Alexis D.
 * @since 2019-01-21
 * @version 1.0.1
 *
 */

public class IterateurMots implements Iterator<Object> {

	private Object[] tab;
	private int curseur;
	
	public IterateurMots(Object[] tab) {	
		this.tab = tab.clone();
		this.curseur = 0;
		this.setCurseur();
	}
	
    private void setCurseur() {
        while (this.curseur < this.tab.length && this.tab[this.curseur] == null) {
            this.curseur++;
        }
    }
    
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasNext() {
		return (this.curseur < tab.length);
	}

	@Override
	public Object next() {
		assert this.hasNext() : "L'itérateur n'est pas sur un objet";
		
	    Object next = (Object) this.tab[this.curseur];
	    ++this.curseur;
	    this.setCurseur();

	    return next;
	}

}
package org.rebelo.demoSB.entidade.Enum;

public enum Marca {

	AUDI("AUDI"),
	BMW("BMW"), 
	MERCEDES("MERCEDES"), 
	MINI("MINI"), 
	PORSHE("PORSHE"), 
	SMART("SMART"),
	VOLKSWAGGEN("VOLKSWAGGEN"),
	OUTROS("OUTROS");

	 private String marca;

	    private Marca(String marca) {
	        this.marca = marca;
	    }

	    public String getMarca() {
	        return marca;
	    }
	
}

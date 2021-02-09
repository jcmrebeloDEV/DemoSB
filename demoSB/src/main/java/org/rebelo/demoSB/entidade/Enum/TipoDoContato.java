package org.rebelo.demoSB.entidade.Enum;

public enum TipoDoContato {
	
	TELEFONECELULAR("TELEFONECELULAR"), 
	TELEFONEFIXO("TELEFONEFIXO"), 
	EMAIL("EMAIL"), 
	WHATSAPP("WHATSAPP"), 
	FACEBOOK("FACEBOOK"), 
	OUTROS("OUTROS");

	 private String code;

	    private TipoDoContato(String code) {
	        this.code = code;
	    }

	    public String getCode() {
	        return code;
	    }
}


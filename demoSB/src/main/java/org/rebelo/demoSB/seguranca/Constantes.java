package org.rebelo.demoSB.seguranca;

public class Constantes {

	    public static final String SECRET = "SecretKeyToGenJWTs";
	    public static final String TOKEN_PREFIX_AUTORIDADES = "AUTORIDADES";
	    public static final long EXPIRATION_TIME = 864_000_000; // 10 dias
	    public static final String TOKEN_PREFIX = "Bearer ";
	    public static final String HEADER_STRING = "Authorization";
	    public static final String CADASTRO_API = "/usuarios/criar/";
	    public static final String LOGIN_API = "/usuarios/login/";
}

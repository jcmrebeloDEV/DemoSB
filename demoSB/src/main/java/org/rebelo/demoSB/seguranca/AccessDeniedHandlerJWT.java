package org.rebelo.demoSB.seguranca;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AccessDeniedHandlerJWT implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AccessDeniedException e) throws IOException, ServletException {

		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

		final Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", HttpServletResponse.SC_FORBIDDEN);
		body.put("message", "Token inválido");
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("path", httpServletRequest.getServletPath());
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(httpServletResponse.getOutputStream(), body);
	}

}
package it.corso;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import it.corso.jwt.JWTTokenNeededFilter;
import jakarta.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig{
	
	public JerseyConfig() {
		//Si deve registrare la classe del filtro su jersey posizionata prima del package
		register(JWTTokenNeededFilter.class);
		packages("it.corso");
		
	}

}

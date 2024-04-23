package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@JWTTokenNeeded
@Provider
public class JWTTokenNeededFilter implements ContainerRequestFilter{

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Secured annotationRole = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
		if(annotationRole == null ) {
			annotationRole =  resourceInfo.getResourceClass().getAnnotation(Secured.class);


		}
		String authorizationHeander = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if(authorizationHeander == null || !authorizationHeander.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		// estrae il token della HTTP Authorization header
		String token = authorizationHeander.substring("Bearer".length()).trim();

		try {
			//proviamo ad estrarre il token con la stessa chiave sereta usata nel controller
			byte [] secretKey = "helloooooooookk1234565656iuuuuiiiiiiiiiii7".getBytes();
			// critografia
			Key key = Keys.hmacShaKeyFor(secretKey);
			//eseguiamo il procedimento al contrario per farci tornare il token
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			Claims body = claims.getBody();
			List<String> rolesToken = body.get("ruoli", List.class);
			Boolean hasRole = false;

			//facciamo un controllo per verificare se il ruolo dell'annotation é contenuto dentro 
			for (String role : rolesToken) {

				if(role.equals(annotationRole.role())) {

					hasRole = true;
				}
			}
			if(!hasRole) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}

}

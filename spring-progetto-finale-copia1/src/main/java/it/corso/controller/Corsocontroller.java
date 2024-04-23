package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CorsoDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.service.CorsoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Secured(role = "Admin")
@JWTTokenNeeded
@Path("/corsi")
public class Corsocontroller {

	@Autowired
	private CorsoService corsoService;

	/*
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourseById(@PathParam("id") int id) {

	CorsoDto corso = corsoService.getCourseById(id);
	if (corso != null) {

	return Response.status(Response.Status.OK).entity(corso).build();
	}

	return Response.status(Response.Status.BAD_REQUEST).build();
	}
	 */

	@GET
	@Path("/listacorsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourses() {
		
		// su postman fare prima il POST login di utente, prendere il token generato e inserirlo 
		//nel GET di questo metodo nella sezione AUTHORIZATION ->  scengliere Bearer token -> token

		try {

			List<CorsoDto> listaCorsi = corsoService.getCourses();
			return Response.status(Response.Status.OK).entity(listaCorsi).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).entity("Errore caricamento utenti").build();
		}
	}

}

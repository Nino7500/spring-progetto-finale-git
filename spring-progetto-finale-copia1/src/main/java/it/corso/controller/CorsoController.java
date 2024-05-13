package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CorsoDto;
import it.corso.service.CorsoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/corsi")
public class CorsoController {

	@Autowired
	private CorsoService corsoService;

	
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
	@POST
	@Path("/inserisci")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response inserisciCorso(CorsoDto corsoDto) {
		corsoService.inserisciCorso(corsoDto);
		return Response.status(Response.Status.CREATED).build();
	}

	@PUT
	@Path("/modifica/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modificaCorso(@PathParam("id") int id, CorsoDto corsoDto) {
		corsoService.modificaCorso(id, corsoDto);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("/elimina/{id}")
	public Response eliminaCorso(@PathParam("id") int id) {
		corsoService.eliminaCorso(id);
		return Response.status(Response.Status.OK).build();
	}

}

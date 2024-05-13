package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Secured(role = "Admin")
@JWTTokenNeeded
@Path("/categorie")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@GET
	@Path("/listacategorie")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllCategorie() {
		try {

			List<CategoriaDto> listaCategorie = categoriaService.getAllCategorie();
			return Response.status(Response.Status.OK).entity(listaCategorie).build();

		} catch (Exception e) {

			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CategoriaDto> getAllWithFilter(@QueryParam("filterName") String filterName) {
		return categoriaService.getAllWithFilter(filterName);
	}

	@POST
	@Path("/inserisci")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertCategoria(@RequestBody CategoriaDto categoriaDto) {
		try {
			categoriaService.insertCategoria(categoriaDto);

			return Response.status(Response.Status.OK).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoriaById(@PathParam("id") int id) {
		try {
			CategoriaDto categoriaDto = categoriaService.getCategoriaById(id);
			return Response.status(Response.Status.OK).entity(categoriaDto).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Categoria non trovata").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante la ricerca della categoria").build();
		}
	}

	@DELETE
	@Path("/elimina/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCategoryById(@PathParam("id") String id) {
		try {
			this.categoriaService.deleteCategoryById(Integer.valueOf(id));
			return Response.status(Response.Status.OK) .build();
		} catch (Exception e) {
			return Response. status (Response. Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCategory(@PathParam("id") int id, @Valid CategoriaDto categoriaDto) {
		try {
			categoriaService.updateCategoria(id, categoriaDto);
			return Response.status(Response.Status.OK).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}

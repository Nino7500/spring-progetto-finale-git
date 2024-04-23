package it.corso.controller;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import it.corso.service.UtenteService;
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

@Path("/utenti")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

	@POST
	@Path("/registrazione")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registraUtente(@Valid @RequestBody UtenteRegistrazioneDto utenteDto) {
		try {
			if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", utenteDto.getPassword())) {

				return Response.status(Response.Status.BAD_REQUEST).build();

			} if(utenteService.existsUserByEmail(utenteDto.getEmail())) {

				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			utenteService.registraUtente(utenteDto);

			return Response.status(Response.Status.OK).build();

		}catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/aggiorna")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@RequestBody UtenteAggiornamentoDto utente) {

		try {

			utenteService.aggiornaUtente(utente);

			return Response.status(Response.Status.OK).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("delete/{email}")
	public Response deleteUser(@PathParam("email") String email) {

		try {
			utenteService.eliminaUtente(email);

			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}


	@GET
	@Path("/visualizzautente")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selzionaUtenteDaEmail(@QueryParam("email") String email) {
		try {
			if (email != null && !email.isEmpty()) {

				UtenteDto utenteDto = utenteService.getUserByEmail(email);
				if (utenteDto != null) {
					return Response.ok().entity(utenteDto).build();
				}
			}

			return Response.status(Response.Status.BAD_REQUEST).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/listautenti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaUtenti() {
		try {

			return Response.status(Response.Status.OK).entity(utenteService.listaUtenti()).build();

		}catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUtente(@RequestBody UtenteLoginRequestDto utenteLoginRequestDto) {
		try {

			if(utenteService.loginUtente(utenteLoginRequestDto)) {
				return Response.ok(issueToken(utenteLoginRequestDto.getEmail())).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();

		}catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private UtenteLoginResponseDto issueToken(String email) {

		// eseguiamo una cifratura attraverso l'algoritmo di crittografia HMAC 
		byte [] secretKey = "helloooooooookk1234565656iuuuuiiiiiiiiiii7".getBytes();
		// critografia
		Key key = Keys.hmacShaKeyFor(secretKey);

		Utente infoUtente = utenteService.findByEmail(email);
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nome", infoUtente.getNome());
		map.put("cognome", infoUtente.getCognome());
		List<String> ruoli = new ArrayList<>();

		for(Ruolo ruolo: infoUtente.getRuoli()) {
			ruoli.add(ruolo.getTipologia().name());
		}
		map.put("ruoli", ruoli);

		//Data di creazione del token
		Date creation = new Date();
		// 									passo la data attuale aggiungendo 15 minuti
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		//Creazione del token firmaqto con la chiave segreta
		String jwtToken = Jwts.builder()
				.setClaims(map)
				.setIssuer("http://8080")
				.setIssuedAt(creation)
				.setExpiration(end)
				.signWith(key)
				.compact();

		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);
		token.setTokenCreationTime(creation);
		token.setTtl(end);

		return token;
	}

}
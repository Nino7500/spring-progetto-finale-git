package it.corso.service;


import java.util.List;

import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Utente;

public interface UtenteService {
	
    void registraUtente(UtenteRegistrazioneDto utenteDto);
    
    boolean existsUserByEmail(String email);

	void aggiornaUtente(UtenteAggiornamentoDto udateUtenteDto);
	
	void eliminaUtente(String email);
	
	UtenteDto getUserByEmail(String email);
	
	Utente findByEmail(String email);
	
	List<UtenteDto> listaUtenti();
	
	boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto);
}
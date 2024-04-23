package it.corso.service;

import java.util.List;

import it.corso.dto.CorsoDto;

public interface CorsoService {

	void inserisciCorso(CorsoDto corso);
	
	boolean existsByNome(String nome);

	List<CorsoDto> getCourses();
	
	
}

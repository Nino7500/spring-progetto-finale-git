package it.corso.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dto.CorsoDto;
import it.corso.model.Corso;

@Service
public class CorsoSeriveImpl implements CorsoService {
	
	private ModelMapper mapper = new ModelMapper();
	
	@Autowired
	private CorsoDao corsoDao;

	@Override
	public void inserisciCorso(CorsoDto corsoDto) {
		Corso corso = new Corso();
		corso.setNomeCorso(corsoDto.getNomeCorso());
		corso.setDescrizioneBreve(corsoDto.getDescrizioneBreve());
		corso.setDescrizioneCompleta(corsoDto.getDescrizioneCompleta());
		corso.setDurata(corsoDto.getDurata());
		corsoDao.save(corso);
	}

	@Override
	public boolean existsByNome(String nome) {
		
		return corsoDao.existsByNomeCorso(nome);
	}
	@Override
	public List<CorsoDto> getCourses() {

	List<Corso> corso = (List<Corso>) corsoDao.findAll();
	List<CorsoDto> corsoDto = new ArrayList<>();
	corso.forEach(c -> corsoDto.add(mapper.map(c, CorsoDto.class)));

	return corsoDto;
	}

}

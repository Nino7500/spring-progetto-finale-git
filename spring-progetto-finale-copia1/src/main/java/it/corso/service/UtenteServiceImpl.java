package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private UtenteDao utenteDao;

	@Autowired
	private RuoloDao ruoloDao;

	@Override
	public void registraUtente(UtenteRegistrazioneDto utenteDto) {
		Utente utente = new Utente();
		String sha256hex = DigestUtils.sha256Hex(utenteDto.getPassword());
		utente.setNome(utenteDto.getNome());
		utente.setCognome(utenteDto.getCognome());
		utente.setEmail(utenteDto.getEmail());
		utente.setPassword(sha256hex);

		utenteDao.save(utente);
	}

	@Override
	public void aggiornaUtente(UtenteAggiornamentoDto utente) {
		try {
			Utente utenteDb = utenteDao.findByEmail(utente.getEmail());
			if(utenteDb != null) {

				utenteDb.setNome(utente.getNome()) ;
				utenteDb.setCognome(utente.getCognome()) ;
				utenteDb.setEmail(utente.getEmail());

				List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloDb = ruoloDao.findById(utente.getIdRuolo());

				if (ruoloDb.isPresent()) {

					Ruolo ruolo = ruoloDb.get();
					ruolo.setId(utente.getIdRuolo());

					ruoliUtente.add(ruolo);
					utenteDb.setRuoli(ruoliUtente);
				}
				utenteDao.save(utenteDb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean existsUserByEmail(String email) {

		return utenteDao.existsByEmail(email);
	}

	@Override
	public void eliminaUtente(String email) {

		Utente utente = utenteDao.findByEmail(email);

		int id = utente.getId();
		Optional<Utente> userOptional = utenteDao.findById(id);
		if (userOptional.isPresent()) {

			utenteDao.delete(userOptional.get());
		}
	}

	@Override
	public UtenteDto getUserByEmail(String email) {
		Utente utente = utenteDao.findByEmail(email);
		UtenteDto utenteDto = modelMapper.map(utente, UtenteDto.class);
		return utenteDto;
	}

	@Override
	public Utente findByEmail(String email) {

		return utenteDao.findByEmail(email);
	}

	@Override
	public List<UtenteDto> listaUtenti() {
		List<Utente> utenti = (List<Utente>) utenteDao.findAll();
		List<UtenteDto> utentiDto = new ArrayList<>();

		utenti.forEach(u -> utentiDto.add(modelMapper.map(u, UtenteDto.class)));

		return utentiDto;
	}

	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto) {
		Utente utente = new Utente();

		utente.setEmail(utenteLoginRequestDto.getEmail());
		//Fingiamo che la password sia"ciao" :
		//getPassword di utenteLoginRequestDto mirecupera questa password e la setto su utente tramite itmetodo setPassword
		//setpassord
		utente.setPassword(utenteLoginRequestDto.getPassword());

		System.out.println(utente.getPassword());
		//tramite il getPassword di utente recupero la password e la passo al metodo che me la hasha
		String passwordHash = DigestUtils.sha256Hex(utente.getPassword());

		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), passwordHash);

		// operatore ternario dato dalla condizione ? espressione A se é vero : espressione B se é falso
		return credenzialiUtente != null ? true : false;
	}

}
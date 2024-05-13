package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	private ModelMapper mapper = new ModelMapper();

	@Autowired
	private CategoriaDao categoriaDao;


	@Override
	public List<CategoriaDto> getAllCategorie() {
		List<Categoria> categorie = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> categorieDto = new ArrayList<>();
		for (Categoria categoria : categorie) {
			CategoriaDto categoriaDto = mapper.map(categoria, CategoriaDto.class);
			categorieDto.add(categoriaDto);
		}
		return categorieDto;
	}
	@Override
	public void insertCategoria(CategoriaDto categoriaDto) {

		if (!categoriaDao.existsByNomeCategoria(categoriaDto.getNomeCategoria())) {

			Categoria categoria = mapper.map(categoriaDto, Categoria.class);
			categoriaDao.save(categoria);
		} else {
			throw new IllegalArgumentException("Categoria già esistente");
		}
	}

	@Override
	public CategoriaDto getCategoriaById(int id) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			Categoria categoria = categoriaOptional.get();
			return mapper.map(categoria, CategoriaDto.class);
		} else {
			throw new IllegalArgumentException("Categoria non trovata");
		}
	}

	@Override
	public void deleteCategoryById(int id) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			categoriaDao.deleteById(id);
		} else {
			throw new IllegalArgumentException("Categoria non trovata");
		}

	}
	@Override
	public void updateCategoria(int id, CategoriaDto categoriaDto) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			Categoria categoria = categoriaOptional.get();

			categoria.setNomeCategoria(categoriaDto.getNomeCategoria());

			categoriaDao.save(categoria);
		} else {

			throw new IllegalArgumentException("Categoria non trovata");
		}
	}

	@Override
	public List<CategoriaDto> getAllWithFilter(String filterName) {
		List<Categoria> listaCategoria = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> listaCategoriaDto = new ArrayList<>();
		listaCategoria.forEach(c -> {if (c.getNomeCategoria().name().equals(filterName)) {
				listaCategoriaDto.add(mapper.map(c, CategoriaDto.class));
			}
		});
		return listaCategoriaDto;
	}

}


package it.corso.service;

import java.util.List;

import it.corso.dto.CategoriaDto;

public interface CategoriaService {

	void insertCategoria(CategoriaDto categoriaDto);
	
    List<CategoriaDto> getAllCategorie();

	CategoriaDto getCategoriaById(int id);
	
    void updateCategoria(int id, CategoriaDto categoriaDto);

	void deleteCategoryById(int id);

	List<CategoriaDto> getAllWithFilter(String filterName);
}

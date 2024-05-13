package it.corso.dto;

import java.util.List;

import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public class CategoriaDto {

	private NomeCategoria nomeCategoria;
	
	private List<Categoria> categorie;
	
	public NomeCategoria getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(NomeCategoria nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public List<Categoria> getCategorie() {
		return categorie;
	}
	public void setCategorie(List<Categoria> categorie) {
		this.categorie = categorie;
	}
}
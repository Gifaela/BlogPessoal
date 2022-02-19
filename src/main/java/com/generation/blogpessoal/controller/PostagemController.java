package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") // origin aceita requisição - allowedHeaders enviar título(tolk)
public class PostagemController {



	@Autowired 	// ingeção de dependencia - string instancia altimaticamente
	private PostagemRepository postagemRepository;

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {
		return ResponseEntity.ok(postagemRepository.findAll()); //select * from tb_postagens;
	}
	
	//labida com mapping / Opitional
	@GetMapping("/{id}")//produrando o ID 
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@GetMapping ("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo (@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); //select * from tb_postagens;
	}                                               
	
	@PostMapping
	public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED) //Status devolva 201(CREATED)
				.body(postagemRepository.save(postagem));
	}
	
	/*@PutMapping("/{id}")
	public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK) //Status devolva 201(CREATED)
				.body(postagemRepository.save(postagem));*/
		
		@PutMapping // desafio 1
	    public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem, @PathVariable Long id) {
	        //return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
	        return postagemRepository.findById(postagem.getId()) //procura pelo id 
	                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem))) //realiza se resposta n for nulla
	                .orElse(ResponseEntity.notFound().build()); //realiza se a resposta for nulla
	    
		
	}
	
	/*@DeleteMapping("/{id}") 
	public void deletePostagem(@PathVariable Long id) {
		postagemRepository.deleteById(id);}
		
		*/
		/*@DeleteMapping("/{id}") // desafio 2
		@ResponseStatus(value = HttpStatus.NO_CONTENT)
		public void deletePostagem(@PathVariable Long id) {
			Optional <Postagem> postagem = postagemRepository.findById(id);
			
			if (postagem.isEmpty()) //quando a postagem for vazia 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
			postagemRepository.deleteById(id);}*/
		
		//ou 
		
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deletePostagem(@PathVariable Long id) {
			
			return postagemRepository.findById(id)
					.map(resposta -> {
						postagemRepository.deleteById(id);
						return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
					})
					.orElse(ResponseEntity.notFound().build());
		}

	
}
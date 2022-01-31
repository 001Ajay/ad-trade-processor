package org.dev.ad.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dev.ad.exception.MadException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiParam;

public abstract class CrudController<T, ID> {
	
	protected abstract JpaRepository<T, ID> getRepo();
	
	@GetMapping("/all")
	public List<T> findAll() {
		return getRepo().findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<T> findById(@ApiParam(value = "Id from which object will be retrieved", required = true) 
		@PathVariable(value = "id") ID id) throws MadException {
		
		T resource = getRepo().findById(id)
				.orElseThrow(() -> new MadException("Resource not found for this id :: " + id));
		return ResponseEntity.ok().body(resource);
	}
	
	@PostMapping("/add")
	public T add(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestBody T employee) {
		return getRepo().save(employee);
	}
	
	@PutMapping("/update")
	public ResponseEntity<T> update(@ApiParam(value = "Add object in database table", required = true) 
		@Valid @RequestBody T employee) {
		T updatedResource = getRepo().save(employee);
		return ResponseEntity.ok().body(updatedResource);
	}
	
	@DeleteMapping("/delete/{id}")
	public Map<String, Boolean> delete(@ApiParam(value = "Id from which object will be delete from database table", required = true) 
		@PathVariable(value = "id") ID id) throws MadException {
		T resource = getRepo().findById(id)
				.orElseThrow(() -> new MadException("Resource not found for this id :: " + id));
		getRepo().delete(resource);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}

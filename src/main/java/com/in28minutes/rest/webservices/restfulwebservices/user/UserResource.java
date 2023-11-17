package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {
	
	public UserDaoService userDaoService;
	
	public UserResource(UserDaoService userDaoService) {
		super();
		this.userDaoService = userDaoService; 
	}
	
	@GetMapping("/users")
	public List<User> retrieveAllUser()
	{
		return userDaoService.findAll();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveAllUser(@PathVariable Integer id)
	{	
		User user = userDaoService.findOne(id);
		
		if(user==null) {
			throw new UserNotFoundException("id : " + id);
		}
		
		EntityModel<User> entityModel = EntityModel.of(user); 
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUser(id));
		return entityModel.add(link.withRel("all-users"));
		
	}
	

	@DeleteMapping("/users/{id}")
	public void deleteById(@PathVariable Integer id)
	{
		userDaoService.deleteOne(id);
	}
	

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user)
	{
		User savedUser = userDaoService.save(user);
		URI Location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.path("/{id}")
					.buildAndExpand(savedUser.getId())
					.toUri();
		return ResponseEntity.created(Location).build();
	}
	
}

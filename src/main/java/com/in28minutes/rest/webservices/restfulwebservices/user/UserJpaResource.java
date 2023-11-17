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

import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaResource {
	
	public UserRepository userRepository;
	public PostRepository postRepository;
	
	public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
		super();
		this.userRepository = userRepository; 
		this.postRepository = postRepository;
	}
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUser()
	{
		return userRepository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveAllUser(@PathVariable Integer id)
	{	
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id : " + id);
		}
		
		EntityModel<User> entityModel = EntityModel.of(user.get()); 
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUser(id));
		return entityModel.add(link.withRel("all-users"));
		
	}
	

	@DeleteMapping("/jpa/users/{id}")
	public void deleteById(@PathVariable Integer id)
	{
		userRepository.deleteById(id);
	}
	
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user)
	{
		User savedUser = userRepository.save(user);
		URI Location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.path("/{id}")
					.buildAndExpand(savedUser.getId())
					.toUri();
		return ResponseEntity.created(Location).build();
	}
	
	//----------------------------POST USER---------------------------------------------------------------------	
	
	
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostForUser(@PathVariable Integer id)
	{
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id : " + id);
		}
		return user.get().getPosts();
		
	}
	
	@DeleteMapping("/jpa/users/{id}/posts")
	public void deleteByPostId(@PathVariable Integer id)
	{
		postRepository.deleteById(id);
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post)
	{
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id : " + id);
		}
		post.setUser(user.get());
		Post savedUser = postRepository.save(post);
		URI Location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.path("/{id}")
					.buildAndExpand(savedUser.getId())
					.toUri();
		return ResponseEntity.created(Location).build();
	}

}
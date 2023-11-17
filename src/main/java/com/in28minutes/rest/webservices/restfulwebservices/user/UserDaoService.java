package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Controller;

@Controller
public class UserDaoService {

	private static int count = 0;
	static List<User> users = new ArrayList<>();
	static {
		users.add(new User(++count, "Vidya", LocalDate.of(1998,Month.JANUARY, 25)));
		users.add(new User(++count, "Avinash", LocalDate.of(1998,Month.AUGUST, 25)));
		users.add(new User(++count, "Subhashree", LocalDate.of(1998,Month.JANUARY, 27)));
		
	}
	
	
	public List<User> findAll(){
		return users;
	}
	
	public User findOne(Integer id){
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
		
	}

	public void deleteOne(Integer id){
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
	
	}

	
	public User save(User user) {
		user.setId(++count);
		users.add(user);	
		return user;
	}
}

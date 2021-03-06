
package com.revature.controllers;


import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.dao.userDao;
import com.revature.dao.userDaoImpl;
import com.revature.models.Playlists;
import com.revature.models.User;

import exceptions.UserNotFoundExeption;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/users")
public class UserController {


	private Logger log = Logger.getLogger(UserController.class);

	@Autowired
	userDao ud = new userDaoImpl();

	@GetMapping
	public List<User> getUsers(){
		log.info("Getting All Users");
		return ud.getAllUsers();
	}

	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public User getUserById(@PathVariable("id")Integer id) {
		log.info("Searching For User With Id "+id);
		User u = ud.getUserById(id);
		log.info("User With Id "+id+" Was Found");
		log.info("User "+id+" info: "+u);
		if(u==null) {
			log.info("User With Id "+id+" Was NOT Found");
			throw new UserNotFoundExeption();
		}
		return u;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public User addUser(@RequestBody User user){
		log.info("Creating New User: Email: "+user.getEmail()+" Password: "+user.getPassword()+" PlaylistId: "+user.getPlaylist());
		log.info("User Created Successfully");
		return ud.createUser(new User(user.getEmail(), user.getPassword(), user.getPlaylist()));
	}

	@PutMapping(value="/{id}")
	public void updateUser(@PathVariable("id")Integer id, @RequestBody User user){
		log.info("Requesting User With Id: "+id);
		User u = ud.getUserById(id);
		List<Playlists> playlist = user.getPlaylist();
		List<Playlists> play = u.getPlaylist();
		System.out.println(play);
		for(Playlists p: playlist) {
			play.add(p);
			
		}
		System.out.println(play);
		u.setPlaylist(play);
		
		log.info("MODIFYING USER: "+u);

		u.setEmail(user.getEmail());

		u.setPassword(user.getPassword());

//		u.setPlaylist(user.getPlaylist());

		ud.updateUser(u);
		log.info("User "+id+"Modified");
	}

	@DeleteMapping(value="/{id}")
	public void deleteUser(@PathVariable("id")Integer id){
		User u = ud.getUserById(id);
		log.info("USER WITH ID NUM "+id+" MARKED FOR DELETION!!");
		ud.deleteUser(u);
		log.info("USER "+id+" DELETED...");
	}


	@PostMapping("/login")
	public List<User> loginUsers(@RequestBody User user){
		List<User> users = ud.getAllUsers();
		for(User u: users) {
			if(null==user.getEmail()|| "".equals(user.getEmail())){
				if(null==user.getPassword()|| "".equals(user.getPassword())){
					log.info("User NonExistent");
					throw new UserNotFoundExeption();
				}
			}
			if(!u.getEmail().equals(user.getEmail()) || !u.getPassword().equals(user.getPassword())){
				log.info("USER CREDENTIALS DO NOT MATCH");
				continue;
			}
			users.clear();
			users.add(u);
			log.info("Searched Database");
			return users;

		}
		log.info("NO SUCH USER EXISTS");
		return new ArrayList<>();
	}
}

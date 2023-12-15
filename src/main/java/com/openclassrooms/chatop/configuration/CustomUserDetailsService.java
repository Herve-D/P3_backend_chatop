package com.openclassrooms.chatop.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.repository.IUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserRepository userRepository;

	/**
	 * Load user details by username.
	 * 
	 * @param username - The username of the user to load.
	 * @return UserDetails object containing user details.
	 * @throws UsernameNotFoundException If the user with the given username is not
	 *                                   found.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ChatopUser user = userRepository.findByEmail(username).get();

		// Create a UserDetails objet using the username, password and granted
		// authorities.
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getGrantedAuthorities());
	}

	/**
	 * Get the granted authorities for the user.
	 * 
	 * @return List of GrantedAuthority objects representing the user's roles.
	 */
	private List<GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}

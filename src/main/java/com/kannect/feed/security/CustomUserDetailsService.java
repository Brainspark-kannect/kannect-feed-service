package com.kannect.feed.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kannect.user.auth.entity.User;
import com.kannect.user.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = repo.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				user.getRoles().stream().map(role -> {
					String roleName = role.getRoleName();
					// Ensure "ROLE_" prefix is added if missing
					if (!roleName.startsWith("ROLE_")) {
						roleName = "ROLE_" + roleName;
					}
					return new SimpleGrantedAuthority(roleName);
				}).collect(Collectors.toList()));
	}

}

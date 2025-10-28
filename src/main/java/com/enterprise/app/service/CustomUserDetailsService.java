package com.enterprise.app.service;

import com.enterprise.app.model.Employee;  // Your User entity class
import com.enterprise.app.repo.EmployeeRepository;  // JPA repository for User entity

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

       
        List<GrantedAuthority> authorities = emp.getRoles().stream()
        	    .map(role -> new SimpleGrantedAuthority(role))  
        	    .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(
        	    emp.getEmail(),       
        	    emp.getPassword(),
        	    authorities
        	);
    }
}

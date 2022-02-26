package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		try {
			// ��û�� �̿��� ������ ����� �����
			UserEntity user = UserEntity.builder()
										.email(userDTO.getEmail())
										.username(userDTO.getUsername())
										.password(userDTO.getPassword())
										.build();
			//���񽺸� �̿��� �������͸��� ����� ����
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
										     .email(registeredUser.getEmail())
										     .id(registeredUser.getId())
										     .username(registeredUser.getUsername())
										     .build();
			return ResponseEntity.ok().body(responseUserDTO);
		}catch(Exception e) {
			//����� ������ �׻� �ϳ��̹Ƿ� ����Ʈ�� ������ �ϴ� ResponseDTO�� ������� �ʰ� �׳Ƥ� UserDTO ����
			
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);
		
		if(user != null) {
			// ��ū ����
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
												   .email(user.getUsername())
												   .id(user.getId())
												   .token(token)
												   .build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed.").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {

	@Autowired
	private TodoService service;
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			// (1) TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) id�� null�� �ʱ�ȭ�Ѵ�. ���� ��ÿ��� id�� ����� �ϱ� �����̴�.
			entity.setId(null);
			
			// (3) �ӽ� ����� ���̵� ������ �ش�. �� �κ��� 4�� ������ �ΰ����� ������ �����̴�. ������ ������ �ΰ� ����� �����Ƿ� �� ����ڷθ� �α��� ���� ����Ҽ� �ִ� ���ø����̼���
			entity.setUserId(userId);
			
			// (4) ���񽺸� �̿��� Todo ��ƼƼ�� �����Ѵ�.
			List<TodoEntity> entities = service.create(entity);
			
			// (5) �ڹ� ��ũ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (6) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (7) ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok().body(response);
		}catch (Exception e) {
			// (8) Ȥ�� ���ܰ� �ִ� ��� dto ��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
		// (1) ���� �޼����� retrieve() �޼��带 ����� Todo ����Ʈ�� �����´�.
		List<TodoEntity> entities = service.retrieve(userId);
		
		// (2) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (3) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (4) ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		// (1) TodoEntity�� ��ȯ�Ѵ�.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// (2) �ӽ� ����� ���̵� ������ �ش�. �� �κ��� 4�� ������ �ΰ����� ������ �����̴�. ������ ������ �ΰ� ����� �����Ƿ� �� ����ڷθ� �α��� ���� ����Ҽ� �ִ� ���ø����̼���
		entity.setUserId(userId);
		
		// (3) ���񽺸� �̿��� entity�� ������Ʈ�Ѵ�.
		List<TodoEntity> entities = service.update(entity);
		
		// (4) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (5) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (6) ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			// (1) TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) �ӽ� ����� ���̵� ������ �ش�. �� �κ��� 4�� ������ �ΰ����� ������ �����̴�. ������ ������ �ΰ� ����� �����Ƿ� �� ����ڷθ� �α��� ���� ����Ҽ� �ִ� ���ø����̼���
			entity.setUserId(userId);
			
			// (3) ���񽺸� �̿��� entity�� �����Ѵ�.
			List<TodoEntity> entities = service.delete(entity);
			
			// (4) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (5) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (6) ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			// (7) Ȥ�� ���ܰ� �ִ� ��� dto ��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}

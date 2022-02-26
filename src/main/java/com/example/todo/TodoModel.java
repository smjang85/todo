package com.example.todo;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TodoModel {
	
	@NonNull
	private String id;
}

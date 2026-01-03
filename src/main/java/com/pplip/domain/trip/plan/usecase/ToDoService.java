package com.pplip.domain.trip.plan.usecase;

import com.pplip.domain.trip.plan.api.request.ToDoRequest;
import com.pplip.domain.trip.plan.api.response.ToDoResponse;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ToDoService {
	List<ToDoResponse.ToDoSummary> getList(Long planId);

	ToDoResponse.ToDoDetail createToDo(List<ToDoRequest.PostTodo> request, Long planId);

	ToDoResponse.ToDoDetail getToDo(Long id);

	ToDoResponse.ToDoUpdated updateToDoList(List<ToDoRequest.Update> update, Long planId);

	Void removeToDo(Long id, UserDetails userDetails);
}

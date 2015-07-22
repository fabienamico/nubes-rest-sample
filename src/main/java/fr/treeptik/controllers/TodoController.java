package fr.treeptik.controllers;

import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

import fr.treeptik.domains.Todo;
import fr.treeptik.services.TodoService;

@Controller("/api")
@ContentType("application/json")
public class TodoController {

	@Service("todoService")
	private TodoService todoService;
	
	@POST("/todo")
	public void addTodo(@RequestBody Todo todo, RoutingContext context, Payload<Todo> result){
		
		todoService.save(todo, handler -> {
			if (handler.succeeded()){
				result.set(handler.result());
				context.next();
			}
		});

	}
	
}

package fr.treeptik.services;


import fr.treeptik.domains.Todo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import com.github.aesteve.vertx.nubes.services.Service;

public class TodoService implements Service{

	private JDBCClient jdbcClient;
	private JsonObject confJdbc;
	
	@Override
	public void init(Vertx vertx) {
		jdbcClient = JDBCClient.createShared(vertx, confJdbc);
	}

	public TodoService(JsonObject confJdbc) {
		this.confJdbc = confJdbc;
	}
	
	
	public void save(Todo todo, Handler<AsyncResult<Todo>> result) {
		
		jdbcClient.getConnection(r -> {
			if(r.succeeded()){
				
				SQLConnection sqlConnection = r.result();
				JsonArray sqlParams = new JsonArray();
				sqlParams.add(todo.getAction());
				sqlParams.add(todo.getDone());
				sqlConnection.updateWithParams("Insert Into todos (action, done) Values (?,?)", sqlParams, h -> {
					if (h.succeeded()){
						todo.setId(h.result().getKeys().getInteger(0));
						result.handle(Future.succeededFuture(todo));
						sqlConnection.close();
					}
				});
				
			}
		});	
	}

	@Override
	public void start(Future<Void> future) {
		future.complete();
	}

	@Override
	public void stop(Future<Void> future) {
		future.complete();
	}


	
	
}

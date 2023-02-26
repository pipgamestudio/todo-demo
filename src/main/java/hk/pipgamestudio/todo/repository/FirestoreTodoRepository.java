package hk.pipgamestudio.todo.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import hk.pipgamestudio.todo.entity.FirestoreTodo;
import hk.pipgamestudio.todo.entity.Todo;

@Repository
public class FirestoreTodoRepository {
	private final static String COLLECTION_NAME = "Todo";
	
	private FirestoreTodo getTodo(String userId) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		
		DocumentReference docRef = db.collection(COLLECTION_NAME).document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(FirestoreTodo.class);
        } else {
            return null;
        }
	}
	
	public void createUser(String userId) {
		try {
			FirestoreTodo todo = getTodo(userId);
			if (null == todo || null == todo.getUserId()) {
				Firestore db = FirestoreClient.getFirestore();
				
				DocumentReference docRef = db.collection(COLLECTION_NAME).document(userId);
				todo = new FirestoreTodo(userId, new ArrayList<Todo>(), new ArrayList<Todo>());
		        docRef.set(todo).get();
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Todo> findNotComplete(String userId) {
		List<Todo> todos = new ArrayList<Todo>();
		
		try {
			todos = getAllSubTodos(userId, "notCompleteList");
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return todos;
	}

	public List<Todo> findCompleted(String userId) {
		List<Todo> todos = new ArrayList<Todo>();
		
		try {
			todos = getAllSubTodos(userId, "completedList");
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return todos;
	}
	
	private List<Todo> getAllSubTodos(String userId, String type) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		
        List<Todo> todos = new ArrayList<>();
        CollectionReference todoCollection = db.collection(COLLECTION_NAME);
        DocumentReference todoItemRef = todoCollection.document(userId);
        ApiFuture<DocumentSnapshot> future = todoItemRef.get();
        DocumentSnapshot document = future.get();
        FirestoreTodo fstodo = document.toObject(FirestoreTodo.class);
        
        if (type.equals("notCompleteList")) {
        	todos = fstodo.getNotCompleteList();
        } else {
        	todos = fstodo.getCompletedList();
        }
        
        // sort
        if (todos.size() > 1) {
			Collections.sort(todos, (t1, t2) -> {
				return t1.getCreatedDate().compareTo(t2.getCreatedDate());
			});
		}

        return todos;
    }
	
	private void saveTodoToList(String userId, Todo todo, String type) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			
	        FirestoreTodo fstodo = getTodo(userId);
	        if (fstodo != null) {
	        	if (type.equals("notCompleteList")) {
			        List<Todo> notCompleteList = fstodo.getNotCompleteList();
			        if (notCompleteList == null) {
			        	notCompleteList = new ArrayList<>();
			        }
			        notCompleteList.add(todo);
		
			        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(userId).update("notCompleteList", FieldValue.arrayUnion(todo));
	        	} else {
	        		List<Todo> completedList = fstodo.getCompletedList();
			        if (completedList == null) {
			        	completedList = new ArrayList<>();
			        }
			        completedList.add(todo);
		
			        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(userId).update("completedList", FieldValue.arrayUnion(todo));
	        	}
	        }
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save(String userId, Todo todo) {
		saveTodoToList(userId, todo, "notCompleteList");
	}
	
	public void deleteById(String userId, String todoCreateDate) {
		try {
			deleteById(userId, todoCreateDate, "completedList");
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteById(String userId, String todoCreateDate, String type) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		
		FirestoreTodo fstodo = getTodo(userId);
		if (null != fstodo) {
			List<Todo> todoList = new ArrayList<Todo>();
			if (type.equals("completedList")) {
				todoList = fstodo.getCompletedList();
			} else {
				todoList = fstodo.getNotCompleteList();
			}
			
			if (null != todoList) {
				for (Todo todo : todoList) {
					if (todo.getCreatedDate().equals(todoCreateDate)) {
						todoList.remove(todo);
						ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(userId).update(type, FieldValue.arrayRemove(todo));
							
						break;
					}
				}
			}
		}		
	}
	
	public void saveToComplete(String userId, String todoCreateDate, String todoContent) {
		try {
			deleteById(userId, todoCreateDate, "notCompleteList");
			
			Todo todo = Todo.builder()
					.createdDate(todoCreateDate)
					.todoItem(todoContent)
					.completed("Yes")
					.build();
			
			saveTodoToList(userId, todo, "completedList");
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToNotComplete(String userId, String todoCreateDate, String todoContent) {
		try {
			deleteById(userId, todoCreateDate, "completedList");
			
			Todo todo = Todo.builder()
					.createdDate(todoCreateDate)
					.todoItem(todoContent)
					.completed("No")
					.build();
			
			saveTodoToList(userId, todo, "notCompleteList");
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

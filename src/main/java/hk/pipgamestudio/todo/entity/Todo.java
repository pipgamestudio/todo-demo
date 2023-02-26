package hk.pipgamestudio.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Todo {
	public final static String CREATED_DATE = "createdDate";
	public final static String TOOD_ITEM = "todoItem";
	public final static String COMPLETED = "completed";

	private String createdDate;
	private String todoItem;
	private String completed;
}
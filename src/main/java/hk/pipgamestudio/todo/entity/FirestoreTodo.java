package hk.pipgamestudio.todo.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FirestoreTodo {
	private String userId;
	private List<Todo> notCompleteList;
	private List<Todo> completedList;
}

package hk.pipgamestudio.todo.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hk.pipgamestudio.todo.entity.Todo;
import hk.pipgamestudio.todo.repository.FirestoreTodoRepository;
import jakarta.servlet.http.HttpSession;


@Controller
public class TodoController {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	FirestoreTodoRepository todoRepository;
	
	@Autowired
	HttpSession session;
	
	/**
	@GetMapping
	public String index() {
		return "index.html";
	}
	*/

	@GetMapping("/todos")
	public String todos(Model model) {
		
		String userId = (String) session.getAttribute("userId");
		
		model.addAttribute("name", userId);
		model.addAttribute("todos_notcomplete", todoRepository.findNotComplete(userId));
		model.addAttribute("todos_completed", todoRepository.findCompleted(userId));
		
		return "todos";
	}
	
	@PostMapping("/todoNew")
	public String add(@RequestParam String todoItem, Model model) {
		
		String userId = (String) session.getAttribute("userId");
		
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		String createdDate = df.format(new Date());
		
		String xml10pattern = "[^"
                + "\u0009\r\n"
                + "\u0020-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]";
		String content = todoItem.replaceAll(xml10pattern, "").replaceAll("[\\%\\;\\/\\\\]", "");
		
		Todo todo = Todo.builder()
				.createdDate(createdDate)
				.todoItem(content)
				.completed("No")
				.build();
				
		todoRepository.save(userId, todo);
		
		model.addAttribute("name", userId);
		model.addAttribute("todos_notcomplete", todoRepository.findNotComplete(userId));
		model.addAttribute("todos_completed", todoRepository.findCompleted(userId));
		
		return "redirect:/todos";
	}
	
	@PostMapping("/todoDelete/{id}")
	public String delete(@PathVariable String id, Model model) {
		String userId = (String) session.getAttribute("userId");
		
		todoRepository.deleteById(userId, id);
	
		model.addAttribute("name", userId);
		model.addAttribute("todos_notcomplete", todoRepository.findNotComplete(userId));
		model.addAttribute("todos_completed", todoRepository.findCompleted(userId));
		return "redirect:/todos"; 
	}
	
	@PostMapping("/todoUpdateToComplete/{id}/{content}")
	public String updateComplete(@PathVariable String id,
			@PathVariable String content, Model model) {
		String userId = (String) session.getAttribute("userId");
	
		todoRepository.saveToComplete(userId, id, content);
		
		model.addAttribute("name", userId);
		model.addAttribute("todos_notcomplete", todoRepository.findNotComplete(userId));
		model.addAttribute("todos_completed", todoRepository.findCompleted(userId));
		return "redirect:/todos";
	}
	
	@PostMapping("/todoUpdateToNotComplete/{id}/{content}")
	public String updateNotComplete(@PathVariable String id,
			@PathVariable String content, Model model) {
		String userId = (String) session.getAttribute("userId");
	
		todoRepository.saveToNotComplete(userId, id, content);
		
		model.addAttribute("name", userId);
		model.addAttribute("todos_notcomplete", todoRepository.findNotComplete(userId));
		model.addAttribute("todos_completed", todoRepository.findCompleted(userId));
		return "redirect:/todos";
	}
}
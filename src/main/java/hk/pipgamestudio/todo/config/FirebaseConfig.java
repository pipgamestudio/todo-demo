package hk.pipgamestudio.todo.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseConfig {
	
	@PostConstruct
	public void FirebaseInitialization() throws IOException {
		if(FirebaseApp.getApps().isEmpty()) {
			GoogleCredentials googleCredentials = GoogleCredentials
		            .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
		    FirebaseOptions options = FirebaseOptions
		            .builder()
		            .setCredentials(googleCredentials)
		            .build();
		    
			FirebaseApp.initializeApp(options);
		}
	}

}

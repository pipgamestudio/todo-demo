package hk.pipgamestudio.todo.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private static List<String> clients = Arrays.asList("google", "facebook", "github");
  private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

  @Autowired
  private Environment env;

  private ClientRegistration getRegistration(String client) {
      String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

      if (clientId == null) {
          return null;
      }

      String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

      if (client.equals("google")) {
          return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
      }
      if (client.equals("facebook")) {
    	  String redirect_uri = "https://" + env.getProperty("server.hostname") + "/login/oauth2/code/facebook"; // must use https for facebook
          return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret).redirectUri(redirect_uri).build();
      }
      if (client.equals("github")) {
          return CommonOAuth2Provider.GITHUB.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
      }
      
      return null;
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	  http.authorizeRequests()
      .requestMatchers("/oauth_login", "/img/**")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .csrf().disable()
      .oauth2Login()
      .loginPage("/oauth_login");
	  
	  return http.build();
  }
  
  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
      List<ClientRegistration> registrations = clients.stream()
        .map(c -> getRegistration(c))
       .filter(registration -> registration != null)
        .collect(Collectors.toList());
      
      return new InMemoryClientRegistrationRepository(registrations);
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClientService() {
	  return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
  }
}
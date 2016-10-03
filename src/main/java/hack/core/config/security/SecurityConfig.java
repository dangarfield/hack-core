package hack.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl securityUserService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("mkyong").password("123456").roles("USER");
//		auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
		auth.inMemoryAuthentication().withUser("admin@admin.com").password("pass").roles("ADMIN");
		auth.userDetailsService(securityUserService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		http.authorizeRequests().antMatchers("/app/**")
				.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')").and().formLogin().loginPage("/login").loginProcessingUrl("/login")
				.usernameParameter("email").passwordParameter("password").and().exceptionHandling().accessDeniedPage("/denied").and().csrf().disable();

	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
}

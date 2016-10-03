package hack.core.config.security;

import java.util.Arrays;
import java.util.List;

import hack.core.dao.PlayerDAO;
import hack.core.models.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PlayerDAO playerDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Player player = playerDAO.getUserByEmail(username);
		
		if(player == null) {
			throw new UsernameNotFoundException("Email not recognised");
		}
		List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

	    return new User(player.getEmail(), player.getPassword(), authorities );
	}

}

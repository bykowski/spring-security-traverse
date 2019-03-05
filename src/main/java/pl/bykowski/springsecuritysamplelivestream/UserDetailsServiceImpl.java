package pl.bykowski.springsecuritysamplelivestream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bykowski.springsecuritysamplelivestream.dao.UserRepo;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<pl.bykowski.springsecuritysamplelivestream.dao.User> userByUserName =
                userRepo.findUserByUserName(username);
        if (userByUserName.isPresent()) {
            UserDetails moderator = User.withDefaultPasswordEncoder()
                    .username(userByUserName.get().getUserName())
                    .password(userByUserName.get().getUserPassword())
                    .roles("MODERATOR")
                    .build();
            return moderator;
        } else {
            throw new RuntimeException();
        }

    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        userRepo.save(new pl.bykowski.springsecuritysamplelivestream.dao.User("user123", "user123"));
    }

}

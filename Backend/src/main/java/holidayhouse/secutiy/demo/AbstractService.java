package holidayhouse.secutiy.demo;

import holidayhouse.secutiy.user.User;
import holidayhouse.secutiy.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;

    protected User getLoggedInUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));
    }
}

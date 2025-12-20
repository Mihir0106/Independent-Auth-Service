package com.Independent.AuthService.Service;

import com.Independent.AuthService.Model.User;
import com.Independent.AuthService.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = repository.findByUsername(username);

        if (user.isPresent()) {
            var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles("USER")
                    .disabled(!userObj.isVerified())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public boolean verifyUser(String token){
        Optional<User> userOptional = repository.findByVerificationToken(token);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setVerified(true);
            user.setVerificationToken(null); // clear the token
            repository.save(user);
            return true;
        }

        return  false;
    }


}

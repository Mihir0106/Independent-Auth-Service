package com.Independent.AuthService.Controller;

import com.Independent.AuthService.Model.User;
import com.Independent.AuthService.Repository.UserRepository;
import com.Independent.AuthService.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @GetMapping("req/signup/verify")
    public ResponseEntity verifyEmail(@RequestParam("token") String token){
        String emailString = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(emailString);

        if(user == null || user.getVerificationToken() == null){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }

        if(!jwtUtils.validateToken(token) || !user.getVerificationToken().equals(token)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }

        user.setVerificationToken(null);
        user.setVerified(true);
        userRepository.save(user);

        return  ResponseEntity.status(HttpStatus.CREATED).body("Email Successfully Verified");
    }

}

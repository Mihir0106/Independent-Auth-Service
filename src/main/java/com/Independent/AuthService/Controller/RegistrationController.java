package com.Independent.AuthService.Controller;

import com.Independent.AuthService.DTO.AuthResponse;
import com.Independent.AuthService.Model.User;
import com.Independent.AuthService.Repository.UserRepository;
import com.Independent.AuthService.Service.EmailService;
import com.Independent.AuthService.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @PostMapping(value = "req/signup", consumes = "application/json")
    public ResponseEntity<?> createUser(@RequestBody User user){

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            if(existingUser.isVerified()){
                return new ResponseEntity<>("User Already Exist and Verified", HttpStatus.BAD_REQUEST);
            }
            else{
                String verificationToken = JwtTokenUtil.generateToken(existingUser.getEmail());
                existingUser.setVerificationToken(verificationToken);
                userRepository.save(existingUser);
                // Send Email Code
                emailService.sendVerificationEmail(existingUser.getEmail(),verificationToken);

                // Return a sanitized response, NOT the user object
                return ResponseEntity.ok(new AuthResponse(null, "User Registration Successfully"));
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(),verificationToken);

        return new ResponseEntity<>("Registration Successful, Please Verify your Email", HttpStatus.OK);


    }
}

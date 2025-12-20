package com.Independent.AuthService.Controller;

import com.Independent.AuthService.DTO.AuthResponse;
import com.Independent.AuthService.DTO.LoginRequest;
import com.Independent.AuthService.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/req")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            //1. Validate Email and Password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
            );

            // if valid, generate Token
            if(authentication.isAuthenticated()){
                String token = JwtTokenUtil.generateToken(loginRequest.getEmail());
                return  ResponseEntity.ok(new AuthResponse(token,"Login Successful"));
            }
        }
        catch (BadCredentialsException e){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed");
    }
}

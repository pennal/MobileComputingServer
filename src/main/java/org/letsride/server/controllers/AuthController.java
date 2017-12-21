package org.letsride.server.controllers;

import org.letsride.server.models.Account;
import org.letsride.server.models.Role;
import org.letsride.server.models.User;
import org.letsride.server.repositories.UserRepository;
import org.letsride.server.requests.auth.UserSignup;
import org.letsride.server.responses.JWTResponse;
import org.letsride.server.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(PasswordEncoder passwordEncoder, TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    // signup
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> handleSignup(@RequestBody UserSignup user, HttpServletRequest request) {

        User existingUser = this.userRepository.findByEmailAddress(user.getEmail());

        if (existingUser != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }

        // TODO: Is this redundant?
        User u = new User();
        u.setFirstName(user.getFirstname());
        u.setLastName(user.getLastname());
        u.setEmailAddress(user.getEmail());
        u.setUsername(user.getUsername());
        u.setDateOfBirth(user.getDateOfBirth());

        Account a = new Account(user.getEmail(), user.getPassword());
        a.encodePassword(this.passwordEncoder);

        u.setAccount(a);

        this.userRepository.save(u);

        JWTResponse res = new JWTResponse(this.tokenProvider.createToken(u.getEmailAddress()));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    //login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody Account loginUser, HttpServletResponse httpServletResponse) throws IOException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

        ResponseEntity response = null;
        // FIXME: Damn this is ugly...
        try {
            this.authenticationManager.authenticate(authenticationToken);

            User authenticatingUser = this.userRepository.findByAccount_Username(loginUser.getUsername());

            JWTResponse res = new JWTResponse(this.tokenProvider.createToken(authenticatingUser.getEmailAddress()));

            response = ResponseEntity.ok(res);
        } catch (DisabledException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is disabled");
        } catch (AuthenticationException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong credentials");
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return response;
    }
}

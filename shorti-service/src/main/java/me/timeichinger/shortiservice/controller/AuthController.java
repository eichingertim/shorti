package me.timeichinger.shortiservice.controller;

import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.service.UserService;
import me.timeichinger.shortiservice.utils.AuthTokenResponse;
import me.timeichinger.shortiservice.utils.requests.AuthenticationRequest;
import me.timeichinger.shortiservice.utils.AuthTokenUtil;
import me.timeichinger.shortiservice.utils.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthTokenUtil authTokenUtil;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = authTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthTokenResponse(token));
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<?> signUpUser(@RequestBody UserRequest user) throws Exception {
        User createdUser = userService.createUser(user);
        createdUser.setPassword("xxx");
        return ResponseEntity.ok(createdUser);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
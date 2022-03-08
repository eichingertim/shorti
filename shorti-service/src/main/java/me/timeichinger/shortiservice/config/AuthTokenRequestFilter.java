package me.timeichinger.shortiservice.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.repositories.UserRepository;
import me.timeichinger.shortiservice.utils.AuthTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthTokenRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        final TokenUserWrapper wrapper = getUserFromToken(requestTokenHeader);


        if (wrapper.getToken() != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(wrapper.getUsername());

            if (jwtTokenUtil.validateToken(wrapper.getToken(), userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        request.setAttribute("user-id", wrapper.getUserId());

        chain.doFilter(request, response);
    }

    public TokenUserWrapper getUserFromToken(String requestTokenHeader) {
        TokenUserWrapper tokenUserWrapper = new TokenUserWrapper();
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            tokenUserWrapper.setToken(requestTokenHeader.substring(7));
            try {
                tokenUserWrapper.setUsername((jwtTokenUtil.getUsernameFromToken(tokenUserWrapper.getToken())));
                User user = userRepository.findUserByUsername(tokenUserWrapper.getUsername());
                tokenUserWrapper.setUserId(user.getId());
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }
        return tokenUserWrapper;
    }

    @Getter
    @Setter
    private static class TokenUserWrapper {
        private String userId;
        private String username;
        private String token;


    }

}
package org.letsride.server.security.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class TokenProvider {
    private String secretKey;

    private long tokenValidityInMilliseconds;

    private final UserDetailsService userService;

    @Autowired
    public TokenProvider(UserDetailsService userService) {
        this.userService = userService;

        int durationInSeconds = 60 * 60 * 24;
        this.secretKey = "HelloWorld";
        this.tokenValidityInMilliseconds =  durationInSeconds * 1000;

        System.err.println("Validity in milliseconds: " + this.tokenValidityInMilliseconds);
        System.err.println("Secret: " + this.secretKey);
    }

    public String createToken(String emailAddress) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);

        return Jwts.builder().setId(UUID.randomUUID().toString())
                .setSubject(emailAddress)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();

        UserDetails userDetails = this.userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
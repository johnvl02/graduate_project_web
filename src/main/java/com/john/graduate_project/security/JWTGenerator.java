package com.john.graduate_project.security;

import com.john.graduate_project.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
public class JWTGenerator {

    public String generateToken(UserDto userDTO){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        Claims claims = Jwts.claims();
        claims.put("username", userDTO.getUsername());

        return Jwts.builder()
                .setSubject(userDTO.getUsername())
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,SecurityConstants.JWT_SECRET)
                .compact();
    }

    public String getUsernameJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("username");

    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}

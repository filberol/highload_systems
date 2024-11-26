package ru.itmo.oxygen.config.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class JwtTokenUtils {

    @Value("\${token.secret.key:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJVadQssw5c}")
    private lateinit var jwtSecret: String

    @Value("\${token.secret.expiration:1000000}")
    private var jwtExpirationMs: Long? = null

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap()
        val roleList = userDetails.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .toList()
        claims["roles"] = roleList
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + jwtExpirationMs!!)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedDate)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact()
    }

    fun getUsername(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getRoles(token: String): List<String>? {
        return getAllClaimsFromToken(token).get("roles", List::class.java) as List<String>?
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .build()
            .parseSignedClaims(token)
            .body
    }
}

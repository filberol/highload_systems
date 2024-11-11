package ru.itmo.user.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtTokenUtils {
    @Value("382ab493gh0439rtvb0429uruj2456yh937dd763fe87t3f89536am")
    private val secret: String? = null

    @Value("10m")
    private val jwtLifetime: Duration? = null

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any?> = HashMap()
        val roleList = userDetails.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .toList()
        claims["roles"] = roleList
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + jwtLifetime!!.toMillis())
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedDate)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun getUsername(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getRoles(token: String): List<String> {
        return getAllClaimsFromToken(token).get("roles", List::class.java) as List<String>
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(token).build().parseClaimsJws(token)
                .body
    }
}
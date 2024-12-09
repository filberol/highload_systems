package ru.itmo.department.config.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts

class JwtTokenUtils {

    private var jwtSecret =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJVadQssw5c"

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

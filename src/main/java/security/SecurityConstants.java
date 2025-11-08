package security;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class SecurityConstants {

    private static final String JWT_SECRET = "fQw8vDNz3pL!eR$Yx7uT@9sZqG2nH#jM";

    public static final SecretKey JWT_SECRET_KEY = Keys.hmacShaKeyFor(
            JWT_SECRET.getBytes(StandardCharsets.UTF_8)
    );

    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 3_600_000; 
}
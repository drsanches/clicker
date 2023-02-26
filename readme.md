# Clicker - Reactive Spring application using web tokens

## Environment

- `PORT` - application port
- `TOKEN_SECRET` - base64 encoded secret
- `MONGO_HOST` - mongo host
- `MONGO_PORT` - mongo port
- `MONGO_DATABASE` - mongo database for connection
- `MONGO_USERNAME` - mongo username with RW access to `MONGO_DATABASE`
- `MONGO_PASSWORD` - mongo password for `MONGO_USERNAME`

## Instructions
    
How to generate new base64 secret key:

    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import java.util.Base64;
    
    Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded())
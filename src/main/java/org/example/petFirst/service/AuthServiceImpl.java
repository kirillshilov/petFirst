package org.example.petFirst.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petFirst.exception.UserNotFound;
import org.example.petFirst.model.*;
import org.example.petFirst.repository.UserRepository;
import org.example.petFirst.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final String privatKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCX/cwL6c39vlCw" +
            "gnMIwk9CGxlg67c+ggyhAhjATJqMyYoPdfkrmNSIUEPy3l1XHQ+3LUMH2jM5fPRY" +
            "Y7kwrVN5gf6jhnaJyYn3jXqvPHLXgEEMjhaiothmyzzHWApMIwUaA54p5DsFz/ua" +
            "ml7GtgAlSRdm4rqgzMwjubGyVpE4itDd3siDR0wp/gowcPBKmqvW4LpqPD9E7kMe" +
            "wylgD7A2JgfX8CQfUUt4bQaaxTrpASpt9swP+Xv9hc573T87QG+0yMpu4PIMoy3q" +
            "L2JEOLu4Ha+tq//rMWZylAbpRdkHT8OLb9rillkZMbS9Hiosg1+/m5bI7zzkBqVF" +
            "tVg7sxG7AgMBAAECggEADvB29j4kmCn1BmnGuCKukFcs4cDpga8It2lcWMfEHSMm" +
            "JdTjs+xRX5X8HSAhg+bVskc0TSOeYKqD3eLsw03AubMUU1XBIFP7R/t+W+u7d1Z3" +
            "jliyUt63bzXPeIySc99b/gLtsJJqrBdAVMw9lUHdtncpDLIrw+hoBvmWmhU2MEdr" +
            "Qjqm6yRHegyQKLWbzo7wrsG/inSSUWN+DdffwEkbFxADCj1IoYJjCLQFFgVknLdC" +
            "dne/a7q+3jr/4r3t5LQdvMBrZ+jeAztXQ3RmSl+7m+T6pq/Qo5Emh0SpTFIiLYS/" +
            "gtz3xciJYud3QVJHJHXhlX/jMORrbPjX7U8/FUG5KQKBgQDJmRH6/wLCxRHJUw14" +
            "wNXc+qCs7QJjTTgZqRasmI4IsFEw5HJI+34teSZ/7WEPpgKLLmL1Ng/ok5GcWBVb" +
            "ul62j3FSGJ8DqsYZrAusF7k+gLydTVVDChmyrlOGVIf0Kq2Ktaty65/Ko7FPNHCS" +
            "/DnYCKMhJvBrteqeTJwnybj29QKBgQDBAcX2II/w6D1bZrDL0p/kX1brKGnm9tuo" +
            "/UK2WEa+Lo43INY4ISXnNCEVvob4baRgM/pZg8lVzXimHXTATqlhC3Nct4iHK5bn" +
            "HjbS9JuMEWBcewbD84cUd4s040sJACAlzRkLAarvcy0KIy8nuYoU2OrXLgu7z2v9" +
            "nVDPuLKX7wKBgCVzkVqOq2WhlLdQ/Yy0Tka6LMVTVoE6JDToK7Uuthw5HSw3FGAo" +
            "kH1EJ3nWkJSGo6xVlr61J2lumLtxxa3/xn6l0UiOGGRnAlxckOKMmbZZ1jBm4HMO" +
            "2k1Og2fOJl+e2hSNwWcdDNdpZ5QuSseToe9XU27iPAHWjtqPoublW75NAoGAaFy6" +
            "Gz0+N+iMDNk5Le0k8gqvaBaBNRc0bWsMYKbZEcsAsUR7MPyTjzH71dMhDkHTCQXy" +
            "D+cXveKNo1z4HWgrZEMZzP+cvmJOhYgr4uPLG/DlZvxLCzeqSeHeDun8T9CWkXoW" +
            "WYI8WJ+xsfKWc+d6h41HZrUKOTKOVSN0Z+2Z/FcCgYAk1ffUxY3ms5+Ty29S+jeD" +
            "HKE7A0BwXsiyl3YgkB2PjP9SPL65vfcTeEIQWCas+TrG75bOhIdgsRthl0Z/z9T8" +
            "DSwID+S7qSxSf6KY1pyxlyyMCqodmUpJr+VgfIEYsJVpWkaevgJBpEXVqXpYtG9L" +
            "DHzBThqD/2crpOjkK89g/Q==";
    private final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl/3MC+nN/b5QsIJzCMJP" +
            "QhsZYOu3PoIMoQIYwEyajMmKD3X5K5jUiFBD8t5dVx0Pty1DB9ozOXz0WGO5MK1T" +
            "eYH+o4Z2icmJ9416rzxy14BBDI4WoqLYZss8x1gKTCMFGgOeKeQ7Bc/7mppexrYA" +
            "JUkXZuK6oMzMI7mxslaROIrQ3d7Ig0dMKf4KMHDwSpqr1uC6ajw/RO5DHsMpYA+w" +
            "NiYH1/AkH1FLeG0GmsU66QEqbfbMD/l7/YXOe90/O0BvtMjKbuDyDKMt6i9iRDi7" +
            "uB2vrav/6zFmcpQG6UXZB0/Di2/a4pZZGTG0vR4qLINfv5uWyO885AalRbVYO7MR" +
            "uwIDAQAB";

    @Override
    public Tokens login(LoginDTO loginDTO) {
        User user = userRepository.findUserByLogin(loginDTO.getLogin());
        if (user == null) {
            throw new UserNotFound("Пользователь не найден");
        }
        Tokens tokens = new Tokens();
        PrivateKey privateKey;
        try {
            privateKey = getPrivateKey();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException();
        }
            tokens.setAuthToken(
                    Jwts.builder().
                            subject(user.getId().toString()).
                            issuedAt(new Date()).claim("type", "access").
                            expiration(Date.from(Instant.now().plusSeconds(1650))).
                            claim("role", user.getRole()).
                            signWith(privateKey, SignatureAlgorithm.RS256)
                            .compact()
            );
        String refresh = Jwts.builder().
                subject(user.getId().toString()).
                issuedAt(new Date()).claim("type", "refresh").
                expiration(Date.from(Instant.now().plusSeconds(2592000))).
                claim("role", user.getRole()).
                signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
            tokens.setRefreshToken(refresh);
//TODO шифровать хеш токена и проверять пароль
            return tokens;
    }

    @Override
    public Token refresh(RefreshTokenDTO refreshToken) {
        return null;
    }

    private PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privatKey);

        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);

        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}

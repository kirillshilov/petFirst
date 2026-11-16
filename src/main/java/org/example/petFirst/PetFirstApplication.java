package org.example.petFirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class PetFirstApplication {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        SpringApplication.run(PetFirstApplication.class, args);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        String privateKey = """
                -----BEGIN PRIVATE KEY-----
                """ + Base64.getMimeEncoder(64, new byte[]{'\n'})
                .encodeToString(keyPair.getPrivate().getEncoded())
                + "\n-----END PRIVATE KEY-----\n";

        String publicKey = """
                -----BEGIN PUBLIC KEY-----
                """ + Base64.getMimeEncoder(64, new byte[]{'\n'})
                .encodeToString(keyPair.getPublic().getEncoded())
                + "\n-----END PUBLIC KEY-----\n";
        if (!Files.exists(Path.of("private.pem")) && !Files.exists(Path.of("public.pem"))) {
            Files.writeString(Path.of("private.pem"), privateKey);
            Files.writeString(Path.of("public.pem"), publicKey);
            System.out.println("RSA-ключи успешно созданы!");
        }


    }
}

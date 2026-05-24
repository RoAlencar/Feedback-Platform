package br.com.fiap.analytics.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class GenerateJwtKeys {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Path privateKeyPath = Path.of("local-keys", "privateKey.pem");
        Path publicKeyPath = Path.of("src", "main", "resources", "security", "publicKey.pem");

        Files.createDirectories(privateKeyPath.getParent());
        Files.createDirectories(publicKeyPath.getParent());

        writePemFile(privateKeyPath, "PRIVATE KEY", keyPair.getPrivate().getEncoded());
        writePemFile(publicKeyPath, "PUBLIC KEY", keyPair.getPublic().getEncoded());

        System.out.println("Chaves JWT geradas com sucesso!");
        System.out.println("Private key: " + privateKeyPath.toAbsolutePath());
        System.out.println("Public key: " + publicKeyPath.toAbsolutePath());
    }

    private static void writePemFile(Path path, String type, byte[] encodedKey) throws IOException {
        String base64Key = Base64.getMimeEncoder(64, System.lineSeparator().getBytes())
                .encodeToString(encodedKey);

        String pem = "-----BEGIN " + type + "-----" + System.lineSeparator()
                + base64Key + System.lineSeparator()
                + "-----END " + type + "-----" + System.lineSeparator();

        Files.writeString(path, pem);
    }
}
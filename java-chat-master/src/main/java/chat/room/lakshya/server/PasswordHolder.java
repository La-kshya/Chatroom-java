package chat.room.lakshya.server;

import chat.room.lakshya.shared.security.ShaUtil;

import java.util.Random;

public class PasswordHolder {
    private final String salt;
    private final String hash;

    public PasswordHolder(String salt, String hash) {
        this.hash = hash;
        this.salt = salt;
    }

    public PasswordHolder(String password) {
        Random saltGenerator = new Random();
        this.salt = Long.toString(Math.abs(saltGenerator.nextLong()));
        this.hash = ShaUtil.hash(salt + password);
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }
}

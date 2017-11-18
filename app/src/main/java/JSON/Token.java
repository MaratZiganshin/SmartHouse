package JSON;


public class Token {
    private String token;
    private String expTime;

    public Token(String token, String expTime) {
        this.token = token;
        this.expTime = expTime;
    }

    public String getToken() {
        return token;
    }

    public String getExpTime() {
        return expTime;
    }
}

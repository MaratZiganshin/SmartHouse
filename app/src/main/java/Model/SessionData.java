package Model;

import JSON.Token;

public class SessionData {
    private static Token mainToken;

    public static Token getMainToken() {
        return mainToken;
    }

    public static void setMainToken(Token _mainToken) {
        mainToken = _mainToken;
    }
}

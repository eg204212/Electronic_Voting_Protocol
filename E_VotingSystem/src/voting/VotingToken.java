package voting;

import java.util.UUID;

public class VotingToken {
    private String token;
    private boolean used;

    public VotingToken() {
        this.token = UUID.randomUUID().toString();
        this.used = false;
    }

    public String getToken() {
        return token;
    }

    public boolean isUsed() {
        return used;
    }

    public void markUsed() {
        this.used = true;
    }
}

package voting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BallotBox {

    private List<Vote> votes;
    private HashMap<String, VotingToken> tokens;  // token string → VotingToken

    public BallotBox() {
        votes = new ArrayList<>();
        tokens = new HashMap<>();
    }

    public void addToken(VotingToken token) {
        tokens.put(token.getToken(), token);
    }

    public boolean verifyAndUseToken(String tokenStr) {
        VotingToken token = tokens.get(tokenStr);
        if (token != null && !token.isUsed()) {
            token.markUsed();
            return true;
        }
        return false;
    }

    public void storeVote(Vote vote) {
        votes.add(vote);
    }

    public List<Vote> getAllVotes() {
        return votes;
    }
}

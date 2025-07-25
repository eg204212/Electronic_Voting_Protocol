package voting;

public class Vote {
    private String voteChoice;
    private byte[] signature;
    private byte[] encryptedVote;

    public Vote(String voteChoice, byte[] signature, byte[] encryptedVote) {
        this.voteChoice = voteChoice;
        this.signature = signature;
        this.encryptedVote = encryptedVote;
    }

    public String getVoteChoice() {
        return voteChoice;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getEncryptedVote() {
        return encryptedVote;
    }
}

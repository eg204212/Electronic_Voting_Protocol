package voting;

import java.security.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionAuthority {
    private KeyPair keyPair; // EA public/private key
    private Map<String, User> registeredVoters; // voterID -> User

    public ElectionAuthority() throws Exception {
        this.keyPair = CryptoUtils.generateRSAKeyPair();
        this.registeredVoters = new HashMap<>();
    }

    // Register user and issue certificate (handled in User constructor)
    public User registerVoter(String voterID) throws Exception {
        User user = new User(voterID, keyPair.getPrivate());
        registeredVoters.put(voterID, user);
        return user;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    // Verify certificate by verifying signature on voterID+publicKey using EA public key
    public boolean verifyCertificate(User user) throws Exception {
        byte[] voterIDBytes = user.getVoterID().getBytes();
        byte[] publicKeyBytes = user.getPublicKey().getEncoded();

        byte[] combined = new byte[voterIDBytes.length + publicKeyBytes.length];
        System.arraycopy(voterIDBytes, 0, combined, 0, voterIDBytes.length);
        System.arraycopy(publicKeyBytes, 0, combined, voterIDBytes.length, publicKeyBytes.length);

        return CryptoUtils.verifySignature(combined, user.getCertificate(), keyPair.getPublic());
    }

    // Decrypt and verify votes, tally results
    public Map<String, Integer> tallyVotes(List<Vote> votes, Map<String, PublicKey> voterPublicKeys) throws Exception {
        Map<String, Integer> tally = new HashMap<>();

        for (Vote vote : votes) {
            // Decrypt vote
            byte[] decrypted = CryptoUtils.decryptRSA(vote.getEncryptedVote(), keyPair.getPrivate());
            String decryptedVoteChoice = new String(decrypted);

            // Verify signature (voter's signature on voteChoice)
            boolean validSignature = false;
            for (PublicKey voterKey : voterPublicKeys.values()) {
                if (CryptoUtils.verifySignature(decryptedVoteChoice.getBytes(), vote.getSignature(), voterKey)) {
                    validSignature = true;
                    break;
                }
            }
            if (validSignature) {
                tally.put(decryptedVoteChoice, tally.getOrDefault(decryptedVoteChoice, 0) + 1);
            }
        }
        return tally;
    }
}

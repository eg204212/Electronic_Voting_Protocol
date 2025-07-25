package voting;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            ElectionAuthority ea = new ElectionAuthority();

            // 1. Register two voters
            User voter1 = ea.registerVoter("Voter001");
            User voter2 = ea.registerVoter("Voter002");

            System.out.println("Registered voters and issued certificates.");

            // Verify certificates
            System.out.println("Verify voter1 certificate: " + ea.verifyCertificate(voter1));
            System.out.println("Verify voter2 certificate: " + ea.verifyCertificate(voter2));

            // 2. Simulate login & issue voting tokens
            BallotBox ballotBox = new BallotBox();

            VotingToken token1 = new VotingToken();
            VotingToken token2 = new VotingToken();

            ballotBox.addToken(token1);
            ballotBox.addToken(token2);

            System.out.println("Voting tokens issued.");

            // 3. Voters cast votes

            // Voter 1 votes "Candidate A"
            String voteChoice1 = "Candidate A";

            // Voter signs vote
            byte[] signature1 = CryptoUtils.signData(voteChoice1.getBytes(), voter1.getPrivateKey());

            // Encrypt vote with EA public key
            byte[] encryptedVote1 = CryptoUtils.encryptRSA(voteChoice1.getBytes(), ea.getPublicKey());

            Vote vote1 = new Vote(voteChoice1, signature1, encryptedVote1);

            if (ballotBox.verifyAndUseToken(token1.getToken())) {
                ballotBox.storeVote(vote1);
                System.out.println("Voter1 vote cast successfully.");
            } else {
                System.out.println("Voter1 token invalid or used.");
            }

            // Voter 2 votes "Candidate B"
            String voteChoice2 = "Candidate B";
            byte[] signature2 = CryptoUtils.signData(voteChoice2.getBytes(), voter2.getPrivateKey());
            byte[] encryptedVote2 = CryptoUtils.encryptRSA(voteChoice2.getBytes(), ea.getPublicKey());
            Vote vote2 = new Vote(voteChoice2, signature2, encryptedVote2);

            if (ballotBox.verifyAndUseToken(token2.getToken())) {
                ballotBox.storeVote(vote2);
                System.out.println("Voter2 vote cast successfully.");
            } else {
                System.out.println("Voter2 token invalid or used.");
            }

            // 4. Tally votes
            List<Vote> allVotes = ballotBox.getAllVotes();

            Map<String, PublicKey> voterPublicKeys = Map.of(
                    voter1.getVoterID(), voter1.getPublicKey(),
                    voter2.getVoterID(), voter2.getPublicKey());

            Map<String, Integer> results = ea.tallyVotes(allVotes, voterPublicKeys);

            System.out.println("Voting Results:");
            for (String candidate : results.keySet()) {
                System.out.println(candidate + ": " + results.get(candidate));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

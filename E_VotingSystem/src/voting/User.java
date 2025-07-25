package voting;

import java.security.*;

public class User {
    private String voterID;
    private KeyPair keyPair;
    private byte[] certificate; // Signature by ElectionAuthority on (voterID + publicKey)

    public User(String voterID, PrivateKey eaPrivateKey) throws Exception {
        this.voterID = voterID;
        this.keyPair = CryptoUtils.generateRSAKeyPair();
        this.certificate = createCertificate(eaPrivateKey);
    }

    private byte[] createCertificate(PrivateKey eaPrivateKey) throws Exception {
        // Certificate = Sign(voterID || publicKey)
        byte[] voterIDBytes = voterID.getBytes();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

        byte[] combined = new byte[voterIDBytes.length + publicKeyBytes.length];
        System.arraycopy(voterIDBytes, 0, combined, 0, voterIDBytes.length);
        System.arraycopy(publicKeyBytes, 0, combined, voterIDBytes.length, publicKeyBytes.length);

        return CryptoUtils.signData(combined, eaPrivateKey);
    }

    public String getVoterID() {
        return voterID;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public byte[] getCertificate() {
        return certificate;
    }
}

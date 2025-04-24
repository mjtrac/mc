package sample.camel;

import org.apache.camel.support.jsse.*;

public class SslUtils {

    public static SSLContextParameters buildSslContextParams(SslUpdateRequest req) {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource(req.keystorePath);
        ksp.setPassword(req.keystorePassword);

        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyStore(ksp);
        kmp.setKeyPassword(req.keystorePassword);

        KeyStoreParameters tsp = new KeyStoreParameters();
        tsp.setResource(req.truststorePath);
        tsp.setPassword(req.truststorePassword);

        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(tsp);

        SSLContextParameters scp = new SSLContextParameters();
        scp.setKeyManagers(kmp);
        scp.setTrustManagers(tmp);

        return scp;
    }
}

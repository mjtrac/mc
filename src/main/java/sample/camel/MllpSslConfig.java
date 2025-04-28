
package sample.camel;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.camel.support.jsse.CipherSuitesParameters;
import java.util.List;

// for potential exceptions (not used)
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class MllpSslConfig {
    @Value("${camel.mllp.ssl.keystore.resource}")
    private String keystoreResourcePath;

    @Value("${camel.mllp.ssl.keystore.password}")
    private String keystorePassword;

    @Value("${camel.mllp.ssl.keystore.keypassword}")
    private String keystoreKeyPassword;

    @Value("${camel.mllp.ssl.truststore.resource}")
    private String truststoreResourcePath;

    @Value("${camel.mllp.ssl.truststore.password}")
    private String truststorePassword;
    @Bean
    public SSLContextParameters mysslContextParameters() {
	// Bean method's name can be referred to in yaml with # (#mysslContextParameters)
	//System.out.println("****** mllp sslContextParameters *********");

	SSLContextParameters sslContextParameters = new SSLContextParameters();
	
	KeyStoreParameters keyStoreParameters = new KeyStoreParameters();
	keyStoreParameters.setResource(keystoreResourcePath);
	//keyStoreParameters.setPassword("tnuWaA6oF1Be");//Mirth43 generated keystore password
	keyStoreParameters.setPassword(keystorePassword);

	KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
	keyManagersParameters.setKeyStore(keyStoreParameters);
	//keyManagersParameters.setKeyPassword("BezGbpwBmXaU");//Mirth43 generated key password
	keyManagersParameters.setKeyPassword(keystoreKeyPassword);

	sslContextParameters.setKeyManagers(keyManagersParameters);

	//To set one particular required protocol:
        //sslContextParameters.setSecureSocketProtocol("TLSv1.3"); // Set the protocol here

	/* Restrict to one particular cipher suite
	CipherSuitesParameters cipherSuitesParameters = new CipherSuitesParameters();
	cipherSuitesParameters.setCipherSuite(

					     List.of(
						     "TLS_AES_128_GCM_SHA256"
						     )
        );
	sslContextParameters.setCipherSuites(cipherSuitesParameters);
	*/
	//System.out.println("About to return sslContextParameters from fn mysslContextParameters in class MllpSslConfig()");
	// ----- TRUSTSTORE (to trust Mirth's certificate) -----
        KeyStoreParameters truststore = new KeyStoreParameters();
        truststore.setResource(truststoreResourcePath); // contains Mirth cert
        truststore.setPassword(truststorePassword);

        TrustManagersParameters trustManagers = new TrustManagersParameters();
        trustManagers.setKeyStore(truststore);
	// ----- TRUSTSTORE (to trust Mirth's certificate) -----

        sslContextParameters.setTrustManagers(trustManagers);
        return sslContextParameters;
    }
}


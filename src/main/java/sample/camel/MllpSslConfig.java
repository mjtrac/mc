
package sample.camel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.CipherSuitesParameters;
import java.util.List;

@Configuration
public class MllpSslConfig {

    @Bean
    public SSLContextParameters mysslContextParameters() {
	// Bean method's name can be referred to in yaml with # (#mysslContextParameters)
	//System.out.println("****** mllp sslContextParameters *********");

	SSLContextParameters sslContextParameters = new SSLContextParameters();
	
	KeyStoreParameters keyStoreParameters = new KeyStoreParameters();
	keyStoreParameters.setResource("classpath:test.jks");
	//keyStoreParameters.setPassword("tnuWaA6oF1Be");//Mirth43 generated keystore password
	keyStoreParameters.setPassword("password");

	KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
	keyManagersParameters.setKeyStore(keyStoreParameters);
	//keyManagersParameters.setKeyPassword("BezGbpwBmXaU");//Mirth43 generated key password
	keyManagersParameters.setKeyPassword("password");

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
        return sslContextParameters;
    }
}


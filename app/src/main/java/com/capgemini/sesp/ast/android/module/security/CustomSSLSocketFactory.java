/**
 * 
 */
package com.capgemini.sesp.ast.android.module.security;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


/**
 * Custom SSL Socket factory that works
 * in conjunction with custom x509 TrustManager
 * 
 * <br>
 * Added for SESPSTD-3075
 * 
 * @author Capgemini
 * @since 1st August, 2014
 * @version 1.0
 *
 */
public final class CustomSSLSocketFactory extends SSLSocketFactory {
    private transient SSLContext sslContext = null; //SSLContext.getInstance("TLS");

    /**
     * Custom constructor that accepts a custom
     * trusted keystore 
     * 
     * @param truststore (java.security.KeyStore)
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    
    public CustomSSLSocketFactory(final KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        // As X509 certificates would be used hence the trust manager
        final TrustManager tm = new CustomX509TrustManager();
        if(sslContext!=null){
        	sslContext.init(null, new TrustManager[] { tm }, null);
        }
    }
    
    /**
     * Custom constructor that accepts a pre-configured
     * SSL Context to start with
     * 
     * @param context (javax.net.ssl.SSLContext)
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */

    public CustomSSLSocketFactory(final SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        sslContext = context;
    }
    
    /**
     * Creation of socket based on provided
     * host, port. <br> 
     * Generally not used directly.
     * 
     * @param socket (java.net.Socket)
     * @param host (java.lang.String)
     * @param port (int)
     * @return java.net.Socket
     */

    @Override
    public Socket createSocket(final Socket socket, final String host, final int port,
            final boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }
    
    /**
     * Creates a new socket based on
     * the pre-initialised ssl contect
     * 
     * @return java.net.Socket
     * @throws IOException
     */

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
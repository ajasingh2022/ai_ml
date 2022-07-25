/**
 * 
 */
package com.capgemini.sesp.ast.android.module.security;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Implementing custom x509 trust manager
 * that would <b>only accept server certificate identical to device certificate</b>
 * 
 * <p>
 * Note that we are not using two way verification, i.e. 
 * <b>client verification is not done </b>here.
 * </p>
 * 
 * <br>
 * Added for SESPSTD-3075
 * 
 * 
 * @author Capgemini
 * @since 1st August, 2014
 * @version 1.0
 *
 */
public final class CustomX509TrustManager implements X509TrustManager {

	/**
	 * Holds the custom certificate to validate all 
	 * SSL connections.
	 */
	private static transient X509Certificate clientCert = null;
	public CustomX509TrustManager(){
	}
	public CustomX509TrustManager(final  String sslCertName){
		InputStream is = null;
		try{
			// Load the custom certificate from asset
			is = ApplicationAstSep.context.getAssets().open(sslCertName);
			// The certificate type would be x509
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// Obtain the public key here that would be used to verify server SSL
			clientCert = (X509Certificate) cf.generateCertificate(is);
		}catch(final Exception ex){
			writeLog("CustomX509TrustManager :" + ApplicationAstSep.context.getString(R.string.loadCustomClientCert), ex);
			// Log using DDMS logcat - useful for emulators
			//Log.e(ApplicationAstSep.context.getString(R.string.loadCustomClientCert),ex.getMessage(),ex);
			// Log to error file as per existing SESP framework
			//SespExceptionHandler.logError(Thread.currentThread(), ex);
		}finally{
			IOUtils.closeQuietly(is);
		}
	}


	/**
	 * Method not implemented as we are not validating
	 * the client
	 * 
	 * @param chain (X509Certificate[])
	 * @param authType (java.lang.String) 
	 */
	
    @Override
    public void checkClientTrusted(final X509Certificate[] chain,
    		final String authType) {
    	// Not required as client authentication is not required in SESP
    }

    /**
     * Verify the certificate of the Server with that 
     * of locally stored one at the time of SSL handshake
     * 
     * @param certs (java.security.cert.X509Certificate[])
     * @param authType (java.lang.String) 
     * 
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] certs,
            final String authType) {
        try {
        	if(clientCert!=null){
        		for (final X509Certificate cert : certs) {
            		/*
            		 *  Verifying by public key
            		 *  
            		 *  If any certificate is not matched,
            		 *  it would throw exception 
            		 */
            		cert.verify(clientCert.getPublicKey());
            	}
        	}
        } catch (final Exception e) {
        	// Log using DDMS logcat - useful for emulators
			writeLog("CustomX509TrustManager :  " + ApplicationAstSep.context.getString(R.string.checkServerTrusted), e);
        	//Log.e(ApplicationAstSep.context.getString(R.string.checkServerTrusted), e.getMessage(),e);
        	// Log the error first
        	//SespExceptionHandler.logError(Thread.currentThread(), e);
        	// Throw error to tear down the SSL connection
        	throw new IllegalArgumentException(ApplicationAstSep.context.getString(R.string.invalidauthmessage));
        } 
    }

    /**
     * Return accepted issuer certificates 
     * in an array
     * 
     * @return X509Certificate
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
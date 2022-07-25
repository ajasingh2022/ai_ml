/**
 * 
 */
package com.capgemini.sesp.ast.android.module.util;

import android.util.Log;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.security.CustomSSLSocketFactory;
import com.capgemini.sesp.ast.android.module.security.CustomX509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

//import android.annotation.SuppressLint;

/**
 * <p>
 * The purpose of this class to
 * assist and expose utility methods which could be 
 * used <b>while initiating a secured connection </b>
 * over the network
 * </p>
 * 
 * <br>
 * Added for SESPSTD-3075
 * 
 * @author Capgemini
 * @since 1st August, 2014
 * @version 1.0
 * 
 */
//@SuppressLint("DefaultLocale")
public final class SecurityUtil {
	
	
	/**
	 * Creates an http client based on custom trust manager
	 * and SSL socket factory
	 * 
	 * @return (org.apache.http.client.HttpClient) 
	 */
	//@SuppressLint("TrulyRandom")
	private static final HttpClient getHttpsClient(final String sslCertName, final String sslPort) throws Exception{
		
		final SSLContext ctx = SSLContext.getInstance(ApplicationAstSep.context.getString(R.string.ssllayer));
        ctx.init(null, new TrustManager[] { new CustomX509TrustManager(sslCertName) },
        		new SecureRandom());

        final HttpClient client = new DefaultHttpClient();

        final SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        final ClientConnectionManager ccm = client.getConnectionManager();
        final SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme(ApplicationAstSep.context.getString(R.string.sslprotocol), 
        		ssf, Integer.valueOf(sslPort)));
        
        final DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
                client.getParams());

        return sslClient;
    }
	

	/**
	 * The exposed method that determines whether to go 
	 * 
	 * @param serverAddress (String)
	 * @return (org.apache.http.client.HttpClient)
	 * @throws Exception
	 */
	
	public static final HttpClient getClient(final String serverAddress, final String sslCertName, final String sslPort) throws Exception{
		Log.d("getClient-SecurityUtil", "Server address=" + serverAddress);
		HttpClient client = null;
		if(isSSLRequired(serverAddress)){
			client = getHttpsClient(sslCertName,sslPort);
		}else{
			client = new DefaultHttpClient();
		}
		return client;
	}


	/**
	 * The exposed method that determines whether to go
	 *
	 * @param serverAddress (String)
	 * @return (org.apache.http.client.HttpClient)
	 * @throws Exception
	 */

	public static final HttpURLConnection getURLConnection(final String serverAddress, final String sslCertName, final String sslPort) throws Exception{
		Log.d("SecurityUtil","getURLConnection - Server address="+serverAddress);
		URL url = new URL(serverAddress);
		HttpURLConnection urlConnection = null;
		if(isSSLRequired(serverAddress)){
			urlConnection = getHttpsURLConnection(url, sslCertName, sslPort);
		}else{
			urlConnection = (HttpURLConnection)url.openConnection();
		}
		return urlConnection;
	}
	/**
	 * The exposed method that determines whether to go depending on the serverAddress String parameter
	 * Decides whether to make a HTTP call or a HTTPS call
	 * For HTTPS call, the SSL Certificate Name and SSL Port is extracted in this method itself.
	 *
	 * @param serverAddress (String)
	 * @return (org.apache.http.client.HttpClient)
	 * @throws Exception
	 */

	public static final HttpURLConnection getURLConnection(final String serverAddress) throws Exception{
		Log.d("SecurityUtil","getURLConnection(final String serverAddress) - Server address="+serverAddress);
		URL url = new URL(serverAddress);
		HttpURLConnection urlConnection = null;
		if(isSSLRequired(serverAddress)){
			String sslCertName = CommunicationHelper.getSslCertName();
			String sslPort = CommunicationHelper.getSslPort();
			urlConnection = getHttpsURLConnection(url, sslCertName, sslPort);
		}else{
			urlConnection = (HttpURLConnection)url.openConnection();
		}
		return urlConnection;
	}

	/**
	 *  This method returns HttpsUrlConnection Object based on TrustManager and public certificate
	 *
	 */
	private static HttpsURLConnection getHttpsURLConnection(URL url,final String sslCertName, final String sslPort) throws Exception{

		// Load the custom certificate from asset
		InputStream is = ApplicationAstSep.context.getAssets().open(sslCertName);
		// The certificate type would be x509
		final CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// Obtain the public key here that would be used to verify server SSL
		Certificate clientCert =  cf.generateCertificate(is);

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null);
		keyStore.setCertificateEntry("rsp_client", clientCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		final SSLContext ctx = SSLContext.getInstance(ApplicationAstSep.context.getString(R.string.ssllayer));
		ctx.init(null, tmf.getTrustManagers(), null);

		HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
		httpsURLConnection.setSSLSocketFactory(ctx.getSocketFactory());
		httpsURLConnection.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return httpsURLConnection;
	}
	/**
	 *  This method returns HttpsUrlConnection Object based on TrustManager and public certificate
	 *
	 */
	@Deprecated
	private static final HttpURLConnection getHttpURLConnection(URL url) throws Exception{
		HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		httpURLConnection.setRequestMethod("POST");
		return httpURLConnection;
	}

	/**
	 * Check the protocol used for communication
	 * <br>
	 * If it uses "https" then return true
	 * 
	 * @param serverAddress
	 * @return boolean
	 */
	private static boolean isSSLRequired(final String serverAddress){
		Log.d("isSSLRequired","Server address="+serverAddress);
		boolean result = false;
		if(serverAddress!=null && !"".equals(serverAddress)
				&& serverAddress.toLowerCase(Locale.getDefault())
				.startsWith(ApplicationAstSep.context.getString(R.string.sslprotocol))){
			result = true;
		}
		return result;
	}
}

package org.jboss.pressgang.ccms.server.utils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProcessUtilities {
    /**
     * Checks that a server exists at the specified URL by sending a request to get the headers from the URL.
     *
     * @param serverUrl The URL of the server.
     * @return True if the server exists and got a successful response otherwise false.
     */
    public static boolean validateServerExists(final String serverUrl) {
        return validateServerExists(serverUrl, false);
    }

    /**
     * Checks that a server exists at the specified URL by sending a request to get the headers from the URL.
     *
     * @param serverUrl The URL of the server.
     * @param disableSSLCert
     * @return True if the server exists and got a successful response otherwise false.
     */
    public static boolean validateServerExists(final String serverUrl, final boolean disableSSLCert) {
        try {
            if (disableSSLCert) {
                // See http://www.exampledepot.com/egs/javax.net.ssl/TrustAll.html

                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }};

                // Install the all-trusting trust manager
                final SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            }

            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            HttpURLConnection.setFollowRedirects(true);
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_MOVED_PERM || response == HttpURLConnection.HTTP_MOVED_TEMP) {
                return validateServerExists(connection.getHeaderField("Location"), disableSSLCert);
            } else {
                return response == HttpURLConnection.HTTP_OK || response == HttpURLConnection.HTTP_BAD_METHOD;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

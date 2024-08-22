package br.com.alura.screenmatch.config;

import okhttp3.OkHttpClient;
import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

public class SSLConfig {

    public static OkHttpClient createHttpClientWithSsl() {
        try {
            // Cria um TrustManager que confia em todos os certificados
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            // Aceita todos os certificados de cliente
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            // Aceita todos os certificados de servidor
                        }
                    }
            };

            // Cria um SSLContext que usa o TrustManager personalizado
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAll, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAll[0])
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.220.11", 3128))) // Substitua pelo endereço do seu proxy
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Erro ao configurar o SSLContext", e);
        }
    }
}

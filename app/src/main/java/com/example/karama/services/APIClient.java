package com.example.karama.services;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    public static final MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");

    public static final Retrofit getClient(final String BASE_URL) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        HttpLoggingInterceptor logInter = new HttpLoggingInterceptor();


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInter)
                .addInterceptor(interceptor)
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        if (BASE_URL.contains("https:")) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(getUnsafeOkHttpClient().build())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.connectTimeout(180, TimeUnit.SECONDS);
            builder.writeTimeout(180, TimeUnit.SECONDS);
            builder.readTimeout(180, TimeUnit.SECONDS);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class ErrorResponse{
        @SerializedName("error")
        @Expose
        error error;

        public ErrorResponse() {
        }

        public ErrorResponse.error getError() {
            return error;
        }

        public void setError(ErrorResponse.error error) {
            this.error = error;
        }

        public static class error implements Serializable {
            //String extraProperties;
            @SerializedName("code")
            String code;
            @SerializedName("message")
            String message;
            @SerializedName("details")
            String details;
            @SerializedName("validationErrors")
            String validationErrors;

            @Override
            public String toString() {
                return "error{" +
                        "code='" + code + '\'' +
                        ", message='" + message + '\'' +
                        ", details='" + details + '\'' +
                        ", validationErrors='" + validationErrors + '\'' +
                        '}';
            }

            public error() {
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getValidationErrors() {
                return validationErrors;
            }

            public void setValidationErrors(String validationErrors) {
                this.validationErrors = validationErrors;
            }
        }
    }
    public static final Map<String, String> API_HEADER(String token) {
        try {
            Map<String, String> API_HEADER = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("Content-Type", "application/json;charset=UTF-8");
                put("Accept", "application/json");
                put("Cache-Control", " max-age=640000");
            }};
            return API_HEADER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

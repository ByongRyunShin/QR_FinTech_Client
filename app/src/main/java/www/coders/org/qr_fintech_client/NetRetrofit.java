package www.coders.org.qr_fintech_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    public static final String BASE_URL = "http://49.173.132.147:9999/mobile/";
    public static final String ENCRYPT_QR = "http://49.173.132.147:9999/encrypt/";

    private static Retrofit retrofit = null;
    private static RetrofitService endPoints = null;
    private static Retrofit encrypt_qr_retrofit = null;
    private static RetrofitService encrypt_qr_endPoints = null;

    public static RetrofitService getEndPoint(){
        if(endPoints ==null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
            endPoints = retrofit.create(RetrofitService.class);
        }
        return endPoints;
    }

    public static RetrofitService getEncryptQr(){
        if(encrypt_qr_endPoints ==null) {
            encrypt_qr_retrofit = new Retrofit.Builder().baseUrl(ENCRYPT_QR).addConverterFactory(GsonConverterFactory.create())
                    .build();
            encrypt_qr_endPoints = encrypt_qr_retrofit.create(RetrofitService.class);
        }
        return encrypt_qr_endPoints;
    }
}



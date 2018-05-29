package www.coders.org.qr_fintech_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    public static final String BASE_URL = "http://49.173.132.147:9999/mobile/";
    private static Retrofit retrofit = null;
    private static RetrofitService endPoints = null;


    public static RetrofitService getEndPoint(){
        if(endPoints ==null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
            endPoints = retrofit.create(RetrofitService.class);
        }
        return endPoints;
    }
}



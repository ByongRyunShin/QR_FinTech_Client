package www.coders.org.qr_fintech_client;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("user")
    Call<JsonObject> do_login(@Field("id") String id, @Field("pw") String pw);

    @FormUrlEncoded
    @POST("qrcode")
    Call<ResponseBody> do_qrCode(@Field("id") String id, @Field("pw") String pw);

    @FormUrlEncoded
    @POST("gcm_insert")
    Call<JsonObject> do_gcm(@Field("id") String id, @Field("pw") String pw, @Field("token") String token);

    @FormUrlEncoded
    @POST("buy")
    Call<JsonObject> do_buy(@Field("id") String id, @Field("pw") String pw, @Field("pNum") String pNum, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("product_detail")
    Call<JsonObject> do_product_detail(@Field("pNum") String pNum);

    @FormUrlEncoded
    @POST("sell")
    Call<JsonObject> do_sell(@Field("id") String id, @Field("pw") String pw, @Field("num") String num,
                             @Field("pNum") String pNum, @Field("amount") String amount, @Field("cyper_str") String cyper_str);

    @Multipart
    @POST("user_insert")
    Call<JsonObject> do_register(@PartMap() HashMap<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("shop_insert")
    Call<JsonObject> do_shop_insert(@PartMap() HashMap<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("shop_update")
    Call<JsonObject> do_shop_update(@PartMap() HashMap<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("product_insert")
    Call<JsonObject> do_product_insert(@PartMap() HashMap<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @Multipart
    @POST("product_update")
    Call<JsonObject> do_product_update(@PartMap() HashMap<String, RequestBody> partMap, @Part MultipartBody.Part file);

}

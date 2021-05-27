package kr.co.fos.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface HttpInterface {
    public static final String API_URL = "http://222.117.135.101:8090/";
    //리뷰

    @POST("/review")
    Call<ResponseBody> reviewRegister();

    @POST("/bookmark/{no}")
    Call<ResponseBody> bookmarkRegister();

    @DELETE("/bookmark/{no}")
    Call<ResponseBody> bookmarkDelete();

    @PUT("/review/{no}")
    Call<ResponseBody> reviewUpdate();

    //매출
    @GET("/order")
    Call<ResponseBody> orederInquiry();

    //즐겨찾기
    @GET("/bookmark")
    Call<ResponseBody> bookmarkInquiry();
}

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
    // @Query = GET   @Field = POST, PUT, DELETE  @Path = /member/{no}   이런거
    //공통
    @GET("/foodtruck")
    Call<ResponseBody> categorySearch();

    //회원
    @POST("/common/idfind")
    Call<ResponseBody> idCheck();

    @DELETE("/member/{no}")
    Call<ResponseBody> memberDelete();

    @GET("/member/{no}")
    Call<ResponseBody> memberDetailInquiry();

    @PUT("/member/{no}")
    Call<ResponseBody> memberUpdate();

    //푸드트럭
    @POST("/foodtruck")
    Call<ResponseBody> memberRegister();

    @GET("/foodtruck/{no}")
    Call<ResponseBody> foodtruckInquiry();

    @GET("/foodtruck")
    Call<ResponseBody> search();

    @GET("/foodtruck")
    Call<ResponseBody> foodtruckLocationSearch();

    @GET("/foodtruck")
    Call<ResponseBody> mapInquiry();

    @GET("/foodtruck")
    Call<ResponseBody> mapSearch();

    @POST("/foodtruck/{no}/location")
    Call<ResponseBody> startBusiness();

    @GET("/foodtruck/{no}")
    Call<ResponseBody> foodtruckDetailInquiry();

    @PUT("/foodtruck/{no}")
    Call<ResponseBody> foodtruckUpdate();

    //메뉴
    @GET("/menu")
    Call<ResponseBody> menuInquiry();

    @GET("/menu/{no}")
    Call<ResponseBody> menuDetailInquiry();

    //주문
    @PUT("/order/{no}")
    Call<ResponseBody> orderCancleAuth();

    @POST("/order")
    Call<ResponseBody> payment();

    @GET("/order")
    Call<ResponseBody> orderInquiry();

    @PUT("/order/{no}")
    Call<ResponseBody> orderCancle();

    @GET("/order/{no}")
    Call<ResponseBody> orderDetailInquiry();

    @PUT("/order/{no}")
    Call<ResponseBody> orderStatusUpdate();

    //리뷰
    @GET("/review")
    Call<ResponseBody> reviewInquiry();

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

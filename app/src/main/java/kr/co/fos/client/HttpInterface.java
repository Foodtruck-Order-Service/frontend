package kr.co.fos.client;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpInterface {
    public static final String API_URL = "http://222.117.135.101:8090/";
    // @Query = GET   @Field = POST, PUT, DELETE  @Path = /member/{no}   이런거
    //공통
    @GET("/foodtruck")
    Call<ResponseBody> categorySearch(@Query("category") String category);

    //회원
    @FormUrlEncoded
    @POST("/member")
    Call<ResponseBody> memberRegister(
            @Field("id") String id,
            @Field("password") String password,
            @Field("name") String name,
            @Field("rrn") String rrn,
            @Field("email") String email,
            @Field("phone") String phone
            );

    @GET("/member")
    Call<ResponseBody> idCheck(@Query("id") String id);

    @FormUrlEncoded
    @DELETE("/member/{no}")
    Call<ResponseBody> memberDelete(@Path("no") int no);

    @GET("/member")
    Call<ResponseBody> memberInquiry(@Query("name") String name, @Query("rrn") String rrn);

    @GET("/member")
    Call<ResponseBody> memberInquiry(@Query("id") String id);

    @GET("/member/{no}")
    Call<ResponseBody> memberDetailInquiry(@Path("no") int no);

    @FormUrlEncoded
    @PUT("/member/{no}")
    Call<ResponseBody> memberUpdate(
            @Path("no") int no,
            @Field("password") String password,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone
    );

    //푸드트럭
    @Multipart
    @FormUrlEncoded
    @POST("/foodtruck")
    Call<ResponseBody> memberBusinessRegister(
            @Field("id") String id,
            @Field("password") String password,
            @Field("name") String name,
            @Field("rrn") String rrn,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("brn") int brn,
            @Field("foodtruckName") String foodtruckName,
            @Field("category") String category,
            @Field("startTime") String startTime,
            @Field("endTime") String endTime,
            @Field("content") String content,
            @PartMap Map<String, RequestBody> photos
            );

    @GET("/foodtruck")
    Call<ResponseBody> foodtruckInquiry(@Query("name") String name);

    @GET("/foodtruck")
    Call<ResponseBody> foodtruckLocationSearch(@Query("lat") double lat, @Query("lng") double lng);

    @FormUrlEncoded
    @PUT("/foodtruck/{no}")
    Call<ResponseBody> startBusiness(@Path("no") int no, @Field("lat") double lat, @Field("lng") double lng);

    @GET("/foodtruck/{no}")
    Call<ResponseBody> foodtruckDetailInquiry(@Path("no") int no);

    @Multipart
    @FormUrlEncoded
    @PUT("/foodtruck/{no}")
    Call<ResponseBody> foodtruckUpdate(
            @Path("no") int no,
            @Field("brn") int brn,
            @Field("foodtruckName") String foodtruckName,
            @Field("category") String category,
            @Field("startTime") String startTime,
            @Field("endTime") String endTime,
            @Field("content") String content,
            @PartMap Map<String, RequestBody> photos
            );

    //메뉴
    @Multipart
    @FormUrlEncoded
    @POST("/menu")
    Call<ResponseBody> menuRegister(
            @Field("foodtruckNo") int foodtruckNo,
            @Field("name") String name,
            @Field("acount") int acount,
            @Field("cookingTime") String cookingTime,
            @Field("content") String content,
            @FieldMap Map<String, List<Map<String, Integer>>> options,
            @PartMap Map<String, RequestBody> photos
    );

    @GET("/menu")
    Call<ResponseBody> menuInquiry(@Query("no") int no);

    @GET("/menu/{no}")
    Call<ResponseBody> menuDetailInquiry(@Path("no") int no);

    @Multipart
    @FormUrlEncoded
    @PUT("/menu/{no}")
    Call<ResponseBody> menuUpdate(
            @Path("no") int no,
            @Field("name") String name,
            @Field("acount") int acount,
            @Field("cookingTime") String cookingTime,
            @Field("content") String content,
            @FieldMap Map<String, List<Map<String, Integer>>> options,
            @PartMap Map<String, RequestBody> photos
            );

    @DELETE("/menu/{no}")
    Call<ResponseBody> menuDelete(@Path("no") int no);

    //주문
    @PUT("/order/{no}")
    Call<ResponseBody> orderCancleAuth(@Path("no") int no);

    @POST("/order")
    Call<ResponseBody> payment(
            @Field("memberNo") int memberNo,
            @Field("foodtruckNo") int foodtruckNo,
            @Field("totalAmount") int totalAmount,
            @FieldMap Map<List<Map<String, List<Map<String, Integer>>>>,Integer> menus

    );

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

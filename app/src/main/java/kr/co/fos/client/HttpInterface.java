package kr.co.fos.client;

import java.util.List;
import java.util.Map;

import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.member.Member;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.order.BusinessListViewItem;
import kr.co.fos.client.order.ListViewItem;
import kr.co.fos.client.order.Order;
import kr.co.fos.client.review.Review;
import kr.co.fos.client.review.ReviewItem;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpInterface {
    public static final String API_URL = "http://222.117.135.101:8090";
    // @Query = GET   @Field = POST, PUT, DELETE  @Path = /member/{no}   이런거
    //공통
    @GET("/foodtruck")
    Call<ResponseBody> categorySearch(@Query("category") String category);

    //회원
    @POST("/member")
    Call<ResponseBody> memberRegister(@Body Member member);

    @GET("/member")
    Call<ResponseBody> idCheck(@Query("id") String id);

    @DELETE("/member/{no}")
    Call<ResponseBody> memberDelete(@Path("no") int no);

    @GET("/member")
    Call<List<Member>> memberInquiry(@Query("name") String name, @Query("rrn") String rrn);

    @GET("/member")
    Call<List<Member>> memberInquiry(@Query("id") String id);

    //추가
    @GET("/member")
    Call<List<Member>> memberLoginInquiry(@Query("id") String id, @Query("password") String pw);

    @GET("/member/{no}")
    Call<ResponseBody> memberDetailInquiry(@Path("no") int no);

    @PUT("/member/{no}")
    Call<ResponseBody> memberUpdate(@Path("no") int no, @Body Member member);

    //푸드트럭
    @Multipart
    @POST("/foodtruck")
    Call<ResponseBody> memberBusinessRegister(@Part("member") Member member, @Part MultipartBody.Part image);

    @GET("/foodtruck")
    Call<List<Foodtruck>> foodtruckInquiry(@Query("name") String name, @Query("category") String category);

    @GET("/foodtruck/location")
    Call<List<Foodtruck>> foodtruckLocationSearch(@Query("lat") double lat, @Query("lng") double lng);

    @PUT("/foodtruck/{no}/location")
    Call<ResponseBody> startBusiness(@Path("no") int no, @Body Foodtruck foodtruck);

    @GET("/foodtruck/{no}")
    Call<ResponseBody> foodtruckDetailInquiry(@Path("no") int no);

    @PUT("/foodtruck/{no}")
    Call<ResponseBody> foodtruckUpdate(
            @Path("no") int no,
            @Body Foodtruck foodtruck
    );

    //추가
    @GET("/foodtruck/business/{memberNo}")
    Call<ResponseBody> myFoodtruckInquiry(@Path("memberNo") int no);

    //추가
    @GET("/foodtruck/business/{memberNo}")
    Call<ResponseBody> foodtruckBusinessDetailInquiry(@Path("memberNo") int no);
    //메뉴
    @Multipart
    @POST("/menu")
    Call<ResponseBody> menuRegister(@Part("menu") Menu menu, @Part MultipartBody.Part image);

    @GET("/menu")
    Call<List<Menu>> menuInquiry(@Query("foodtruckNo") int no);

    @GET("/menu/{no}")
    Call<ResponseBody> menuDetailInquiry(@Path("no") int no);

    @PUT("/menu/{no}")
    Call<ResponseBody> menuUpdate(
            @Path("no") int no,
            @Body Menu menu
    );

    @DELETE("/menu/{no}")
    Call<ResponseBody> menuDelete(@Path("no") int no);


    //주문
    @PUT("/order/{no}")
    Call<ResponseBody> orderCancleAuth(@Path("no") int no);

    @POST("/order")
    Call<ResponseBody> payment(@Body Order order);
    //추가
    @GET("/order")
    Call<List<ListViewItem>> orderInquiry(@Query("memberNo") int no);

    //추가
    @GET("/order/business")
    Call<List<BusinessListViewItem>> orderBusinessInquiry(@Query("foodtruckNo") int no);

    //추가
    @PUT("/order/{no}")
    Call<ResponseBody> orderCancel(@Path("no") int no, @Body Order Order);

    @GET("/payment/cancel")
    Call<ResponseBody> orderCancel(@Query("no") int no);

    @GET("/order/{no}")
    Call<ResponseBody> orderDetailInquiry(@Path("no") int no);

    @GET("/order/business/{no}")
    Call<ResponseBody> orderBusinessDetailInquiry(@Path("no") int no);

    @PUT("/order/{no}")
    Call<ResponseBody> orderStatusUpdate(@Path("no") int no, @Body Order order);

    //리뷰
    @GET("/review")
    Call<ResponseBody> reviewInquiry(@Query("no") int no);

    @GET("/review/{no}")
    Call<ResponseBody> reviewDetailInquiry(@Path("no") int memberNo, @Query("foodtruckNo")int foodtruckNo);

    @Multipart
    @POST("/review")
    Call<ResponseBody> reviewRegister(@Part("review") Review review, @Part MultipartBody.Part image);

    @PUT("/review/{no}")
    Call<ResponseBody> reviewUpdate(@Path("no") int no, @Body Review review);

    @DELETE("/review/{no}")
    Call<ResponseBody> reviewDelete(@Path("no") int no);

    //즐겨찾기
    @GET("/bookmark")
    Call<ResponseBody> bookmarkInquiry(@Query("memberNo") int memberNo, @Query("foodtruckNo") int foodtruckNo);

    @POST("/bookmark")
    Call<ResponseBody> bookmarkRegister(@Body Bookmark bookmark);

    @HTTP(method = "DELETE", path = "/bookmark", hasBody = true)
    Call<ResponseBody> bookmarkDelete(@Body Bookmark bookmark);

    //사진
    @Multipart
    @POST("/photo")
    Call<ResponseBody> photoRegister(
            @PartMap Map<String, RequestBody> photos
    );

    //이메일
    @POST("/email")
    Call<String> emailCertification(@Body String email);
}

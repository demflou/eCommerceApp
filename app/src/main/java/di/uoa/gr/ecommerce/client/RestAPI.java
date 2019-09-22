package di.uoa.gr.ecommerce.client;


import java.util.List;

import di.uoa.gr.ecommerce.rest.Login;
import di.uoa.gr.ecommerce.rest.Message;
import di.uoa.gr.ecommerce.rest.User;
import di.uoa.gr.ecommerce.rest.myBid;
import di.uoa.gr.ecommerce.rest.myCat;
import di.uoa.gr.ecommerce.rest.myItem;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestAPI {

    /*@GET("users")
    @Headers("Accept: application/json")
    Call<List<User>> getAllUsers();

    @GET("users/{userId}")
    @Headers("Accept: application/json")
    Call<User> getUser( @Path("userId") int userId);

    @POST("images")
    @Multipart
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );
    */

    @PUT("messages/{id}")
    Call<Void> editMsg (@Header("Authorization") String token,@Path("id") Integer id);

    @GET("user/check/{uname}")
    Call<Boolean> check(@Path("uname") String uname);

    @POST("bids")
    Call<Void> bid(@Header("Authorization") String token,@Body myBid bid);

    @PUT("item/{id}")
    Call<Void> startItem (@Header("Authorization") String token,@Path("id") Integer id, @Body myItem item);

    @DELETE("item/{id}")
    Call<Void> deleteItem (@Header("Authorization") String token,@Path("id") Integer id);

    @DELETE("messages/{id}")
    Call<Void> deleteMsg (@Header("Authorization") String token,@Path("id") Integer id);

    @GET("item/{id}")
    Call<myItem> getItem(@Path("id") Integer id);

    @GET("category/ibc/{category}")
    Call<List<myItem>> findByCat(@Path("category") String category);

    @GET("images/item/{id}")
    Call<String> getImage(@Path("id") Integer id);

    @GET("messages/count/{to}")
    Call<Long> countMsgs(@Header("Authorization") String token,@Path("to") String to);

    @GET("category")
    Call<List<myCat>> findAllCats();

    @GET("item/byDesc/{words}")
    Call<List<myItem>> searchDesc (@Path("words") String words);

    @GET("messages/to/{id}")
    Call<List<Message>> getMessagesIn (@Header("Authorization") String token, @Path("id") String id);

    @GET("item/seller/{seller}")
    Call<List<myItem>> getAuctionsbySeller (@Header("Authorization") String token, @Path("seller") String seller);

    @GET("messages/from/{id}")
    Call<List<Message>> getMessagesOut (@Header("Authorization") String token, @Path("id") String id);

    @POST("login/login")
    Call<String> login (@Body Login login);

    @POST("user")
    Call<Integer> register (@Body User user);

    @POST("item")
    Call<Integer> createAuction(@Header("Authorization") String token, @Body myItem item);

    @POST("images")
    Call<Void> postImage(@Header("Authorization") String token, @Body RequestBody body);

    @POST("messages")
    Call<Boolean> createMessage(@Body Message msg, @Header("Authorization") String token);

}

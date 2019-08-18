package di.uoa.gr.ecommerce.client;


import di.uoa.gr.ecommerce.rest.Login;
import di.uoa.gr.ecommerce.rest.User;
import di.uoa.gr.ecommerce.rest.myItem;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
    @POST("login/login")
    Call<String> login (@Body Login login);

    @POST("user")
    Call<Integer> register (@Body User user);

    @POST("item")
    Call<Integer> createAuction(@Header("Authorization") String token, @Body myItem item);

    @POST("images")
    Call<Void> postImage(@Header("Authorization") String token, @Body RequestBody body);
}

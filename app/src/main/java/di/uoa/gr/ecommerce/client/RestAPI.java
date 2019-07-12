package di.uoa.gr.ecommerce.client;


import java.util.List;

import di.uoa.gr.ecommerce.rest.Login;
import di.uoa.gr.ecommerce.rest.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestAPI {

    @GET("users")
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

    @POST("login/login")
    Call<String> login (@Body Login login);

}

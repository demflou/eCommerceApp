package di.uoa.gr.ecommerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    public LoginTask(Context context){
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
//        String retToken = null;
//
//        RestAPI restAPI =
//                RestClient.getStringClient().create(RestAPI.class);
//        Login login = new Login();
//        login.setUsername(params[0]);
//        login.setPassword(params[1].trim());
//        System.out.println(login.getPassword()+ "_"+login.getUsername());
//        Call<String> call = restAPI.login(login);
//        try {
//            Response<String> resp = call.execute();
//            retToken = resp.body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (retToken != null && !retToken.equals("not")) {
//            System.out.println("tokkkkken "+retToken);
//            SharedPreferences.Editor editor = mContext.getSharedPreferences("jwt", MODE_PRIVATE).edit();
//            editor.putString("jwt",retToken);
//            editor.commit();
//            System.out.println(editor.commit()+" coomit true");
//        }
//        return retToken;

        RestAPI restAPI =
                RestClient.getStringClient().create(RestAPI.class);
        Login login = new Login();
        login.setUsername(params[0]);
        login.setPassword(params[1].trim());
        System.out.println(login.getPassword()+ "_"+login.getUsername());
        Call<String> call = restAPI.login(login);
        ((Call)call).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String retToken = response.body();
                if (retToken != null && !retToken.equals("not")) {
                    System.out.println("tokkkkken "+retToken);
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("jwt", MODE_PRIVATE).edit();
                    editor.putString("jwt",retToken);
                    editor.apply();
                    System.out.println(editor.commit()+" coomit true");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String retToken) {
//        if (retToken != null && !retToken.equals("not")) {
//            System.out.println("tokkkkken "+retToken);
//            SharedPreferences.Editor editor = mContext.getSharedPreferences("jwt", MODE_PRIVATE).edit();
//            editor.putString("jwt",retToken);
//            editor.apply();
//            System.out.println(editor.commit()+" coomit true");
//        }
    }
}
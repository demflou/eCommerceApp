package di.uoa.gr.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.Login;
import di.uoa.gr.ecommerce.rest.User;
import retrofit2.Call;
import retrofit2.Response;

class AESCrypt
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(AESCrypt.KEY.getBytes(),AESCrypt.ALGORITHM);
        return key;
    }
}

public class    MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button Signin;
    private Button Signup;
    private Button Guest;

    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    private String token;

    public String getToken() {
        return token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.tusr);
        password = (EditText) findViewById(R.id.tpass);
        Signin = (Button) findViewById(R.id.signinbtn);
        Signup = (Button) findViewById(R.id.signupbtn);
        Guest = (Button) findViewById(R.id.guestbtn);


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LoginTask().execute(username.getText().toString(), AESCrypt.encrypt(password.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(reg);
            }
        });
    }

    /*public void login(View view)
    {
        EditText username = (EditText)findViewById(R.id.editText1);
        EditText password = (EditText)findViewById(R.id.editText2);
        new LoginTask().execute(username.getText().toString(), password.getText().toString());
    }*/

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String retToken = null;

            RestAPI restAPI =
                    RestClient.getStringClient().create(RestAPI.class);
            Login login = new Login();
            login.setUsername(params[0]);
            login.setPassword(params[1]);
            Call<String> call = restAPI.login(login);
            try {
                Response<String> resp = call.execute();
                retToken = resp.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retToken;
        }

        @Override
        protected void onPostExecute(String retToken) {

            MainActivity.this.token = retToken;
            if (token != null && !token.equals("not")) {
                System.out.println(token);
                SharedPreferences.Editor editor = getSharedPreferences("jwt", MODE_PRIVATE).edit();
                editor.putString("jwt",retToken);
                editor.apply();
                Intent login = new Intent (MainActivity.this, HomeActivity.class);
                startActivity(login);
            }
        }

        /*private void validate (String username, String pass) {
            if ((username.equals("Kokos")) && (pass.equals("1234"))) {
                Intent login = new Intent (MainActivity.this, HomeActivity.class);
                startActivity(login);
            }
        }*/
    }
}


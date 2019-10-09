package di.uoa.gr.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.Login;
import di.uoa.gr.ecommerce.rest.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class AESCrypt1
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt1.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt1.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(AESCrypt1.KEY.getBytes(),AESCrypt1.ALGORITHM);
        return key;
    }
}

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText cpassword;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText dob;
    private EditText telephone;
    private EditText afm;
    private EditText address;
    private EditText city;
    private EditText zipcode;
    private EditText country;
    private EditText gloc;
    private Button Register;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pass);
        cpassword = (EditText) findViewById(R.id.cpass);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        dob = (EditText) findViewById(R.id.dob);
        telephone = (EditText) findViewById(R.id.tel);
        afm = (EditText) findViewById(R.id.afm);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        zipcode = (EditText) findViewById(R.id.zipcode);
        country = (EditText) findViewById(R.id.country);
        gloc = (EditText) findViewById(R.id.gloc);
        Register = (Button) findViewById(R.id.regbtn);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0)
                    return;
                RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                Call<Boolean> check = restAPI.check(charSequence.toString().trim());
                check.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(!response.body())
                            Toast.makeText(RegisterActivity.this,"Username already taken",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] params = {username.getText().toString(), password.getText().toString(), cpassword.getText().toString(), name.getText().toString(), surname.getText().toString(), email.getText().toString(),telephone.getText().toString(), afm.getText().toString(), address.getText().toString(), country.getText().toString(), gloc.getText().toString()};

                new RegisterTask().execute(params);
            }
        });
    }

    public class RegisterTask extends AsyncTask<String[], Void, User> {

        @Override
        protected User doInBackground(String[]... params) {
            final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            try {
                if (!AESCrypt.encrypt(params[0][1]).equals(AESCrypt.encrypt(params[0][2]))){
                    RegisterActivity.this.runOnUiThread(new Runnable() { public void run() {
                        Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();}});
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            final User newUser = new User();
            newUser.setUsername(params[0][0]);
            try {
                newUser.setPassword(AESCrypt.encrypt(params[0][1]).trim());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            newUser.setName(params[0][3]);
            newUser.setSurname(params[0][4]);
            newUser.setEmail(params[0][5]);
            newUser.setTelephone(Long.valueOf(params[0][6]));
            newUser.setAfm(Long.valueOf(params[0][7]));
            newUser.setAddress(params[0][8]);
            newUser.setCountry(params[0][9]);
            newUser.setLocation(params[0][10]);
            System.out.println("NUser is "+newUser.toString());
            Call<Integer> call = restAPI.register(newUser);
            ((Call) call).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if(response.isSuccessful()){
                        int res = (Integer) response.body();
                        System.out.println(res);
                        switch (res) {
                            case 0:
                                try {
                                    Login login = new Login();
                                    login.setUsername(newUser.getUsername());
                                    login.setPassword(newUser.getPassword().trim());
                                    System.out.println(login.getPassword()+ "_"+login.getUsername());
                                    restAPI.login(login).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String retToken = response.body();
                                            if (retToken != null && !retToken.equals("not")) {
                                                System.out.println("tokkkkken "+retToken);
                                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE).edit();
                                                editor.putString("jwt",retToken);
                                                editor.apply();
                                                System.out.println(editor.commit()+" coomit true");
                                                Intent login2 = new Intent(getApplicationContext(), Menu.class);
                                                startActivity(login2);
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Wrong Login details", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
                                    String restoredText = prefs.getString("jwt", null);
                                    System.out.println("main jwt on click = "+restoredText);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                Toast.makeText(RegisterActivity.this,"Username already used",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(RegisterActivity.this,"Email already used",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(RegisterActivity.this,"AFM already used",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this,"Unknown error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Unknown error",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,"Server connection error",Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(User newUser) {

        }
    }
}

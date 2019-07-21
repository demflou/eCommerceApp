package di.uoa.gr.ecommerce;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.User;
import retrofit2.Call;
import retrofit2.Response;

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

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] params = {username.getText().toString(), password.getText().toString(), cpassword.getText().toString(), name.getText().toString(), surname.getText().toString(), email.getText().toString(),telephone.getText().toString(), afm.getText().toString(), address.getText().toString(), country.getText().toString(), gloc.getText().toString()};

                new RegisterTask().execute(params);
                //Intent reg = new Intent(RegisterActivity.this, HomeActivity.class);
                //startActivity(reg);
            }
        });
    }

    public class RegisterTask extends AsyncTask<String[], Void, User> {

        @Override
        protected User doInBackground(String[]... params) {
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            try {
                if (!AESCrypt.encrypt(params[0][1]).equals(AESCrypt.encrypt(params[0][2])))
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            User newUser = new User();
            System.out.println("check 1");
            newUser.setUsername(params[0][0]);
            try {
                newUser.setPassword(AESCrypt.encrypt(params[0][1]).trim());
                System.out.println(newUser.getPassword()+" 111111111");
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
            Call<String> call = restAPI.register(newUser);
            try {
                Response<String> resp = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return newUser;
        }

        @Override
        protected void onPostExecute(User newUser) {
            if (newUser!=null) {
                try {

                    new LoginTask(getApplicationContext()).execute(newUser.getUsername(), newUser.getPassword());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("error in post execution of register");
            }
            //        MainActivity.this.token = retToken;
            //        if (token != null && !token.equals("not")) {
            //            System.out.println(token);
            //            SharedPreferences.Editor editor = getSharedPreferences("jwt", MODE_PRIVATE).edit();
            //            editor.putString("jwt",retToken);
            //            editor.apply();
            //            Intent login = new Intent (MainActivity.this, HomeActivity.class);
            //            startActivity(login);
            //        }
        }
    }
}

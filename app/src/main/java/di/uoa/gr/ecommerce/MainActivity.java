package di.uoa.gr.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class    MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button Signin;
    private Button Signup;
    private Button Guest;

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
            public void onClick (View v) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent reg = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(reg);
            }
        });
    }
    private void validate (String username, String pass) {
        if ((username.equals("Kokos")) && (pass.equals("1234"))) {
            Intent login = new Intent (MainActivity.this, HomeActivity.class);
            startActivity(login);
        }
    }
}


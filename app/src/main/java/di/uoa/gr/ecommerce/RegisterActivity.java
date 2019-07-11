package di.uoa.gr.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

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
                Intent reg = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(reg);
            }
        });
    }
}

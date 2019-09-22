package di.uoa.gr.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.fragments.MenuFragment3;
import di.uoa.gr.ecommerce.rest.Message;
import di.uoa.gr.ecommerce.rest.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMsg extends AppCompatActivity {

    private EditText usrto;
    private EditText content;
    private Button send;
    private String jwt;
    private String usrfrom;
    private Toolbar toolbar;
    public String iuto;
    public Integer num;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_msgs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usrto = (EditText) findViewById(R.id.receiver);
        content = (EditText) findViewById(R.id.txtmsg);
        send = (Button) findViewById(R.id.send);

        if(jwt==null) {
            SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
            String restoredText = prefs.getString("jwt", null);
            jwt = restoredText;
        }
        if (jwt!=null) {
            int i = jwt.lastIndexOf('.');
            String withoutSignature = jwt.substring(0, i+1);
            Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
            usrfrom = untrusted.getBody().getSubject().toLowerCase();
        }

        Intent intent = getIntent();
        iuto = intent.getStringExtra(MenuFragment3.EXTRA_TEXT);
        num = intent.getIntExtra(MenuFragment3.EXTRA_NUM, 0);
        System.out.println("---------------------------------iuto = "+ iuto);
        System.out.println("---------------------------------num = "+ num);

        if (num == 4) {
            usrto.setText(iuto);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User ufrom = new User();
                ufrom.setUsername(usrfrom);
                System.out.println(usrfrom+" "+usrto.getText().toString());
                User uto = new User();
                if (num == 4) {
                    uto.setUsername(iuto);
                } else {
                    uto.setUsername(usrto.getText().toString().toLowerCase());
                }
                Message m = new Message();
                m.setFromUserID(ufrom);
                m.setToUserID(uto);
                m.setMessage(content.getText().toString());
                RestAPI ra = RestClient.getStringClient().create(RestAPI.class);
                Call<Boolean> crmsg = ra.createMessage(m, jwt);
                crmsg.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body() == true) {
                            Toast.makeText(NewMsg.this, "Message sent.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if (response.body() == false){
                            Toast.makeText(NewMsg.this, "Cannot contact this user.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(NewMsg.this, "Fail to send message. Try again.", Toast.LENGTH_SHORT).show();}
                });
            }
        });

    }


}

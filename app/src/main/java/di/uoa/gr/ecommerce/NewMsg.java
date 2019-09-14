package di.uoa.gr.ecommerce;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
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
            usrfrom = untrusted.getBody().getSubject();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User ufrom = new User();
                ufrom.setUsername(usrfrom);
                System.out.println(usrfrom+" "+usrto.getText().toString());
                User uto = new User();
                uto.setUsername(usrto.getText().toString());

                Message m = new Message();
                m.setFromUserID(ufrom);
                m.setToUserID(uto);
                m.setMessage(content.getText().toString());
                RestAPI ra = RestClient.getStringClient().create(RestAPI.class);
                Call<Void> crmsg = ra.createMessage(m, jwt);
                crmsg.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println("-------------------TRUE-----------------");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("-------------------FALSE-----------------");
                    }
                });
            }
        });

    }


}

package di.uoa.gr.ecommerce;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        if (restoredText != null) {
            System.out.println("home jwt = "+restoredText);
        }
    }
}

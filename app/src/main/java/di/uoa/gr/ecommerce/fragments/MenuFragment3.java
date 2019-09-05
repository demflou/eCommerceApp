package di.uoa.gr.ecommerce.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import di.uoa.gr.ecommerce.R;
import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.Message;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class MenuFragment3 extends Fragment {
//    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
//            "WebOS","Ubuntu","Windows7","Max OS X"};
    public String jwt ;

    public MenuFragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(jwt==null) {
            SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
            String restoredText = prefs.getString("jwt", null);
            System.out.println("reti jwt on click = " + restoredText);
            jwt = restoredText;
            System.out.println("from auction page 2 jwt = " + jwt);
        }
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<Message>> call = restAPI.getMessagesIn(jwt,untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<Message>>() {
                                 @Override
                                 public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                     System.out.println("SUCCESESEWSE");
                                     String[] mobileArray;
                                     if (!response.isSuccessful()) {
                                         mobileArray = new String[1];
                                         mobileArray[0]="ERROR on Response: " + response.code();
                                     } else {
                                         try {
                                             List<Message> listMessages = response.body();
                                             mobileArray = new String[listMessages.size()];
//                                             mobileArray = listMessages.toArray(new String[0]);
                                             for (int i=0;i< listMessages.size();i++) {
                                                 mobileArray[i]="FROM: "+listMessages.get(i).getFromUserID().getUsername()+"\n Message: "+listMessages.get(i).getMessage();
                                             }
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                             mobileArray = new String[1];
                                             mobileArray[0]="ERROR:" + e.getMessage();
                                         }
                                     }
                                     for (String c : mobileArray){
                                         System.out.println(c);
                                     }
                                     ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
                                             R.layout.test_list_item, mobileArray);

                                     ListView listView = (ListView) getView().findViewById(R.id.InboxList);
                                     listView.setAdapter(adapter);
                                 }

                                 @Override
                                 public void onFailure(Call<List<Message>> call, Throwable t) {
                                     System.out.println("FAILUERRERERE");
                                     String[] mobileArray;
                                     mobileArray = new String[1];
                                     mobileArray[0]=t.fillInStackTrace()+ " ERROR on Failure: " + t.getMessage();
                                     for (String c : mobileArray){
                                         System.out.println(c);
                                     }
                                     ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
                                         R.layout.test_list_item, mobileArray);
                                     ListView listView = (ListView) getView().findViewById(R.id.InboxList);
                                     listView.setAdapter(adapter);
                                 }
                             });
    }

}
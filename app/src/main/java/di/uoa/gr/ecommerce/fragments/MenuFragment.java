package di.uoa.gr.ecommerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import java.util.List;

import di.uoa.gr.ecommerce.HomeActivity;
import di.uoa.gr.ecommerce.R;
import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.myItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuFragment extends Fragment {
    public String jwt;

    public MenuFragment() {
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
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AppCompatImageButton newAuction = (AppCompatImageButton) view.findViewById(R.id.fab2);
        newAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(requireContext(), HomeActivity.class);
                startActivity(reg);
            }
        });
//        if(jwt==null) {
//            SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
//            String restoredText = prefs.getString("jwt", null);
//            System.out.println("reti jwt on click = " + restoredText);
//            jwt = restoredText;
//            System.out.println("from auction page 2 jwt = " + jwt);
//        }
//        int i = jwt.lastIndexOf('.');
//        String withoutSignature = jwt.substring(0, i+1);
//        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
//        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
//        Call<List<myItem>> call = restAPI.getAuctionsbySeller(jwt,untrusted.getBody().getSubject());
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<myItem>> call = restAPI.getAuctionsbySeller(jwt,"Kyriakos");
        call.enqueue(new Callback<List<myItem>>() {
            @Override
            public void onResponse(Call<List<myItem>> call, Response<List<myItem>> response) {
                System.out.println("SUCCESESEWSE");
                String[] mobileArray;
                if (!response.isSuccessful()) {
                    mobileArray = new String[1];
                    mobileArray[0]="ERROR on Response: " + response.code();
                } else {
                    try {
                        List<myItem> listMessages = response.body();
                        mobileArray = new String[listMessages.size()];
//                                             mobileArray = listMessages.toArray(new String[0]);
                        for (int i=0;i< listMessages.size();i++) {
                            mobileArray[i]=listMessages.get(i).getName();
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

                ListView listView = (ListView) getView().findViewById(R.id.myAuctionsList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<myItem>> call, Throwable t) {
                System.out.println("FAILUERRERERE");
                String[] mobileArray;
                mobileArray = new String[1];
                mobileArray[0]=t.fillInStackTrace()+ " ERROR on Failure: " + t.getMessage();
                for (String c : mobileArray){
                    System.out.println(c);
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
                        R.layout.test_list_item, mobileArray);
                ListView listView = (ListView) getView().findViewById(R.id.myAuctionsList);
                listView.setAdapter(adapter);
            }
        });
    }
}
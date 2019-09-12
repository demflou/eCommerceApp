package di.uoa.gr.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import di.uoa.gr.ecommerce.HomeActivity;
import di.uoa.gr.ecommerce.ItemAdapter;
import di.uoa.gr.ecommerce.R;
import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.myItem;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


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

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AppCompatImageButton newAuction = (AppCompatImageButton) view.findViewById(R.id.fab2);
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.myAuctionsList);
        final List<myItem> list = null;
        ItemAdapter adapter = new ItemAdapter(requireContext(),list);
        LinearLayoutManager llm=new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        newAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(requireContext(), HomeActivity.class);
                startActivity(reg);
            }
        });
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
        Call<List<myItem>> call = restAPI.getAuctionsbySeller(jwt,untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<myItem>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<myItem>> call, final Response<List<myItem>> response) {
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
//                            list.add(listMessages.get(i));
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
//                ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
//                        R.layout.test_list_item, mobileArray);
//
//                ListView listView = (ListView) getView().findViewById(R.id.myAuctionsList);
                ItemAdapter adapter = (ItemAdapter) listView.getAdapter();
                adapter.insert(response.body());
                System.out.println(adapter.getItemCount()+ " is size");
                adapter.setOnItemClickListener(new ItemAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if(response.body()!=null)
                            System.out.println("onItemClick position: " + response.body().get(position).getId());
                    }

//                    @Override
//                    public void onItemLongClick(int position, View v) {
//                        Log.d(TAG, "onItemLongClick pos = " + position);
//                    }
                });
                adapter.notifyDataSetChanged();
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
                ItemAdapter adapter = (ItemAdapter) listView.getAdapter();
                adapter.insert(null);
            }
        });
    }
}
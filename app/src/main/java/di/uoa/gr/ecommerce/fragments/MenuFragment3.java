package di.uoa.gr.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import di.uoa.gr.ecommerce.Menu;
import di.uoa.gr.ecommerce.MessageAdapter;
import di.uoa.gr.ecommerce.NewMsg;
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
    public String uto;
    public Integer mnum;
    public static final String EXTRA_TEXT = "di.uoa.gr.ecommerce.NewMsg.EXTRA_TEXT";
    public static final String EXTRA_NUM = "di.uoa.gr.ecommerce.NewMsg.EXTRA_NUM";

    public void showMenu(View v)
    {
        PopupMenu popup = new PopupMenu(requireContext(),v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.i1:
                        Toast.makeText (getActivity(), "Reply to message", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), NewMsg.class);
                        intent.putExtra (EXTRA_TEXT, uto);
                        intent.putExtra (EXTRA_NUM, 4);
                        startActivity(intent);
                        return true;
                    case R.id.i2:
                        final RestAPI ra2 = RestClient.getStringClient().create(RestAPI.class);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setTitle("Delete Message")
                                .setMessage("Are you sure you want to delete this message?");
                        final AlertDialog dialog = builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("Delete button clicked"+mnum);
                                Call<Void> calldel=ra2.deleteMsg(jwt,mnum);
                                calldel.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setTitle("Message Deleted");
                                        final AlertDialog dialog = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                System.out.println("OK");
                                                Toast.makeText (getActivity(), "Message Deleted", Toast.LENGTH_SHORT).show();
                                            }

                                        }).create();
//2. now setup to change color of the button
                                        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface arg0) {
//                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);
//                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);
                                            }
                                        });

                                        dialog.show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }

                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("Cancel button clicked"+mnum);
                            }
                        }).create();
//2. now setup to change color of the button
                        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);
//                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);
                            }
                        });

                        dialog.show();
                        return true;
                    case R.id.i3:
                        RestAPI ra3 = RestClient.getStringClient().create(RestAPI.class);
                        System.out.println("------------------------------------------------"+jwt);
                        Call<Void> mas=ra3.editMsg(jwt,mnum);
                        mas.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {

                                                Toast.makeText(getActivity(), "Message marked as seen", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        //mark as seen
                        return true;

                }
                return false;
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.msg_menu, popup.getMenu());
        popup.show();
    }

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
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    public void onResume(){
        View view = getView();
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
                                 @SuppressLint("WrongConstant")
                                 @Override
                                 public void onResponse(Call<List<Message>> call, final Response<List<Message>> response) {
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
                                     RecyclerView listView = getView().findViewById(R.id.InboxList);
                                     MessageAdapter adapter = new MessageAdapter(requireContext(), response.body());
                                     LinearLayoutManager llm=new LinearLayoutManager(requireContext());
                                     llm.setOrientation(LinearLayoutManager.VERTICAL);
                                     listView.setLayoutManager(llm);
                                     listView.setAdapter(adapter);
                                     DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requireContext(),
                                             DividerItemDecoration.VERTICAL);
                                     listView.addItemDecoration(mDividerItemDecoration);
                                     adapter.setOnItemClickListener(new MessageAdapter.ClickListener() {
                                            @Override
                                            public void onItemClick(int position, View v) {
                                                if(response.body()!=null) {
                                                    uto = response.body().get(position).getFromUserID().getUsername();
                                                    mnum = response.body().get(position).getId();
                                                    showMenu(v);
//                                                    System.out.println("onItemClick position: " + response.body().get(position).getId());
                                                }
                                            }

                                            @Override
                                            public void onItemLongClick(int position, View v) {
                                                if(response.body()!=null)
                                                    System.out.println("onItemLongClick position: " + response.body().get(position).getId());
                                            }
                                        });
                                     listView.setAdapter( adapter);
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
    super.onResume();
    }

}
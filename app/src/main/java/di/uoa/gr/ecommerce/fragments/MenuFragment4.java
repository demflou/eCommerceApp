package di.uoa.gr.ecommerce.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import java.util.List;

import di.uoa.gr.ecommerce.Menu;
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


public class MenuFragment4 extends Fragment {
    public String jwt ;

    public MenuFragment4() {
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
        return inflater.inflate(R.layout.fragment_menu4, container, false);
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
        AppCompatImageButton Compose = getView().findViewById(R.id.newmsg);
        Compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(requireContext(), NewMsg.class);
                startActivity(msg);}});

        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        final ListView listView = (ListView) getView().findViewById(R.id.OutboxList);
        Call<List<Message>> call = restAPI.getMessagesOut(jwt,untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<Message>>() {
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
                            mobileArray[i]="ΤΟ: "+listMessages.get(i).getToUserID().getUsername()+"\n Message: "+listMessages.get(i).getMessage();
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                                final Message o = response.body().get(position);
                                /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                System.out.println("Yes button clicked");
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                System.out.println("No button clicked");
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                AlertDialog dialog = builder.setTitle("Delete Message").setMessage("Are you sure?").setPositiveButton("Delete", dialogClickListener)
                                        .setNegativeButton("Cancel", dialogClickListener).create();
//                                dialog.getButton(0).setBackgroundColor(Color.RED);
//                                dialog.getButton(1).setBackgroundColor(Color.GREEN);*/
                                final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setTitle("Delete Message")
                                        .setMessage("Are you sure you want to delete this message?");
                                final AlertDialog dialog = builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        System.out.println("Delete button clicked"+o.getId());
                                        Call<Void> calldel=restAPI.deleteMsg(jwt,o.getId());
                                        calldel.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setTitle("Message Deleted");
                                                final AlertDialog dialog = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        System.out.println("OK");
                                                        Intent intent = new Intent(requireContext(), Menu.class);
                                                        startActivity(intent);
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
                                        System.out.println("Cancel button clicked"+o.getId());
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
                                System.out.println(o.getMessage());
                            }
                        });
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
                ListView listView = (ListView) getView().findViewById(R.id.OutboxList);
                listView.setAdapter(adapter);
            }
        });
    }
}
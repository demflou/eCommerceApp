package di.uoa.gr.ecommerce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.myBid;
import di.uoa.gr.ecommerce.rest.myCat;
import di.uoa.gr.ecommerce.rest.myItem;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAuction  extends AppCompatActivity {

    private String jwt;
    private Toolbar toolbar;
    private TextView StartPrice;
    private TextView CurrPrice;
    private TextView FromDate;
    private TextView EndDate;
    private TextView Categories;
    private TextView Desc;
    private TextView Location;
    private TextView Country;
    private TextView Seller;
    private TextView BidsNo;
    private ListView listView;
    private ImageView image;
    private ImageButton delete;
    private ImageButton start;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_auction);
        delete = findViewById(R.id.DeleteAuction);
        start = findViewById(R.id.startAuction);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        StartPrice= (TextView) findViewById(R.id.startPriceView);
        CurrPrice= (TextView) findViewById(R.id.CurrentPriceView);
        FromDate= (TextView) findViewById(R.id.FromDateView);
        EndDate= (TextView) findViewById(R.id.EndDateView);
        Categories= (TextView) findViewById(R.id.CategoriesView);
        Desc= (TextView) findViewById(R.id.DescrView);
        Location= (TextView) findViewById(R.id.itemLocationView);
        Country= (TextView) findViewById(R.id.itemCountryView);
        Seller = (TextView) findViewById(R.id.sellerView);
        BidsNo= (TextView) findViewById(R.id.numOfBidsView);
        listView = (ListView) findViewById(R.id.bidsView);
        image = (ImageView) findViewById(R.id.imageView4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        final String restoredText = prefs.getString("jwt", null);
        System.out.println("reti jwt on click = " + restoredText);
        jwt = restoredText;
        int i = jwt.lastIndexOf('.');
        final Context mContext=getApplicationContext();
        String withoutSignature = jwt.substring(0, i+1);
        final Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        final Integer id =getIntent().getIntExtra("ItemID",-1);
        System.out.println(id+" is id");
        final myItem[] item = new myItem[1];
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        final Call<myItem> call = restAPI.getItem(id);
        call.enqueue(new Callback<myItem>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<myItem> call, Response<myItem> response) {
                System.out.println("RESPONSE");
                item[0] =response.body();
                toolbar.setTitle(response.body().getName());
                if(response.body().getStartDate()!=null||(response.body().getNumofbids()!=null&&response.body().getNumofbids()>0))
                    delete.setVisibility(ImageView.GONE);
                StartPrice.setText(Html.fromHtml("<b>Starting Price: </b>EUR " +response.body().getFirstBid().toString()));
                if(response.body().getCurrentPrice()==null)
                    CurrPrice.setText(Html.fromHtml("<b>Current Price:</b> -"));
                else
                    CurrPrice.setText(Html.fromHtml("<b>Current Price:</b> EUR "+response.body().getCurrentPrice().toString()));
                if(response.body().getStartDate()==null)
                    FromDate.setText(Html.fromHtml("<b>Started on:</b> -"));
                else {
                    start.setVisibility(ImageView.GONE);
                    FromDate.setText(Html.fromHtml("<b>Started on:</b> " + response.body().getStartDate().toString()));
                }
                if(response.body().getEndDate()==null)
                    EndDate.setText(Html.fromHtml("<b>Ending on:</b> -"));
                else {
                    start.setVisibility(ImageView.GONE);
                    EndDate.setText(Html.fromHtml("<b>Ending on:</b> " + response.body().getEndDate().toString()));
                }
                Desc.setText(Html.fromHtml("<b>Description:</b> "+response.body().getDescription()));
                Location.setText(Html.fromHtml("<b>Location:</b> "+response.body().getLocation()));
                Country.setText(Html.fromHtml("<b>Country:</b> "+response.body().getCountry()));
                Seller.setText(Html.fromHtml("<b>Seller:</b> "+response.body().getSellerID().getUsername()+" <b>Rating:</b> "+response.body().getSellerID().getRating()+"/10"));
                BidsNo.setText(Html.fromHtml("<b>"+BidsNo.getText().toString()+"</b> "+response.body().getNumofbids()));
                ArrayList<String> bids=new ArrayList<>();
                for(myBid bid:response.body().getBidsCollection())
                    bids.add(bid.getBidsPK().getBidderID()+"     EUR "+bid.getAmount());
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.test_list_item, bids);
                listView.setAdapter(adapter);
                String categories="";
                for(myCat cat:response.body().getCategoryCollection())
                    categories+=cat.getName()+"\n";
                Categories.setText(Html.fromHtml("<b>Categories:</b>\n"+categories));
                Call<String> callimage = restAPI.getImage(id);
                callimage.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body() != null) {
                            byte[] encodeByte = Base64.decode(response.body(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            image.setImageBitmap(bitmap);
                        } else {
                            System.out.println(id + " IMAGE IS NULL");
                            image.setImageResource(R.drawable.no_image_available);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<myItem> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAuction.this).setTitle("Delete Auction")
                        .setMessage("Are you sure you want to delete "+toolbar.getTitle()+"?");
                final AlertDialog dialog = builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Delete button clicked");
                        Call<Void> calldel=restAPI.deleteItem(jwt,id);
                        calldel.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAuction.this).setTitle("Auction Deleted");
                                final AlertDialog dialog = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
//                                        System.out.println("OK");
//                                        Intent intent = new Intent(ViewAuction.this, Menu.class);
//                                        startActivity(intent);
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
                        System.out.println("Cancel button clicked");
                    }
                }).create();
                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);
                    }
                });
                dialog.show();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(ViewAuction.this).create();
                View dialog_layout = getLayoutInflater().inflate(R.layout.design_layout, null);
                // Create the text field in the alert dialog...
                final EditText starting= (EditText) dialog_layout.findViewById(R.id.text1);
                final EditText ending = (EditText) dialog_layout.findViewById(R.id.text2);
                Button startbtn = dialog_layout.findViewById(R.id.button2);
                Button cancelbtn = dialog_layout.findViewById(R.id.button3);
                alertDialog.setCancelable(true);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                startbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date sDate = null;
                        Date eDate = null;
                        try {
                            sDate = sdf.parse(starting.getText().toString());
                            eDate=sdf.parse(ending.getText().toString());
                            if(eDate.before(sDate)||eDate.before(new Date())||sDate.before(new Date()))
                                Toast.makeText(ViewAuction.this,"Please provide correct dates",Toast.LENGTH_SHORT).show();
                            else {
                                for(myCat c:item[0].getCategoryCollection())
                                    System.out.println("Cat is "+c.getName());
                                item[0].setStartDate(sDate);
                                item[0].setEndDate(eDate);
                                Call<Void> callstart = restAPI.startItem(jwt, id, item[0]);
                                callstart.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        System.out.println("STARTED");
                                        alertDialog.cancel();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                                finish();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewAuction.this,"Please provide correct dates",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.setView(dialog_layout);
                alertDialog.show();
            }
        });
    }
}

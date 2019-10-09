package di.uoa.gr.ecommerce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.BidsPK;
import di.uoa.gr.ecommerce.rest.User;
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

public class BidAuction extends AppCompatActivity {

    private Button bidBtn;
    private String jwt;
    private Toolbar toolbar;
    private EditText bidAmount;
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
    private LinearLayout bidLayout;
    private ImageView image;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_auction);
        bidLayout = findViewById(R.id.linLay);
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
        bidAmount=findViewById(R.id.myBid);
        bidBtn = findViewById(R.id.button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED BACK");
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        final String restoredText = prefs.getString("jwt", null);
        Context mContext;
        System.out.println("reti jwt on click = " + restoredText);
        jwt = restoredText;
        final Integer id =getIntent().getIntExtra("ItemID",-1);
        System.out.println(id+" is id");
        final myItem[] item = new myItem[1];
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        final Call<myItem> call = restAPI.getItem(id);
        call.enqueue(new Callback<myItem>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<myItem> call, final Response<myItem> response) {
                System.out.println("RESPONSE");
                item[0] = response.body();
                toolbar.setTitle(response.body().getName());
                String withoutSignature;
                int i;
                String userIn = null;
                Jwt<Header, Claims> untrusted = null;
                if (jwt != null) {
                    i = jwt.lastIndexOf('.');
                    withoutSignature = jwt.substring(0, i + 1);
                    untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
                    userIn = untrusted.getBody().getSubject();
                }
                System.out.println(userIn);
                if (userIn != null && userIn.trim().equals(response.body().getSellerID().getUsername().trim()))
                    bidLayout.setVisibility(LinearLayout.GONE);
                StartPrice.setText(Html.fromHtml("<b>Starting Price: </b>EUR " + response.body().getFirstBid().toString()));
                if (response.body().getCurrentPrice() == null)
                    CurrPrice.setText(Html.fromHtml("<b>Current Price:</b> -"));
                else
                    CurrPrice.setText(Html.fromHtml("<b>Current Price:</b> EUR " + response.body().getCurrentPrice().toString()));
                if (response.body().getStartDate() == null)
                    FromDate.setText(Html.fromHtml("<b>Started on:</b> -"));
                else {
                    FromDate.setText(Html.fromHtml("<b>Started on:</b> " + response.body().getStartDate().toString()));
                }
                if (response.body().getEndDate() == null)
                    EndDate.setText(Html.fromHtml("<b>Ending on:</b> -"));
                else {
                    EndDate.setText(Html.fromHtml("<b>Ending on:</b> " + response.body().getEndDate().toString()));
                }
                Date today = new Date();
                System.out.println(today);
                if (jwt == null || (response.body().getStartDate() == null || response.body().getStartDate().after(today)) || (response.body().getEndDate() == null || response.body().getEndDate().before(today)))
                    bidLayout.setVisibility(LinearLayout.GONE);
                Desc.setText(Html.fromHtml("<b>Description:</b> " + response.body().getDescription()));
                Location.setText(Html.fromHtml("<b>Location:</b> " + response.body().getLocation()));
                Country.setText(Html.fromHtml("<b>Country:</b> " + response.body().getCountry()));
                Seller.setText(Html.fromHtml("<b>Seller:</b> " + response.body().getSellerID().getUsername() + " <b>Rating:</b> " + response.body().getSellerID().getRating() + "/10"));
                BidsNo.setText(Html.fromHtml("<b>" + BidsNo.getText().toString() + "</b> " + response.body().getNumofbids()));
                ArrayList<String> bids = new ArrayList<>();
                for (myBid bid : response.body().getBidsCollection())
                    bids.add(bid.getBidsPK().getBidderID() + "     EUR " + bid.getAmount());
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.test_list_item, bids);
                listView.setAdapter(adapter);
                String categories = "";
                for (myCat cat : response.body().getCategoryCollection())
                    categories += cat.getName() + "\n";
                Categories.setText(Html.fromHtml("<b>Categories:</b>\n" + categories));
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
                final String finalUserIn = userIn;
                bidAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 0) {
                            bidBtn.setEnabled(false);
                            return;
                        }
                        if(CurrPrice.getText().toString().equals("Current Price: -")) {
                            if (Float.compare(Float.valueOf(charSequence.toString()), Float.valueOf(StartPrice.getText().toString().substring(19))) <= 0) {
                                Toast.makeText(BidAuction.this, "Amount should be greater than the starting price", Toast.LENGTH_SHORT).show();
                                bidBtn.setEnabled(false);
                            } else
                                bidBtn.setEnabled(true);
                        }
                        else if (Float.compare(Float.valueOf(charSequence.toString()), Float.valueOf(CurrPrice.getText().toString().substring(19))) <= 0) {
                            Toast.makeText(BidAuction.this, "Amount should be greater than the current price", Toast.LENGTH_SHORT).show();
                            bidBtn.setEnabled(false);
                        } else
                            bidBtn.setEnabled(true);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                bidBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    final myBid bid = new myBid();
                    BidsPK bidsPK = new BidsPK();
                    bidsPK.setBidderID(finalUserIn);
                    bidsPK.setDateTime(new Date());
                    bidsPK.setItemID(response.body().getId());
                    bid.setAmount(Float.valueOf(bidAmount.getText().toString()));
                    myItem item = new myItem();
                    item.setId(response.body().getId());
                    bid.setItem(item);
                    bid.setBidsPK(bidsPK);
                    User bidder = new User();
                    bidder.setUsername(finalUserIn);
                    bid.setUser(bidder);
                    final myBid fBid = bid;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(BidAuction.this).setTitle("Submit your bid ")
                            .setMessage("Are you sure you want to bid for this item?");
                    final AlertDialog dialog = builder.setPositiveButton("BID", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Call<Void> call = restAPI.bid(jwt, fBid);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(BidAuction.this, "Bid was made successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(BidAuction.this, "Bid was not made", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create();
                    dialog.show();
                    }
                });
            }
            @Override
            public void onFailure(Call<myItem> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
}

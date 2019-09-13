package di.uoa.gr.ecommerce;

/*public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        if (restoredText != null) {
            System.out.println("home jwt = "+restoredText);
        }
    }
}
*/

/*
public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload;
    public static final String TAG = "myLogs";

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.app_name));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);

        listView = findViewById(R.id.listView);
        mProgressBar = findViewById(R.id.progressBar);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
//                if (askForPermission())
                    showChooser();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {// If the file selection was successful
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> arrayList = null;
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        currentItem = currentItem + 1;
                        Log.d("Uri Selected", imageUri.toString());
                        try {
                            // Get the file path from the URI
                            String path = FileUtils.getPath(this, imageUri);
                            Log.d("Multiple File Selected", path);
                            arrayList.add(imageUri);
                            MyAdapter mAdapter = new MyAdapter(HomeActivity.this, arrayList);
                            listView.setAdapter(mAdapter);
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                } else if (data.getData() != null) {
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    final Uri uri = data.getData();
                    Log.i(TAG, "Uri = " + uri.toString());
                    try {
                        // Get the file path from the URI
                        final String path = FileUtils.getPath(this, uri);
                        Log.d("Single File Selected", path);
                        arrayList.add(uri);
                        MyAdapter mAdapter = new MyAdapter(HomeActivity.this, arrayList);
                        listView.setAdapter(mAdapter);
                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.User;
import di.uoa.gr.ecommerce.rest.myCat;
import di.uoa.gr.ecommerce.rest.myItem;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageView imageToUpload;
    Button bUI;
    EditText iName;
    EditText iStPrice;
    EditText iDescr;
    EditText iCategories;
    EditText iCountry;
    EditText iLocation;

    private static final int RESULT_LOAD_IMAGE = 1;
    public String jwt ;

//    @Override
//    protected void onDestroy() {
//        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
//        prefs.edit().clear().commit();
//        System.out.println("DESTROYED");
//        super.onDestroy();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);
        toolbar = (Toolbar) findViewById(R.id.toolbarshow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(jwt==null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
            String restoredText = preferences.getString("jwt", null);
            jwt = restoredText;
        }
        System.out.println("from auction page jwt = "+jwt);
//        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
//        String restoredText = prefs.getString("jwt", null);
//        jwt = restoredText;
//        System.out.println("from auction page jwt = "+jwt);

        iName=(EditText)findViewById(R.id.NameItem);
        iStPrice=(EditText)findViewById(R.id.StartingPrice);
        iDescr=(EditText)findViewById(R.id.Description);
        iCategories=(EditText)findViewById(R.id.Categories);
        iCountry=(EditText)findViewById(R.id.itemCountry);
        iLocation=(EditText)findViewById(R.id.itemLocation);
        imageToUpload = (ImageView) findViewById(R.id.ImageToUL);
        bUI=(Button)findViewById(R.id.postItem);
        imageToUpload.setOnClickListener(this);
        bUI.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null ){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    @Override
    public void onBackPressed(){
        System.out.println("BACKK");
        finish();
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.ImageToUL:
                 Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                 break;
             case R.id.postItem:
                 myItem newItem = new myItem();
                 newItem.setCountry(iCountry.getText().toString());
                 newItem.setDescription(iDescr.getText().toString());
                 newItem.setLocation(iLocation.getText().toString());
                 newItem.setName(iName.getText().toString());
                 newItem.setNumofbids(0);
                 newItem.setFirstBid(Float.parseFloat(iStPrice.getText().toString()));
                 Set<myCat> categoryCollection = new HashSet<myCat>();
                 StringTokenizer st1= new StringTokenizer(iCategories.getText().toString(),",");
                 while (st1.hasMoreTokens()) {
                     String cat = st1.nextToken().trim();
                     if (!cat.isEmpty()) {
                         System.out.println("Cat is " + cat+"!");
                         categoryCollection.add(new myCat(cat));
                     }
                 }
                 for(myCat tmyCat:categoryCollection){
                     System.out.println(tmyCat.getName()+"!");
                 }
                 System.out.println(categoryCollection.size());
                 newItem.setCategoryCollection(categoryCollection);
                 if(jwt==null) {
                     SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
                     String restoredText = prefs.getString("jwt", null);
                     System.out.println("reti jwt on click = " + restoredText);
                     jwt = restoredText;
                     System.out.println("from auction page 2 jwt = " + jwt);
                 }
                 User tUser = new User();
                 int i = jwt.lastIndexOf('.');
                 String withoutSignature = jwt.substring(0, i+1);
                 Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
                 tUser.setUsername(untrusted.getBody().getSubject());
                 newItem.setSellerID(tUser);
                 BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageToUpload.getDrawable());
                 Bitmap bitmap;
                 if(bitmapDrawable!=null) {
                     bitmap = bitmapDrawable.getBitmap();
                     ByteArrayOutputStream stream = new ByteArrayOutputStream();
                     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                     String imageInByte = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                     new RegisterTaskItem2(newItem,imageInByte).execute();
                 }
                 else
                     new RegisterTaskItem2(newItem,null).execute();

//                 EditText iCategories;

//                 new RegisterTaskItem(imageInByte,3,4).execute();
                 break;
         }
    }

    public class RegisterTaskItem extends AsyncTask<Void, Void, Void> {

        String image;
        Integer itemID;
        Integer imageID;

        public RegisterTaskItem(String im,Integer itID) {
            this.image=im;
            this.itemID=itID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            RequestBody reqBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("image",this.image)
                    .addFormDataPart("data","{ \"item\":{\"id\":" + itemID+"}}")
                    .build();

            Call<Void> call = restAPI.postImage(jwt.trim(),reqBody);
            ((Call)call).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(HomeActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(HomeActivity.this,"Unknown client error on image",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(HomeActivity.this,"Server error on image", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent login2 = new Intent(getApplicationContext(), Menu.class);
            startActivity(login2);
            super.onPostExecute(aVoid);
//            Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
        }
    }

    public class RegisterTaskItem2 extends AsyncTask<Void, Void, Void> {
        String image;
        myItem item;

        public RegisterTaskItem2(myItem item,String image) {
            this.image=image;
            this.item=item;

        }

        @Override
        protected Void doInBackground(Void... params) {
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            Call<Integer> call = restAPI.createAuction(jwt.trim(),item);
            ((Call)call).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Auction Created",Toast.LENGTH_SHORT).show();
                        if(image!=null)
                            new RegisterTaskItem(image,response.body()).execute();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Auction not created due to client error",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Auction not created due to server error",Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(image==null) {
                Intent login2 = new Intent(getApplicationContext(), Menu.class);
                startActivity(login2);
                finish();
            }
            super.onPostExecute(aVoid);
//            Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
        }
    }

//    private HttpParams getHttpReqParams(){
//        HttpParams httpReqParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpReqParams,1000*30);
//        HttpConnectionParams.setSoTimeout(httpReqParams,1000*30);
//        return httpReqParams;
//    }
}
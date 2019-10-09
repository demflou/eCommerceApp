package di.uoa.gr.ecommerce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.myItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> implements Filterable {

    private List<myItem> mDataset;
    private List<myItem> mDatasetFull;
    private Context mContext;
    private static ClickListener clickListener;

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView price;
        public TextView ends;
        public ImageView image;
        public Integer id;

        public ItemHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(this);
            title = (TextView) v.findViewById(R.id.ItemTitle);
            price = (TextView) v.findViewById(R.id.ItemPrice);
            ends = (TextView) v.findViewById(R.id.ItemEnds);
            image = (ImageView) v.findViewById(R.id.ItemImage);
        }

        @Override
        public void onClick(View v) {
            System.out.println("on click "+id);
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        private void setItemDetails(final myItem item) {
            if (item == null)
                return;
            id=item.getId();
            title.setText(item.getName());
            String tprice;
            if (item.getCurrentPrice() != null)
                tprice = "FROM:\n" + item.getCurrentPrice() + " EUR";
            else
                tprice = "FROM:\n" + item.getFirstBid() + " EUR";
            price.setText(tprice);
            String tends;
            if (item.getEndDate() != null)
                tends = "UNTIL:\n" + item.getEndDate().toString();
            else
                tends = "Not yet started";
            ends.setText(tends);
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            Call<String> call = restAPI.getImage(item.getId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        byte[] encodeByte = Base64.decode(response.body(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        image.setImageBitmap(bitmap);
                    } else {
                        System.out.println(item.getId() + " IMAGE IS NULL");
                        image.setImageResource(R.drawable.no_image_available);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println("FAILURE ID id " + item.getId());
                    System.out.println(t.getMessage());
                    System.out.println(t.fillInStackTrace());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ItemAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void insert(List<myItem> myD) {
        if(myD==null) {

        }
        else {
            mDataset = myD;
            mDatasetFull = new ArrayList<>(myD);
            notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(Context c, List<myItem> myDataset) {
        if(myDataset!=null) {
            mDataset = myDataset;
            mDatasetFull = new ArrayList<>(myDataset);
        }
        else{
            mDataset = myDataset;
            mDatasetFull = new ArrayList<>();
        }

        mContext = c;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<myItem> filteredList = new ArrayList<>();
            if (charSequence==null || charSequence.length()==0){
                filteredList.addAll(mDatasetFull);
            }
            else{
                String filterPattern = charSequence.toString();
                System.out.println(filterPattern +" is fp");
                for(myItem item:mDatasetFull)
                    if((item.getName()!=null&&item.getName().toLowerCase().contains(filterPattern))||(item.getDescription()!=null&&item.getDescription().toLowerCase().contains(filterPattern)))
                        filteredList.add(item);
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDataset.clear();
            List<myItem> list = (List<myItem>) filterResults.values;
            System.out.println(list.size());
            if(list.size()>0)
                mDataset.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_list_view, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ((ItemHolder) holder).setItemDetails(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }
}
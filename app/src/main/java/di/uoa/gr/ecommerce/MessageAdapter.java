package di.uoa.gr.ecommerce;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import di.uoa.gr.ecommerce.rest.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private List<Message> mDataset;
    private Context mContext;
    private static ClickListener clickListener;

    public static class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        // each data item is just a string in this case
        public TextView text;

        public MessageHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(this);
            text= (TextView) v.findViewById(R.id.label);
        }

        @Override
        public void onClick(View v) {
            System.out.println("on click ");
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            System.out.println("on long click ");
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

        private void setItemDetails(final Message item) {
            if (item == null)
                return;
            String t="FROM: "+item.getFromUserID().getUsername()+"\n Message: "+item.getMessage();
            text.setText(t);
            if(!item.getSeen())
                text.setTypeface(text.getTypeface(),Typeface.BOLD);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MessageAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public void insert(List<Message> myD) {
        if(myD==null) {

        }
        else {
            mDataset = myD;
            notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(Context c, List<Message> myDataset) {
        mDataset = myDataset;
        mContext = c;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.test_list_item, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        ((MessageHolder) holder).setItemDetails(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }
}
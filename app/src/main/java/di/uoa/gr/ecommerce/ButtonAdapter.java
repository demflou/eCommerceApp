package di.uoa.gr.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import di.uoa.gr.ecommerce.rest.User;

/**
 * Created by ecommerce on 2/4/2017.
 */

public class ButtonAdapter extends ArrayAdapter<String> {

    private MainActivity myContext;
    private ArrayList<String> users;

    public ButtonAdapter(Context context, int resource, ArrayList<String> users) {
        super(context, resource, users);
        myContext = (MainActivity) context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listitem, null);

        // Lookup view for data population
        Button btButton = (Button) rowView.findViewById(R.id.itemButton);

        // Cache row position inside the button using `setTag`
        btButton.setTag(position);
        btButton.setText(users.get(position));

        // Attach the click event handler
        btButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                String userName = users.get(position);
                User user = new User();
                user.setUsername(userName);
                user= myContext.getUsers().get(myContext.getUsers().indexOf(user));
                //new UserDetailsRequestTask().execute(user.getId());
                //ListView listView = (ListView)myContext.findViewById(R.id.usersListView);
                //listView.setVisibility(View.GONE);
            }
        });
        // Return the completed view
        return rowView;
    }

}

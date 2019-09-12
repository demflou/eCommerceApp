package di.uoa.gr.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import di.uoa.gr.ecommerce.ItemAdapter;
import di.uoa.gr.ecommerce.R;
import di.uoa.gr.ecommerce.client.RestAPI;
import di.uoa.gr.ecommerce.client.RestClient;
import di.uoa.gr.ecommerce.rest.myCat;
import di.uoa.gr.ecommerce.rest.myItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuFragment2 extends Fragment {

    SearchView searchView;
    RecyclerView listView;
    List<myItem> list;
    ItemAdapter adapter;
    Spinner spinner;
    Button resetBtn;

    public MenuFragment2() {
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
        return inflater.inflate(R.layout.fragment_menu2, container, false);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = (RecyclerView) view.findViewById(R.id.SearchResults);
        adapter = new ItemAdapter(requireContext(),list);
        final LinearLayoutManager llm=new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        final DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        spinner=(Spinner)view.findViewById(R.id.spinner1);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        resetBtn = (Button) view.findViewById(R.id.resetSearch);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setSelection(0);
                searchView.setQuery("",false);
                searchView.clearFocus();
                list.clear();
                adapter = new ItemAdapter(requireContext(), list);
                listView.setLayoutManager(llm);
                listView.setAdapter(adapter);
                listView.addItemDecoration(mDividerItemDecoration);
                adapter.notifyDataSetChanged();
            }
        });
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<myCat>> call = restAPI.findAllCats();
        call.enqueue(new Callback<List<myCat>>() {
            @Override
            public void onResponse(Call<List<myCat>> call, Response<List<myCat>> response) {
                List<String> categories = new ArrayList<>();
                categories.add("None");
                for(myCat m:response.body())
                    categories.add(m.getName());
                ArrayAdapter<String> catAdapter= new ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, categories);
                catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(catAdapter);
            }

            @Override
            public void onFailure(Call<List<myCat>> call, Throwable t) {

            }
        });
        list = new ArrayList<>();

//        list.clear();
//        adapter.notifyDataSetChanged();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.clear();
                adapter = new ItemAdapter(requireContext(), list);
                listView.setLayoutManager(llm);
                listView.setAdapter(adapter);
                listView.addItemDecoration(mDividerItemDecoration);
                adapter.notifyDataSetChanged();
                if(!(spinner.getSelectedItem().toString().equals("None"))){
                    System.out.println("Category selected");
                    Call<List<myItem>> call = restAPI.findByCat(spinner.getSelectedItem().toString());
                    call.enqueue(new Callback<List<myItem>>() {
                        @Override
                        public void onResponse(Call<List<myItem>> call, Response<List<myItem>> response) {
//                            for(myItem i : response.body()){
//                                System.out.println(i.getId());
////                                list.clear();
//                                list.add(i.getName());
//                            }
                            list=response.body();
//                            listView.setAdapter(adapter);
                            adapter = new ItemAdapter(requireContext(), list);
                            listView.setLayoutManager(llm);
                            listView.addItemDecoration(mDividerItemDecoration);
                            adapter.getFilter().filter(query);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<myItem>> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                    return false;
                }
                adapter = new ItemAdapter(requireContext(), list);
                Call<List<myItem>> call = restAPI.searchDesc(query);
                call.enqueue(new Callback<List<myItem>>() {
                    @Override
                    public void onResponse(Call<List<myItem>> call, Response<List<myItem>> response) {
                        if(response.body() == null) {
//                            list.add("No Match found 3");
                        }
                        else{
                            for (myItem m:response.body()){
                                list.add(m);
                            }
                        }
                        adapter.insert(list);
                        listView.setLayoutManager(llm);
                        listView.setAdapter(adapter);
                        listView.addItemDecoration(mDividerItemDecoration);
                        adapter.setOnItemClickListener(new ItemAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                if(list!=null)
                                    System.out.println("onItemClick position: " + list.get(position).getId());
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
//                        list.add("Failure");
                    }
                });
//                adapter = new ArrayAdapter<String>(requireContext(), R.layout.test_list_item, list);


//                if (list.contains(query)) {
//                    adapter.getFilter().filter(query);
//                } else {
//                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show();
//                }
//                return false;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
//                if (newText.isEmpty()) {
//                    list.clear();
//                    adapter.notifyDataSetChanged();
//                }
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, final long l) {
                list.clear();
                adapter = new ItemAdapter(requireContext(), list);
                listView.setLayoutManager(llm);
                listView.setAdapter(adapter);
                listView.addItemDecoration(mDividerItemDecoration);
                adapter.notifyDataSetChanged();
                if(!searchView.getQuery().toString().equals("")|| adapterView.getItemAtPosition(i).toString().equals("None"))
                    return;
                Call<List<myItem>>call = restAPI.findByCat(adapterView.getItemAtPosition(i).toString());
                call.enqueue(new Callback<List<myItem>>() {
                    @Override
                    public void onResponse(Call<List<myItem>> call, Response<List<myItem>> response) {
                        for(myItem m:response.body())
                            list.add(m);
//                        adapter = new ItemAdapter(requireContext() ,list);
                        adapter.insert(list);
                        listView.setLayoutManager(llm);
                        listView.setAdapter(adapter);
                        listView.addItemDecoration(mDividerItemDecoration);
                        adapter.setOnItemClickListener(new ItemAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                if(list!=null)
                                    System.out.println("onItemClick position: " + list.get(position).getId());
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

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
//        SearchView sv= view.findViewById(R.id.searchView);
//        String words = sv.getQuery().toString();
//        System.out.println("SEARCH : " +words);
//        Call<List<myItem>> call = restAPI.searchDesc(words);
//            call.enqueue(new Callback<List<myItem>>() {
//            @Override
//            public void onResponse(Call<List<myItem>> call, Response<List<myItem>> response) {
//                System.out.println("SUCCESESEWSE");
//                String[] mobileArray;
//                if (!response.isSuccessful()) {
//                    mobileArray = new String[1];
//                    mobileArray[0]="ERROR on Response: " + response.code();
//                } else {
//                    try {
//                        List<myItem> listMessages = response.body();
//                        mobileArray = new String[listMessages.size()];
//    //                                             mobileArray = listMessages.toArray(new String[0]);
//                        for (int i=0;i< listMessages.size();i++) {
//                            mobileArray[i]=listMessages.get(i).getName();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        mobileArray = new String[1];
//                        mobileArray[0]="ERROR:" + e.getMessage();
//                    }
//                }
//                for (String c : mobileArray){
//                    System.out.println(c);
//                }
//                ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
//                        R.layout.test_list_item, mobileArray);
//
//                ListView listView = (ListView) getView().findViewById(R.id.SearchResults);
//                listView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<myItem>> call, Throwable t) {
//                System.out.println("FAILUERRERERE");
//                String[] mobileArray;
//                mobileArray = new String[1];
//                mobileArray[0]=t.fillInStackTrace()+ " ERROR on Failure: " + t.getMessage();
//                for (String c : mobileArray){
//                    System.out.println(c);
//                }
//                ArrayAdapter adapter = new ArrayAdapter<String>(requireContext(),
//                        R.layout.test_list_item, mobileArray);
//                ListView listView = (ListView) getView().findViewById(R.id.SearchResults);
//                listView.setAdapter(adapter);
//            }
//        });
//    }
}
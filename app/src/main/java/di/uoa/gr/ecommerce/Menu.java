package di.uoa.gr.ecommerce;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import di.uoa.gr.ecommerce.fragments.MenuFragment;
import di.uoa.gr.ecommerce.fragments.MenuFragment2;
import di.uoa.gr.ecommerce.fragments.MenuFragment3;
import di.uoa.gr.ecommerce.fragments.MenuFragment4;

public class Menu extends AppCompatActivity {

    private String jwt;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        prefs.edit().clear().commit();
        System.out.println("DESTROYED");
        super.onDestroy();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(jwt==null) {
            SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
            String restoredText = prefs.getString("jwt", null);
            System.out.println("reti jwt on click = " + restoredText);
            jwt = restoredText;
            System.out.println("from auction page 2 jwt = " + jwt);
        }
        if(jwt!=null) {
            adapter.addFragment(new MenuFragment(), "My Auctions");
            adapter.addFragment(new MenuFragment2(), "Search");
            adapter.addFragment(new MenuFragment3(), "Inbox");
            adapter.addFragment(new MenuFragment4(), "Outbox");
        }
        else{
            adapter.addFragment(new MenuFragment2(), "Search");
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
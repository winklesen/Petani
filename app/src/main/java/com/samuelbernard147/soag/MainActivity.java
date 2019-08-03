package com.samuelbernard147.soag;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.samuelbernard147.soag.adapter.TabFragmentAdapter;
import com.samuelbernard147.soag.fragment.AboutFragment;
import com.samuelbernard147.soag.fragment.ChiliFragment;
import com.samuelbernard147.soag.fragment.KaleFragment;
import com.samuelbernard147.soag.fragment.MustardGreenFragment;
import com.samuelbernard147.soag.fragment.TomatoFragment;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;
    Toolbar mToolBar;
    private String[] tabText = {
            "Cabai",
            "Kangkung",
            "Sawi",
            "Tomat"
    };
    private int[] tabIcons = {
            R.drawable.ic_chili,
            R.drawable.ic_kale,
            R.drawable.ic_mustard_green,
            R.drawable.ic_tomato
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        mToolBar = findViewById(R.id.tb_main);
        setSupportActionBar(mToolBar);
        mToolBar.setTitle("Petani");
        mViewPager = findViewById(R.id.vp_main);
        mTabLayout = findViewById(R.id.tl_main);

        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        TextView tabChili = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabChili.setText(tabText[0]);
        tabChili.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        mTabLayout.getTabAt(0).setCustomView(tabChili);

        TextView tabKale = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabKale.setText(tabText[1]);
        tabKale.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        mTabLayout.getTabAt(1).setCustomView(tabKale);

        TextView tabMustardGreens = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabMustardGreens.setText(tabText[2]);
        tabMustardGreens.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        mTabLayout.getTabAt(2).setCustomView(tabMustardGreens);

        TextView tabTomato = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTomato.setText(tabText[3]);
        tabTomato.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        mTabLayout.getTabAt(3).setCustomView(tabTomato);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFrag(new ChiliFragment(), tabText[0]);
        adapter.addFrag(new KaleFragment(), tabText[1]);
        adapter.addFrag(new MustardGreenFragment(), tabText[2]);
        adapter.addFrag(new TomatoFragment(), tabText[3]);
        viewPager.setAdapter(adapter);
    }

    //    Pemanggilan menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //    Fungsi ketika menu dipilih
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            FragmentManager fm = getSupportFragmentManager();
            AboutFragment about = new AboutFragment();
            about.show(fm, about.getTag());
        }
        return super.onOptionsItemSelected(item);
    }
}
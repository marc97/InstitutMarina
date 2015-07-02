package cat.institutmarina.insmarina.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.fragments.SearchFragment;

/**
 * Created by marcpacheco on 25/1/15.
 */
public class SearchActivity extends BaseActivity {
    private SearchAdapter searchAdapter;
    private static int currentPageType;
    private static SearchFragment currentFragment;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_activity_search, SearchFragment.newInstance());
        }*/

        viewPager = (ViewPager) findViewById(R.id.view_pager_activity_search);
        searchAdapter = new SearchAdapter(getFragmentManager());
        viewPager.setAdapter(searchAdapter);


        final Button btn_marina = (Button) findViewById(R.id.btn_activity_search_marina);
        final Button btn_aniram = (Button) findViewById(R.id.btn_activity_search_aniram);
        btn_marina.setSelected(true);

        btn_marina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        btn_aniram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_marina.setSelected(true);
                    btn_aniram.setSelected(false);
                } else if (position == 1) {
                    btn_marina.setSelected(false);
                    btn_aniram.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       // SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout_activity_search);
      //  slidingTabLayout.setViewPager(viewPager);
    }

    private static class SearchAdapter extends FragmentPagerAdapter {
        private FragmentManager fm;
        private static final String[] tabs = { "MARINA", "ANIRAM" };

        public SearchAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            SearchFragment frag = (SearchFragment) fm.findFragmentByTag("android:switcher:" + R.id.view_pager_activity_search + ":" + position);
            if (frag == null) {
                if (position == 0) {
                    frag = SearchFragment.newInstance(SearchFragment.TYPE_MARINA);
                } else if (position == 1) {
                    frag = SearchFragment.newInstance(SearchFragment.TYPE_ANIRAM);
                }
            }

            return frag;
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    private SearchFragment getCurrentFragment() {
        return (SearchFragment) getFragmentManager().
                findFragmentByTag("android:switcher:" + R.id.view_pager_activity_search + ":" + viewPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        if (searchItem != null) {
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            if (searchView == null) {
            } else {
                setupSearchView(searchView);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        searchView.clearFocus();
                        getCurrentFragment().updateSearch(s);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return true;
                    }
                });
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        finish();
                        return false;
                    }
                });
            }
        }
        return true;
    }
    private void setupSearchView(final SearchView searchView) {
        searchView.setQueryHint("Cercar...");

        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate)
                .setBackgroundColor(Color.WHITE);

        AutoCompleteTextView searchText = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setTextColor(getResources().getColor(R.color.text_color_primary));
        searchText.setHintTextColor(getResources().getColor(R.color.text_color_primary));

        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);

        // Hacky remove search icon.
        ImageView icon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        icon.setAdjustViewBounds(true);
        icon.setMaxWidth(0);
        icon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageDrawable(null);

    }
}
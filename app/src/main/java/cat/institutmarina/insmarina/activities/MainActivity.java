package cat.institutmarina.insmarina.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cat.institutmarina.insmarina.AppPreferences;
import cat.institutmarina.insmarina.Config;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.adapters.MenuDrawerListViewAdapter;
import cat.institutmarina.insmarina.drawermenu.DrawerItem;
import cat.institutmarina.insmarina.drawermenu.DrawerItemClickable;
import cat.institutmarina.insmarina.drawermenu.DrawerItemSeparator;
import cat.institutmarina.insmarina.fragments.BloggerPostsFragment;
import cat.institutmarina.insmarina.fragments.ContactFragment;
import cat.institutmarina.insmarina.fragments.PicasaAlbumsFragment;
import cat.institutmarina.insmarina.fragments.WebMenuFragment;
import cat.institutmarina.insmarina.ui.widget.TypefacedTextView;
import cat.institutmarina.insmarina.utils.AppRater;
import cat.institutmarina.insmarina.utils.DiskCacheUtils;
import cat.institutmarina.insmarina.utils.Utils;

public class MainActivity extends BaseActivity {
    private Context ctx = this;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private View lastDrawerItemViewPressed;
    private int currentDrawerItemSelected;
    private MenuDrawerListViewAdapter _menuDrawerListViewAdapter;

    private ListView listViewDrawer;

    private Runnable mPendingRunnable;
    private Handler mHandler = new Handler();

    private String mTitle = "";

    private static final int NAVDRAWER_ITEM_ANIRAM     = 0;
    private static final int NAVDRAWER_ITEM_CONTACTE   = 1;
    private static final int NAVDRAWER_ITEM_FOTOS      = 2;
    private static final int NAVDRAWER_ITEM_FALTES     = 3;
    private static final int NAVDRAWER_ITEM_FACEBOOK   = 4;
    private static final int NAVDRAWER_ITEM_MOODLE     = 5;
    private static final int NAVDRAWER_ITEM_PAGINA_WEB = 6;
    private static final int NAVDRAWER_ITEM_SETTINGS   = 7;
    private static final int NAVDRAWER_ITEM_MENU       = 8;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAppRater();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFragmentManager = getFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setActionBarTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setActionBarTitle(mTitle);

                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setUpNavDrawerList();
        listViewDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Object item = parent.getItemAtPosition(position);

                if (item instanceof DrawerItemClickable) {
                    mDrawerLayout.closeDrawers();

                    mPendingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            final int id_drawer = ((DrawerItemClickable) item).getId();
                            switch (id_drawer) {
                                case NAVDRAWER_ITEM_MENU:
                                    if (makeDrawerItemChecked(position)) {
                                        currentDrawerItemSelected = id_drawer;
                                        replaceFragment(new WebMenuFragment());
                                    }
                                    break;
                                case NAVDRAWER_ITEM_FOTOS:
                                    if (makeDrawerItemChecked(position)) {
                                        currentDrawerItemSelected = id_drawer;
                                        replaceFragment(PicasaAlbumsFragment.newInstance());
                                    }
                                    break;

                                case NAVDRAWER_ITEM_CONTACTE:
                                    if (makeDrawerItemChecked(position)) {
                                        currentDrawerItemSelected = id_drawer;
                                        replaceFragment(ContactFragment.getInstance());
                                    }
                                    break;

                                case NAVDRAWER_ITEM_ANIRAM:
                                    System.out.println("Aniram");
                                    System.out.println(position);
                                    if (makeDrawerItemChecked(position)) {
                                        System.out.println("Aniram checked");
                                        currentDrawerItemSelected = id_drawer;
                                        replaceFragment(BloggerPostsFragment.newInstance());
                                    }
                                    break;

                                case NAVDRAWER_ITEM_PAGINA_WEB:
                                    if (makeDrawerItemChecked(position)) {
                                        Utils.startWebViewActivity(MainActivity.this,
                                                getString(R.string.app_name), Config.URL_MARINA);
                                    }
                                    break;

                                case NAVDRAWER_ITEM_FALTES:
                                    if (makeDrawerItemChecked(position)) {
                                        Utils.startWebViewActivity(MainActivity.this,
                                                getString(R.string.nav_missings), Config.URL_MARINA_FALTES);
                                    }
                                    break;

                                case NAVDRAWER_ITEM_MOODLE:
                                    if (makeDrawerItemChecked(position)) {
                                        Utils.startWebViewActivity(MainActivity.this,
                                                getString(R.string.nav_moodle), Config.URL_MARINA_MOODLE);
                                    }
                                    break;

                                case NAVDRAWER_ITEM_FACEBOOK:
                                    if (makeDrawerItemChecked(position)) {
                                        try {
                                            startActivity(Utils.getOpenFacebookIntent(MainActivity.this,
                                                    Config.FB_MARINA_ID, Config.FB_MARINA_NAME));
                                        } catch (Exception e) {
                                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                    break;

                                case NAVDRAWER_ITEM_SETTINGS:
                                    if (makeDrawerItemChecked(position)) {
                                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                    }
                            }
                        }
                    };
                }

                if (canItemDrawerBeChecked(position)) {
                    // Bold on selected.
                    if (lastDrawerItemViewPressed != null) {
                        ((TypefacedTextView) lastDrawerItemViewPressed
                                .findViewById(R.id.txv_drawer_clickable_item))
                                .setTypeface(getString(R.string.font_normal));
                    }
                    TypefacedTextView txv = ((TypefacedTextView) view.findViewById(R.id.txv_drawer_clickable_item));
                    txv.setTypeface(getString(R.string.font_extra_bold));
                    lastDrawerItemViewPressed = txv;
                }

            }
        });

        // Set initial menu item selected.
        replaceFragment(new WebMenuFragment());
        currentDrawerItemSelected = NAVDRAWER_ITEM_MENU;
        makeDrawerItemChecked(0);

    }

    private int lastPosition = 0;
    private boolean makeDrawerItemChecked(int position) {
        Object item = getDrawerItemAtPosition(position);
        if (item instanceof DrawerItemClickable) {
            int id = ((DrawerItemClickable) item).getId();
            if (id == getDrawerItemSelected()) {
                listViewDrawer.setItemChecked(position, true);
                return false;
            } else {
                if (canItemDrawerBeChecked(position)) {
                    listViewDrawer.setItemChecked(position, true);
                    lastPosition = position;
                } else {
                    listViewDrawer.setItemChecked(position, false);

                    // Check previous item.
                    listViewDrawer.setItemChecked(lastPosition, true);
                }
                return true;
            }
        }
        return false;
    }

    private int getDrawerItemSelected() {
        return currentDrawerItemSelected;
    }

    private Object getDrawerItemAtPosition(int position) {
        return listViewDrawer.getAdapter().getItem(position);
    }

    private boolean canItemDrawerBeChecked(int position) {
        final Object item = getDrawerItemAtPosition(position);
        if (item instanceof DrawerItemClickable) {
            int id = ((DrawerItemClickable) item).getId();
            if (id == NAVDRAWER_ITEM_FACEBOOK)   return false;
            if (id == NAVDRAWER_ITEM_MOODLE)     return false;
            if (id == NAVDRAWER_ITEM_PAGINA_WEB) return false;
            if (id == NAVDRAWER_ITEM_FALTES)     return false;
            if (id == NAVDRAWER_ITEM_SETTINGS)   return false;
        }
        return true;
    }

    private void setUpNavDrawerList() {
        listViewDrawer = (ListView) findViewById(R.id.lsv_drawer_menu);
        listViewDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Menu drawer items
        List<DrawerItem> drawerItems = new ArrayList<DrawerItem>();
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_menu), R.drawable.ic_three_lines, NAVDRAWER_ITEM_MENU));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_aniram), R.drawable.ic_news, NAVDRAWER_ITEM_ANIRAM));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_photos), R.drawable.ic_image, NAVDRAWER_ITEM_FOTOS));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_contact), R.drawable.ic_contact,NAVDRAWER_ITEM_CONTACTE));
        drawerItems.add(new DrawerItemSeparator());
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_webpage), R.drawable.ic_institut_marina, NAVDRAWER_ITEM_PAGINA_WEB));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_missings), R.drawable.ic_clipboard, NAVDRAWER_ITEM_FALTES));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_moodle), R.drawable.ic_graduation_cap, NAVDRAWER_ITEM_MOODLE));
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_facebook), R.drawable.ic_facebook,NAVDRAWER_ITEM_FACEBOOK));
        drawerItems.add(new DrawerItemSeparator());
        drawerItems.add(new DrawerItemClickable(getString(R.string.nav_settings), R.drawable.ic_gear, NAVDRAWER_ITEM_SETTINGS));

        _menuDrawerListViewAdapter = new MenuDrawerListViewAdapter(this, drawerItems);
        listViewDrawer.setAdapter(_menuDrawerListViewAdapter);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public void addFragment(Fragment fragment) {
        // Show arrow.
        hideDrawerIndicator();

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setActionBarTitle(String title) {
        TextView txvTitle = (TextView) getActionBarToolbar().findViewById(R.id.toolbar_title);
        txvTitle.setText(title);

        if (!title.equalsIgnoreCase(getString(R.string.app_name)))
            mTitle = title;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                toggleDrawerMenu();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public void onBackPressed() {
        listenForBack(true);
    }

    @Override
    protected void listenForNavigationClick() {
        listenForBack(false);
    }

    private void popBackStack() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mFragmentManager.popBackStack();
        ft.commit();
    }

    private void toggleDrawerMenu() {
        if (!mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.openDrawer(Gravity.START | Gravity.LEFT);
        } else {
            mDrawerLayout.closeDrawers();
        }
    }

    private void hideDrawerIndicator() {
       /* if (mFragmentManager.getBackStackEntryCount() > 0) {*/
            mDrawerToggle.setDrawerIndicatorEnabled(false);
      /*  }*/
    }

    private void listenForBack(boolean isFromBackPressed) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            toggleDrawerMenu();
        } else {
            if (mFragmentManager.getBackStackEntryCount() == 1) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.syncState();
            }

            if (mFragmentManager.getBackStackEntryCount() > 0) {
                popBackStack();
            } else {
                if (isFromBackPressed) {
                    finish();
                } else {
                    toggleDrawerMenu();
                }
            }
        }
    }


    /** Clean Cache */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean App Cache
        if (!AppPreferences.isPhotosDiskCacheEnabled(this)) {
            DiskCacheUtils cacheUtils = new DiskCacheUtils(this);
            cacheUtils.deleteCache();
            cacheUtils.deleteExternalCache();
        }
    }

    /** AppRater */
    private void initAppRater() {
        AppRater appRater = new AppRater.Builder(this)
                .setPositiveBtn(getString(R.string.app_rater_positive_button))
                .setNegativeBtn(getString(R.string.app_rater_negative_button))
                .setNeutralBtn(getString(R.string.app_rater_neutral_button))
                .setTitle(getString(R.string.app_rater_title))
                .setMessage(getString(R.string.app_rater_message))
                .setPromptMins(Config.APP_RATER_MIN_LAUNCHES_UNTIL_PROMPT,
                        Config.APP_RATER_MIN_DAYS_UNTIL_PROMPT,
                        Config.APP_RATER_MIN_LAUNCHES_UNTIL_NEXT_PROMPT,
                        Config.APP_RATER_MIN_DAYS_UNTIL_NEXT_PROMPT)
                .build();
        appRater.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_search) {
                item.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ctx, SearchActivity.class));
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return true;
    }
}
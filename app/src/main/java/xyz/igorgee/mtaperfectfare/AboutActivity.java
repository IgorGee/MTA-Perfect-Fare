package xyz.igorgee.mtaperfectfare;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initializeInstances();
    }

    private void initializeInstances() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(AboutActivity.this, drawerLayout,
                R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.setDrawerListener(drawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity_about_title));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

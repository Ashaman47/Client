package fm.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance = null;
    private final int REQ_CODE_ORDER_INFO = 1;

    public static MainActivity getInstance() {
        return instance;
    }

    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (intent.getStringExtra("REFRESH") != null){
            FragmentManager fm = this.getSupportFragmentManager();
            MapFragment frag = (MapFragment) fm.findFragmentById(R.id.fragment_map);
            if (frag == null){
                frag = createMapFragment(DataCache.getInstance().getUsername());
                fm.beginTransaction()
                        .add(R.id.fragment_map, frag)
                        .commit();
            }
        }
        else {

            FragmentManager fm = this.getSupportFragmentManager();
            LoginFragment frag = (LoginFragment) fm.findFragmentById(R.id.fragment_login);
            if (frag == null) {
                frag = createLoginFragment(getString(R.string.firstName));
                fm.beginTransaction()
                        .add(R.id.fragment_login, frag)
                        .commit();
            }
        }
    }

    public static LoginFragment createLoginFragment(String title) {
        LoginFragment fragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putString(LoginFragment.ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }
    public static MapFragment createMapFragment(String title) {
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }


}
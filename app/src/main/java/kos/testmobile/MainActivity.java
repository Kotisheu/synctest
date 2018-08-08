package kos.testmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import kos.testmobile.AssignmentFragment.OnListFragmentInteractionListener;
import kos.testmobile.dummy.DummyContent;

import static android.app.PendingIntent.getActivity;


import static kos.testmobile.JSONAsyncTask.URLMESSAGE;
import static kos.testmobile.SyncFragment.EXTRA_MESSAGE;




public class MainActivity extends AppCompatActivity implements SyncFragment.OnFragmentInteractionListener,JSONRequestFragment.OnFragmentInteractionListener,
        OnListFragmentInteractionListener{

    static Sync sync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("workmarket");
        setContentView(R.layout.activity_main);
        System.out.println("maincreate");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("fragment", "").equals("assignments")){
            replaceFragment(new AssignmentFragment());
            navigation.setSelectedItemId(R.id.navigation_assignments);
        }else {
            replaceFragment(new SyncFragment());
            navigation.setSelectedItemId(R.id.navigation_home);
        }


        if(sync== null){
            sync= new Sync( this);
            try {
                sync.joinRoom(LoginActivity.getmUsername());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("mainresume");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("fragment", "").equals("assignments")){
            replaceFragment(new AssignmentFragment());
            navigation.setSelectedItemId(R.id.navigation_assignments);
        }else {
            replaceFragment(new SyncFragment());
            navigation.setSelectedItemId(R.id.navigation_home);
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = preferences.edit();
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    replaceFragment(new SyncFragment());

                    editor.putString("fragment", "sync");
                    editor.apply();

                    return true;
                case R.id.navigation_assignments:
                    //Intent intent = new Intent(getBaseContext(), Assignments.class);
                    //startActivity(intent);
                    replaceFragment(new AssignmentFragment());

                    editor.putString("fragment", "assignments");
                    editor.apply();


                    return true;

            }
            return false;
        }
    };


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        System.out.println(fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    };

        /**
         * Called when the user taps the Send button
         */
    public void sendMessage(View view) throws InterruptedException, ExecutionException {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.endpoint);
        String endpoint = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.request);
        String request = editText2.getText().toString();
        System.out.println("1test" + URLMESSAGE);
        System.out.println("URL=" + endpoint+":"+request);

        String[] urls = {endpoint, request};
        new JSONAsyncTask().execute(urls).get();
        System.out.println("3" + URLMESSAGE);
        String msg =endpoint +":"+request;
        msg+= "\n"+URLMESSAGE;

        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(JSONObject item) {

    }
}


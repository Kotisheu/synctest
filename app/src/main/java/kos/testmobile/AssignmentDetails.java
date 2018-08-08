package kos.testmobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static kos.testmobile.JSONAsyncTask.URLMESSAGE;
import static kos.testmobile.LoginActivity.mUsername;


public class AssignmentDetails extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra("id");
        setTitle("Assignment "+id);

        TextView textView = findViewById(R.id.assignid);
        try {
            textView.setText(getDetails(id).toString(2));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sent to the web app!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                String endpoint = "assignments/"+id+"/sync/M/n";
                String request = "{\"user\":\""+mUsername+"\"}";
                String[] urls = {endpoint, request};
                try {
                    new JSONAsyncTask().execute(urls).get();
                    //new JSONObject(URLMESSAGE);// should just be 'OK'
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetector = new GestureDetectorCompat(this,this);


    }
    public JSONObject getDetails(String id) throws ExecutionException, InterruptedException, JSONException {
        String endpoint = "assignments/"+id;
        String request = " ";
        String[] urls = {endpoint, request};
        new JSONAsyncTask().execute(urls).get();
        return new JSONObject(URLMESSAGE);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("touch");
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        System.out.println("Scroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        System.out.println("flung");
        String endpoint = "assignments/"+id+"/sync/M/n";
        String request = "{\"user\":\""+mUsername+"\"}";
        String[] urls = {endpoint, request};
        try {
            new JSONAsyncTask().execute(urls).get();
            //new JSONObject(URLMESSAGE);// should just be 'OK'
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }


}

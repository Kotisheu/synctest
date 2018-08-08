package kos.testmobile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;



public class Sync {

    private Socket mSocket;
    Activity A;
    public Sync( Activity A) {
        this.A=A;
        try {
            //mSocket = IO.socket("http://192.168.21.46:80/");
            mSocket = IO.socket("http://192.168.0.25:80/");
//            mSocket.on("my_response", onNewMessage);
            mSocket.on("assignment", onAssignSync);
            mSocket.on("Wassignment", onAssignWSync);
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(String room) throws JSONException {
        mSocket.emit("join", new JSONObject("{room:\""+room+"\"}") );
    }
    public Socket getSocket() {
        return mSocket;
    }

//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            //= (JSONObject) new JafaSONParser().parse(args[0]);
//            try {
//                String payload= args[0].toString();
//                System.out.println("PAYLOAD: "+payload);
//                if (payload.equals("my_response")){
//                    return;// break
//                }
//                // /SYNC GET SEND DIFFERENTLY SO THIS DOES NOT RECEIVE PROPERLY
//                JSONObject data= new JSONObject(payload);
//                //String data= (String)args[0];
//                //String username;
//                String message;
//                // try {
//                message= data.get("data").toString();
//                //message= args[0].toString();
//                System.out.println("DATA: " +message);
//                //username = data.getString("username");
//                //message = data.getString("message");
//                // } catch (JSONException e) {
//                //   return;
//                //}
//
//                // add the message to view
//                addMessage( message);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        };
//    };

    private Emitter.Listener onAssignSync = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                String payload= args[0].toString();
                System.out.println("PAYLOAD: "+payload);
                if (payload.equals("assignment")){
                    return;// break
                }

                JSONObject data= new JSONObject(payload);
                String message;
                message= data.get("data").toString();
                //message= args[0].toString();
                System.out.println("DATA: " +message);

                Intent intent = new Intent(A, AssignmentDetails.class);
                intent.putExtra("id",message);
                A.startActivity(intent);

                //addMessage("ASSIGNMENT RECEIVED");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    };
    private Emitter.Listener onAssignWSync = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                String payload= args[0].toString();
                System.out.println("PAYLOAD: "+payload);
                if (payload.equals("assignment")){
                    return;// break
                }

                JSONObject data= new JSONObject(payload);
                //String data= (String)args[0];
                //String username;
                String message;
                // try {
                message= data.get("data").toString();
                //message= args[0].toString();
                System.out.println("DATA: " +message);

                Intent intent = new Intent(A, AssignmentDetails.class);
                intent.putExtra("id",message);
                A.startActivity(intent);

                //addMessage("WASSIGNMENT RECEIVED");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public void show_children(View v) {
        ViewGroup viewgroup = (ViewGroup) v;
        System.out.println("VIEWgroup:"+viewgroup.toString());
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            View v1 = viewgroup.getChildAt(i);
            System.out.println("C: "+v1.toString());
            if (v1 instanceof ViewGroup) show_children(v1);

        }
        System.out.println("__________");
    }


}

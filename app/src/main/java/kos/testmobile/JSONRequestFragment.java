package kos.testmobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JSONRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JSONRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JSONRequestFragment extends Fragment {


    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    private OnFragmentInteractionListener mListener;

    public JSONRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JSONRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JSONRequestFragment newInstance(String param1, String param2) {
        JSONRequestFragment fragment = new JSONRequestFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SSLCertificateHandler.nuke();

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        //String endpoint = preferences.getString("endpoint", "");
        String endpoint ="";
        if(endpoint.equalsIgnoreCase(""))
        {
            endpoint="in";
        }
        EditText editText1 = (EditText) view.findViewById(R.id.endpoint);
        editText1.setText(endpoint);

        //String request = preferences.getString("request", "");
        String request="";
        if(request.equalsIgnoreCase(""))
        {
            request="{\"username\": \"bob\"}";  /* Edit the value here*/
        }
        EditText editText2 = (EditText) view.findViewById(R.id.request);
        editText2.setText(request);

        //view.findViewById(R.id.yourId).setOnClickListener(this);

        // or
        //getActivity().findViewById(R.id.yourId).setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package kos.testmobile;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kos.testmobile.AssignmentFragment.OnListFragmentInteractionListener;


import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link JSONObject} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAssignmentRecyclerViewAdapter extends RecyclerView.Adapter<MyAssignmentRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<JSONObject> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyAssignmentRecyclerViewAdapter(JSONArray items, OnListFragmentInteractionListener listener) {
        
        mValues = new ArrayList<>();
        if(items != null){
            for(int i=0; i< items.length();i++){
                mValues.add(items.optJSONObject(i));
            }
            
        }

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        try {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getInt("id")));
            holder.mContentView.setText(mValues.get(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    Intent intent = new Intent(v.getContext(), AssignmentDetails.class);
                    intent.putExtra("id",holder.mIdView.getText());
                    v.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public JSONObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

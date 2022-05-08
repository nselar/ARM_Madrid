package com.example.arm_madrid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView id, place, comment, lat, lon;
        private final ImageButton locate, edit, delete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            id = view.findViewById(R.id.id);
            place = view.findViewById(R.id.place);
            comment = view.findViewById(R.id.comment);
            lat = view.findViewById(R.id.lat);
            lon = view.findViewById(R.id.lon);

            locate = view.findViewById(R.id.locate);
            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);

            locate.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        public TextView getId() {
            return id;
        }

        public TextView getPlace() {
            return place;
        }

        public TextView getComment() {
            return comment;
        }

        public TextView getLat() {
            return lat;
        }

        public TextView getLon() {
            return lon;
        }

        @Override
        public void onClick(View view) {

            if(view.equals(delete)){
                delete(Integer.parseInt(id.getText().toString()), getAdapterPosition());
            }

            if(view.equals(locate)){
                locate(place.getText().toString(), Long.parseLong(lat.getText().toString()), Long.parseLong(lon.getText().toString()));
            }

            if(view.equals(edit)){
                edit(Integer.parseInt(id.getText().toString()),place.getText().toString(), comment.getText().toString(), Double.parseDouble(lat.getText().toString()), Double.parseDouble(lon.getText().toString()));
            }

        }

    }

    private Cursor localDataSet;
    private SQLiteManager db;
    private final Context context;

    public CustomAdapter(Context context) {

        this.context = context;
        this.db = new SQLiteManager(context);
        this.localDataSet = db.getAllData();

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, null, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        localDataSet.moveToPosition(position);

        viewHolder.getId().setText(String.valueOf(localDataSet.getInt(localDataSet.getColumnIndexOrThrow(SQLiteManager.LocationData._ID))));
        viewHolder.getLat().setText(String.valueOf(localDataSet.getLong(localDataSet.getColumnIndexOrThrow(SQLiteManager.LocationData.COLUMN_NAME_LATITUDE))));
        viewHolder.getLon().setText(String.valueOf(localDataSet.getLong(localDataSet.getColumnIndexOrThrow(SQLiteManager.LocationData.COLUMN_NAME_LONGITUDE))));
        viewHolder.getPlace().setText(localDataSet.getString(localDataSet.getColumnIndexOrThrow(SQLiteManager.LocationData.COLUMN_NAME_PLACE)));
        viewHolder.getComment().setText(localDataSet.getString(localDataSet.getColumnIndexOrThrow(SQLiteManager.LocationData.COLUMN_NAME_COMMENT)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.getCount();
    }

    public void updateData(){

        localDataSet = db.getAllData();
        notifyDataSetChanged();

    }

    private void delete(int id, int index){

        db.deletePlace(id);
        localDataSet = db.getAllData();

        notifyItemRemoved(index);

    }

    private void edit(int id, String place, String comment, double lat, double lon){

        Intent placeIntent = new Intent(context,AddPlaceActivity.class);

        placeIntent.putExtra("isEditing", true);
        placeIntent.putExtra("id", id);
        placeIntent.putExtra("place", place);
        placeIntent.putExtra("comment", comment);
        placeIntent.putExtra("lat", lat);
        placeIntent.putExtra("lon", lon);

        context.startActivity(placeIntent);

    }

    private void locate(String name,double lat, double lon){

        Intent mapsIntent = new Intent(this.context,MapsActivity.class);

        mapsIntent.putExtra("name", name);
        mapsIntent.putExtra("lat", lat);
        mapsIntent.putExtra("lon", lon);

        context.startActivity(mapsIntent);

    }
}

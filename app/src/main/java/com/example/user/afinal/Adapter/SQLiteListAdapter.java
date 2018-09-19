package com.example.user.afinal.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.Model.Contact;
import com.example.user.afinal.R;

import java.util.ArrayList;

/**
 * Created by USER on 10/21/2017.
 */

public class SQLiteListAdapter extends BaseAdapter implements Filterable {

    Context context;
    private ArrayList<Contact> mOriginalValues = null;
    ArrayList<Contact> mcontact ;
    ArrayList<String> UserId;
    ArrayList<String> UserName;
    ArrayList<String> User_Age;
    ArrayList<String> UserAddress;
    ArrayList<byte[]> UserImage;
    DBAdapter adb ;
    int intid;
    public SQLiteListAdapter(
            Context context2,
            ArrayList<String> name,
            ArrayList<String> age,
            ArrayList<String> address,
            ArrayList<String> UserId,
            ArrayList<byte[]> Uimage) {
        super();
        this.context = context2;
        this.UserName = name;
        this.User_Age = age;
        this.UserAddress = address;
        this.UserId = UserId;
        this.UserImage = Uimage;
        adb =new DBAdapter(context);
    }
    @Override
    public int getCount() {
        return UserName.size();
       // return  UserId.size();
    }
    @Override
    public Object getItem(int position) {
         return UserName.get(position);
        //return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
       // return UserName.get(position).getId();
    }
    @Override
    public View getView(final int position, View child, final ViewGroup parent) {

        final Holder holder;
        final LayoutInflater layoutInflater;

        if (child == null) {

            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listviewdatalayout, null);

            holder = new Holder();

            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);
            holder.textviewage = (TextView) child.findViewById(R.id.textViewAGE);
            holder.textviewaddress = (TextView) child.findViewById(R.id.textViewADDRESS);
            holder.cls = (ImageView) child.findViewById(R.id.imgcls);
            holder.textViewid=(TextView)child.findViewById(R.id.textID);
            holder.imageView1 =(ImageView)child.findViewById(R.id.imagepic);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewname.setText(UserName.get(position));
        holder.textviewage.setText(User_Age.get(position));
        holder.textviewaddress.setText(UserAddress.get(position));
        holder.textViewid.setText(UserId.get(position));

        if(UserImage.get(position)!= null)
       holder.imageView1.setImageBitmap(convertToBitmap(UserImage.get(position)));
        holder.imageView1.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
                {
                    LayoutInflater layinf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    View dialogview = layinf.inflate(R.layout.profilepic, null);
                    builder.setView(dialogview);
                    ImageView propic = (ImageView) dialogview.findViewById(R.id.propic);
                    TextView protext =(TextView)dialogview.findViewById(R.id.protext);
                    protext.setText(UserName.get(position));
                    propic.setImageBitmap(convertToBitmap(UserImage.get(position)));
                }

                builder.show();
            }
        });
        holder.cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, "you clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Confirm Delete...");
                    alertDialog.setMessage("Are you sure you want delete this?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                            View pr=(View)v.getParent();
                            TextView txtid = (TextView)pr.findViewById(R.id.textID) ;
                            adb.open();
                            UserName.remove(position);
                            User_Age.remove(position);
                            UserAddress.remove(position);
                            UserId.remove(position);
                            UserImage.remove(position);
                           // adb.deleterow3(Integer.parseInt(txtid.getText().toString();
                            
                            notifyDataSetChanged();
                            adb.close();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
        });
        return child;

    }

    @Override
    public Filter getFilter() {
        try {
            Filter filter = new Filter() {

                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {
                    try{
                        mcontact = new ArrayList<Contact>();
                        mcontact = (ArrayList<Contact>) results.values; // has the filtered values
                        //   notifyDataSetChanged();  // notifies the data with new filtered values
                    }catch (Exception ex){
                        Log.e("resultfilter", ex.getMessage().toString());
                        Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Contact> FilteredArrList = new ArrayList<Contact>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Contact>(mcontact); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        try {
                            constraint = constraint.toString().toLowerCase();
                            for (int i = 0; i < mOriginalValues.size(); i++) {
                                String data = mOriginalValues.get(i).getName();
                                if (data.toLowerCase().startsWith(constraint.toString())) {
                                    FilteredArrList.add(new Contact(mOriginalValues.get(i).getID(),mOriginalValues.get(i).getName(),mOriginalValues.get(i).getAge(),mOriginalValues.get(i).getAddress(),mOriginalValues.get(i).getImage()));
                                }
                            }
                            // set the Filtered result to return
                            results.count = FilteredArrList.size();
                            results.values = FilteredArrList;

                        }catch (Exception ex){
                            Log.e("FilterRFesult",ex.getMessage().toString());
                        }

                    }
                    return results;
                }
            };
            return filter;
        }catch (Exception ex){
            Log.e("getfilter", ex.getMessage().toString());
            Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public class Holder {
        TextView textviewname;
        TextView textviewage;
        TextView textviewaddress;
        TextView textViewid;
        ImageView imageView1;
        ImageView cls;
    }

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);


    }
}

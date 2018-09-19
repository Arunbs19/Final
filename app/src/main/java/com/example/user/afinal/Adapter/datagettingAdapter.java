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
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.DATABASE.DBAdapter;
import com.example.user.afinal.Model.Contact;
import com.example.user.afinal.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by USER on 12/2/2017.
 */

public class datagettingAdapter extends ArrayAdapter<Contact> implements Filterable {

    Context context;
    ArrayList<Contact> mcontact ;
    DBAdapter adb ;

    public datagettingAdapter(Context context,ArrayList<Contact> contact){
        super(context,R.layout.listviewdatalayout,contact);
        this.context = context;
        this.mcontact=contact;
        adb =new DBAdapter(context);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public Contact getItem(int position) {
        return mcontact.get(position);
    }
    @Override
    public View getView(final int position, View child, final ViewGroup parent) {

        Holder holder;
        Contact data = getItem(position);
        //Contact data = mcontact.get(position);
        final LayoutInflater layoutInflater;
        if (child == null) {

            holder = new Holder();

            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listviewdatalayout, null);
            //child=LayoutInflater.from(getContext()).inflate(R.layout.listviewdatalayout,parent,false);
            holder.textViewid=(TextView)child.findViewById(R.id.textID);
            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);
            holder.textviewage = (TextView) child.findViewById(R.id.textViewAGE);
            holder.textviewaddress = (TextView) child.findViewById(R.id.textViewADDRESS);
            holder.imageView1 =(ImageView)child.findViewById(R.id.imagepic);
            holder.cls = (ImageView) child.findViewById(R.id.imgcls);

            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        if (data!=null){
            if (holder.textViewid!=null){
                holder.textViewid.setText(String.valueOf(data.getID()));}
           if ( holder.textviewname!=null){
               holder.textviewname.setText(data.getName());}
            holder.textviewage.setText(data.getAge());
             holder.textviewaddress.setText(data.getAddress());
         if(data.getImage()!= null)
            holder.imageView1.setImageBitmap(convertToBitmap(data.getImage()));}

        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                {
                    LayoutInflater layinf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    View dialogview = layinf.inflate(R.layout.profilepic, null);
                    Contact data = getItem(position);
                    builder.setView(dialogview);
                    ImageView propic = (ImageView) dialogview.findViewById(R.id.propic);
                    TextView protext =(TextView)dialogview.findViewById(R.id.protext);
                    protext.setText(data.getName());
                    propic.setImageBitmap(convertToBitmap(data.getImage()));
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
                        Contact data = getItem(position);
                        adb.open();
                        remove(data);
                       // adb.deleterow3(Integer.parseInt(txtid.getText().toString()));
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
        return myFilter;
    }

    public class Holder {
        TextView textViewid;
        TextView textviewname;
        TextView textviewage;
        TextView textviewaddress;
        ImageView imageView1;
        ImageView cls;
    }

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);


    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Contact> tempList=new ArrayList<Contact>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint != null && mcontact!=null) {
                int length=mcontact.size();
                int i=0;
                while(i<length){
                    Contact item=mcontact.get(i);
                    //do whatever you wanna do here
                    //adding result set output array
                    tempList.add(item);
                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            mcontact = (ArrayList<Contact>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
}

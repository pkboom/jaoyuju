package com.sujichim.jasanjao2.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sujichim.jasanjao2.R;

import java.io.File;

/**
 * Created by yedam on 15. 10. 22.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewListItemViewHolder> {
    private static final String LOG_TAG = "RecyclerViewAdapter";

    java.util.List mDataset;
    Context mContext;
    Record record;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(java.util.List mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewListItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        RecyclerViewListItemViewHolder vh = new RecyclerViewListItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerViewListItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Record record = (Record) mDataset.get(position);
        holder.mTextView.setText(record.getShortInfo());

        holder.setClickListener(new RecyclerViewListItemViewHolder.ClickListener() {
            @Override
            public void onClick(View v, final int position, boolean isLongClick) {
                if (isLongClick) {
                    //long-clicked.
//                    Toast.makeText(mContext, "long clicked " + record.getInfo(), Toast.LENGTH_SHORT).show();
                    final View view = v;

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                    dialogBuilder.setTitle("삭제하시겠습니까?");
//                    alert.setMessage("Message");
                    dialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do something with value!
                            deleteItem(position);
                            Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialogBuilder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                }
                            });

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                } else {
                    // View v at position pos is clicked.
//                    Toast.makeText(mContext, "clicked " + recordItem.getInfo(), Toast.LENGTH_SHORT).show();
                    showItem(position);
                }
            }

        });
    }

    public void addItem(Record record) {
        DBHelper db = DBHelper.getInstance(mContext);
        db.addRecord(record);

/*
        // Reading all contacts

        java.util.List records = db.getAllRecords();

        for (Record rd : records) {
            String log = "Id: " + rd.getId()
                    + " ,Date: " + rd.getDate()
                    + " ,Name: " + rd.getName()
                    + " ,Birthday: " + rd.getBirthday()
                    + " ,Memo: " + rd.getMemo();
            // Writing Records to log
            Log.d("Name: ", log);
        }
*/

        //refresh list<record> to notify change
        mDataset = db.getAllRecords();
//        notifyItemInserted(db.getRowCount());
        notifyDataSetChanged();
    }

    public void updateItem(Record record) {
        DBHelper db = DBHelper.getInstance(mContext);
        int affectedRow = db.updateRecord(record);
        Log.d(LOG_TAG, "affectedRow " + String.valueOf(affectedRow));

        //refresh list<record> to notify change
        mDataset = db.getAllRecords();
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        DBHelper db = DBHelper.getInstance(mContext);
        record = (Record) mDataset.get(position);

        // delete record
        db.deleteRecord(record);

        //refresh list<record> to notify change
        mDataset = db.getAllRecords();
        notifyItemRemoved(position);
    }

    public void showItem(int position) {
        record = (Record) mDataset.get(position);

        if (mContext == null)
            return;
        if (mContext instanceof List) {
            ((List) mContext).switchContent(record);
        }

        //Toast.makeText(mContext, "clicked " + record.getInfo(), Toast.LENGTH_SHORT).show();

    }

    public void showAllItems() {
        DBHelper db = DBHelper.getInstance(mContext);
        mDataset = db.getAllRecords();
        notifyDataSetChanged();
    }

    //delete files and directory
    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    void getSearchResult(String name) {
        DBHelper db = DBHelper.getInstance(mContext);
        mDataset = db.getAllRecordsOfSameName(name);

        notifyDataSetChanged();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
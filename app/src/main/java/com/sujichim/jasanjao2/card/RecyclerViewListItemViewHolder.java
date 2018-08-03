package com.sujichim.jasanjao2.card;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sujichim.jasanjao2.R;

/**
 * Created by yedam on 15. 10. 22.
 */

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class RecyclerViewListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    // each data item is just a string in this case
    public TextView mTextView;
    private ClickListener clickListener;

    public RecyclerViewListItemViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.textView4);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    /* Interface for handling clicks - both normal and long ones. */
    public interface ClickListener {

        /**
         * Called when the view is clicked.
         *
         * @param v view that is clicked
         * @param position of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        void onClick(View v, int position, boolean isLongClick);
    }

    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // Handles the row being clicked
    @Override
    public void onClick(View v) {
        // If not long clicked, pass last variable as false.
        clickListener.onClick(v, getAdapterPosition(), false);
    }


    @Override
    public boolean onLongClick(View v) {
        // If long clicked, passed last variable as true.
        clickListener.onClick(v, getAdapterPosition(), true);
//        return true;
        return false;
    }
}
package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private final List<BasicNoteA> mItems;
    public int mSelectedItem = -1;

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note, parent, false);

        //view.setOnCreateContextMenuListener(mContextMenuListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(mItems.get(position).getTitle());
        if (!mItems.get(position).getIsEncrypted())
            holder.mEncryptedImage.setVisibility(View.GONE);
        else
            holder.mEncryptedImage.setVisibility(View.VISIBLE);
        holder.mLastModified.setText(DateFormat.getDateInstance().format(new Date(mItems.get(position).getLastModified())));
        //holder.mLastModified.setText(mItems.get(position).getLastModified());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        if (position == mSelectedItem) {
            Log.d("RecycleViewAdapter", "set selected position, position=" + position + ", selectedItem=" + mSelectedItem);
            holder.mView.setBackgroundResource(R.color.colorAccent);
        } else {
            Log.d("RecycleViewAdapter", "set not selected position, position=" + position + ", selectedItem=" + mSelectedItem);
            holder.mView.setBackgroundResource(R.color.windowBackground);
        }

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*
                PopupMenu popupMenu = new PopupMenu(holder.mView.getContext(), holder.mView);
                popupMenu.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(holder.mView.getContext(), "Long pressed " + holder.getAdapterPosition() + " and selected " + item, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupMenu.show();
                */
                BasicNoteActivity activity = (BasicNoteActivity)v.getContext();
                activity.startSupportActionMode(activity.new ActionBarCallBack());

                if (mSelectedItem != -1)
                    BasicNoteRecycleViewAdapter.this.notifyItemChanged(mSelectedItem);

                Log.d("RecycleViewAdapter", "set selectedItem=" + holder.getAdapterPosition());
                mSelectedItem = holder.getAdapterPosition();

                BasicNoteRecycleViewAdapter.this.notifyItemChanged(mSelectedItem);

                Toast.makeText(holder.mView.getContext(), "Selected " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                //BasicNoteRecycleViewAdapter.this.notifyDataSetChanged();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mLastModified;
        public final ImageView mEncryptedImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mEncryptedImage = (ImageView) view.findViewById(R.id.encrypted_image);
        }
    }
}

package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.model.BasicHNoteItemA;

import java.util.Date;
import java.util.List;

public class BasicHEventNamedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicHEventNamedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicHNoteItemA> mItemList;
    private final DateTimeFormatter mDTF;

    public BasicHEventNamedItemRecyclerViewAdapter(Context context, List<BasicHNoteItemA> itemList) {
        mDTF = new DateTimeFormatter(context);
        mItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.h_event_named_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BasicHNoteItemA item = mItemList.get(i);

        viewHolder.mValueTextView.setText(item.getValue());
        String eventTimeString = mDTF.formatDateTimeDelimited(new Date(item.getEventTime()), " ");
        viewHolder.mEventTimeTextView.setText(eventTimeString);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mValueTextView;
        private final TextView mEventTimeTextView;

        public ViewHolder(View view) {
            super(view);

            mValueTextView = view.findViewById(R.id.value);
            mEventTimeTextView = view.findViewById(R.id.event_time);
        }
    }
}

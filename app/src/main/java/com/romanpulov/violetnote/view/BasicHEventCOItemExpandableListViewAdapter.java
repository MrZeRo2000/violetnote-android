package com.romanpulov.violetnote.view;

import android.content.Context;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class BasicHEventCOItemExpandableListViewAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final LongSparseArray<BasicHEventA> mHEventList;
    private final LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItemList;

    private final DateTimeFormatter mDTF;

    public BasicHEventCOItemExpandableListViewAdapter(
            Context context,
            LongSparseArray<BasicHEventA> hEventList,
            LongSparseArray<List<BasicHNoteCOItemA>> hEventCOItemList) {
        mContext = context;
        mDTF = new DateTimeFormatter(mContext);
        mHEventList = hEventList;
        mHEventCOItemList = hEventCOItemList;
    }

    @Override
    public int getGroupCount() {
        return mHEventList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        BasicHEventA group = (BasicHEventA)getGroup(groupPosition);
        List<BasicHNoteCOItemA> items = mHEventCOItemList.get(group.getId());
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //for descending order of events
        return mHEventList.get(mHEventList.keyAt(mHEventList.size() - 1 - groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        BasicHEventA group = (BasicHEventA)getGroup(groupPosition);
        List<BasicHNoteCOItemA> items = mHEventCOItemList.get(group.getId());
        return items == null ? null : items.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((BasicHEventA)getGroup(groupPosition)).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        BasicHEventA group = (BasicHEventA)getGroup(groupPosition);
        List<BasicHNoteCOItemA> items = mHEventCOItemList.get(group.getId());
        return items == null ? 0 : items.get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_h_event_group, null);
        }
        TextView groupTitle = convertView.findViewById(R.id.group_title);
        long eventTime = ((BasicHEventA)getGroup(groupPosition)).getEventTime();
        String eventTimeString = DateFormat.getDateTimeInstance().format(new Date(eventTime));
        groupTitle.setText(eventTimeString);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_h_event_list_item, null);
        }
        TextView itemTitle = convertView.findViewById(R.id.item_title);
        BasicHNoteCOItemA item = (BasicHNoteCOItemA)getChild(groupPosition, childPosition);
        itemTitle.setText(item.getValue());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

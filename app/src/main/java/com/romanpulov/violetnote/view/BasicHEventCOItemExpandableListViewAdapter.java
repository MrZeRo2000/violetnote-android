package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.appcompat.view.ActionMode;
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
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;

import java.util.Date;
import java.util.List;

public class BasicHEventCOItemExpandableListViewAdapter extends BaseExpandableListAdapter implements ViewSelectorHelper.ChangeNotificationListener{
    private final Context mContext;
    private final LongSparseArray<BasicHEventA> mHEventList;
    private final LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItemList;
    private final DateTimeFormatter mDTF;

    private final ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mExViewSelector;

    public ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> getViewSelector() {
        return mExViewSelector;
    }

    public BasicHEventCOItemExpandableListViewAdapter(
            Context context,
            LongSparseArray<BasicHEventA> hEventList,
            LongSparseArray<List<BasicHNoteCOItemA>> hEventCOItemList,
            ActionMode.Callback actionModeCallback
            ) {
        mContext = context;
        mHEventList = hEventList;
        mHEventCOItemList = hEventCOItemList;
        mDTF = new DateTimeFormatter(context);

        mExViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
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
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private static final class GroupViewHolder {
        final TextView mGroupTitle;

        GroupViewHolder(View v) {
            mGroupTitle = v.findViewById(R.id.group_title);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_h_event_group, null);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        //TextView groupTitle = convertView.findViewById(R.id.group_title);
        long eventTime = ((BasicHEventA)getGroup(groupPosition)).getEventTime();
        //String eventTimeString = DateFormat.getDateTimeInstance().format(new Date(eventTime));
        String eventTimeString = mDTF.formatDateTimeDelimited(new Date(eventTime), " ");
        viewHolder.mGroupTitle.setText(eventTimeString);

        return convertView;
    }

    private static final class ChildViewHolder implements View.OnLongClickListener, View.OnClickListener{
        final View mView;
        final TextView mItemTitle;
        final ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;
        BasicHNoteCOItemA mData;

        ChildViewHolder(View v, ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> viewSelector) {
            this.mViewSelector = viewSelector;
            mView = v;
            mItemTitle = v.findViewById(R.id.item_title);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            /*
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "long click " + ((ChildViewHolder)v.getTag()).mItemTitle.getText(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
             */
        }

        @Override
        public void onClick(View v) {
            mViewSelector.setSelectedView(v, mData);
        }

        @Override
        public boolean onLongClick(View v) {
            mViewSelector.startActionMode(v, mData);
            return true;
        }

        public void updateBackground() {
            ViewSelectorHelper.updateSelectedViewBackground(mView, mViewSelector, mData);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_h_event_list_item, null);
            viewHolder = new ChildViewHolder(convertView, mExViewSelector);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder)convertView.getTag();
        }
        BasicHNoteCOItemA item = (BasicHNoteCOItemA)getChild(groupPosition, childPosition);
        viewHolder.mData = item;
        viewHolder.mItemTitle.setText(item.getValue());
        viewHolder.updateBackground();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

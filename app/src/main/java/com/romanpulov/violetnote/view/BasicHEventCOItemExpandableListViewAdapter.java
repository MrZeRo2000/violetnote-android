package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;

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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicHEventCOItemExpandableListViewAdapter extends BaseExpandableListAdapter implements ViewSelectorHelper.ChangeNotificationListener{
    private final Context mContext;
    private final LongSparseArray<BasicHEventA> mHEventList;
    private final LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItemList;
    private final Set<String> mValues;
    private final Set<String> mSelectedValues = new HashSet<>();
    private final int mDimColor;
    private final int mBrightColor;

    private final DateTimeFormatter mDTF;

    private final ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mExViewSelector;

    public ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> getViewSelector() {
        return mExViewSelector;
    }

    public BasicHEventCOItemExpandableListViewAdapter(
            Context context,
            LongSparseArray<BasicHEventA> hEventList,
            LongSparseArray<List<BasicHNoteCOItemA>> hEventCOItemList,
            Collection<String> values,
            ActionMode.Callback actionModeCallback
            ) {
        mContext = context;
        mHEventList = hEventList;
        mHEventCOItemList = hEventCOItemList;
        mValues = new HashSet<>(values);

        mDimColor = ContextCompat.getColor(context, R.color.dimTextColor);
        mBrightColor = ContextCompat.getColor(context, R.color.brightTextColor);

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
        GroupViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.expandable_list_h_event_group, parent, false);
                viewHolder = new GroupViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        if (viewHolder != null) {
            //TextView groupTitle = convertView.findViewById(R.id.group_title);
            long eventTime = ((BasicHEventA) getGroup(groupPosition)).getEventTime();
            //String eventTimeString = DateFormat.getDateTimeInstance().format(new Date(eventTime));
            String eventTimeString = mDTF.formatDateTimeDelimited(new Date(eventTime), " ");
            viewHolder.mGroupTitle.setText(eventTimeString);
        }

        return convertView;
    }

    private static final class ChildViewHolder implements View.OnLongClickListener, View.OnClickListener{
        final View mView;
        final TextView mItemTitle;
        final ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;
        final Set<String> mValues;
        final Set<String> mSelectedValues;
        BasicHNoteCOItemA mData;

        ChildViewHolder(
                View v,
                ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> viewSelector,
                Set<String> values,
                Set<String> selectedValues
        ) {
            mViewSelector = viewSelector;
            mView = v;
            mItemTitle = v.findViewById(R.id.item_title);
            mValues = values;
            mSelectedValues = selectedValues;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        private void updateSelectedValues() {
            mSelectedValues.clear();
            for (BasicHNoteCOItemA item: mViewSelector.getSelectedItems()) {
                mSelectedValues.add(item.getValue());
            }
        }

        private boolean clickAllowed() {
            return !((mValues.contains(mData.getValue())) ||
                    (mSelectedValues.contains(mData.getValue()) && !(mViewSelector.getSelectedItems().contains(mData)))
            );
        }

        @Override
        public void onClick(View v) {
            if (clickAllowed()) {
                mViewSelector.setSelectedView(mData);
                updateSelectedValues();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (clickAllowed()) {
                mViewSelector.startActionMode(v, mData);
                updateSelectedValues();
                return true;
            } else {
                return false;
            }
        }

        public void updateBackground() {
            ViewSelectorHelper.updateSelectedViewBackground(mView, mViewSelector, mData);
        }
    }

    private boolean selectorContainsValue(String value) {
        for (BasicHNoteCOItemA item : mExViewSelector.getSelectedItems()) {
            if (item.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.expandable_list_h_event_list_item, parent, false);
                viewHolder = new ChildViewHolder(convertView, mExViewSelector, mValues, mSelectedValues);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ChildViewHolder)convertView.getTag();
        }

        if (viewHolder != null) {
            BasicHNoteCOItemA item = (BasicHNoteCOItemA) getChild(groupPosition, childPosition);
            viewHolder.mData = item;

            String itemValue = item.getValue();
            viewHolder.mItemTitle.setText(itemValue);
            if (mValues.contains(itemValue) || selectorContainsValue(itemValue))  {
                viewHolder.mItemTitle.setTextColor(mDimColor);
            } else {
                viewHolder.mItemTitle.setTextColor(mBrightColor);
            }

            viewHolder.updateBackground();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

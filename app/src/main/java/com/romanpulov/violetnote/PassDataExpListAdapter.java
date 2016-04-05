package com.romanpulov.violetnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.romanpulov.violetnotecore.Model.PassData;

/**
 * Created by rpulov on 04.04.2016.
 */
public class PassDataExpListAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private PassDataExp mData;

    public PassDataExpListAdapter(Context context) {
        mContext = context;
    }

    public void setData(PassDataExp data) {
        mData = data;
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.getPassCategoryList().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData == null ? 0 : mData.getPassNoteList().get(mData.getPassCategoryList().get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData == null ? null : mData.getPassCategoryList().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData == null ? null : mData.getPassNoteList().get(mData.getPassCategoryList().get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plv_group, null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plv_item, null);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

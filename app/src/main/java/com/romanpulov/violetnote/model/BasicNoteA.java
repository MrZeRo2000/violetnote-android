package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.romanpulov.violetnote.db.provider.BasicNoteDBManagementProvider;
import com.romanpulov.violetnote.model.vo.BasicNoteSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BasicNote data class
 * Created by rpulov on 11.08.2016.
 */
public final class BasicNoteA extends BasicCommonNoteA implements Parcelable {
    public static final int NOTE_TYPE_CHECKED = 0;
    public static final int NOTE_TYPE_NAMED = 1;

    private long mGroupId;
    private int mNoteType;
    private String mTitle;
    private boolean mEncrypted;
    private String mEncryptedString;

    //calculated

    private BasicNoteSummary mSummary = new BasicNoteSummary();

    @NonNull
    public BasicNoteSummary getSummary() {
        return mSummary;
    }

    public void setSummary(BasicNoteSummary value) {
        mSummary = value;
        summaryChanged();
    }

    public long getNoteGroupId() {
        return mGroupId;
    }

    public int getNoteType() {
        return mNoteType;
    }

    //DisplayTitleProvider

    @Override
    public String getDisplayTitle() {
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isEncrypted() {
        return mEncrypted;
    }

    public String getEncryptedString() {
        return mEncryptedString;
    }

    public void summaryChanged() {
        mSummary.updateItemCountTitle(mNoteType);
    }

    public String getItemCountTitle() {
        return mSummary.getItemCountTitle();
    }

    private List<BasicNoteItemA> mItems = new ArrayList<>();

    public final List<BasicNoteItemA> getItems() {
        return mItems;
    }

    public void setItems(List<BasicNoteItemA> items) {
        mItems = items == null ? new ArrayList<BasicNoteItemA>() : items;
    }

    private Set<String> mValues = new HashSet<>();

    public Collection<String> getValues() {
        return mValues;
    }

    public void setValues(Collection<String> values) {
        mValues = values == null ?  new HashSet<String>() : new HashSet<>(values);
    }

    private final List<BasicNoteHistoryItemA> mHistoryItems = new ArrayList<>();

    public List<BasicNoteHistoryItemA> getHistoryItems() {
        return mHistoryItems;
    }

    /**
     * Returns last note item position with given priority
     * @param priority Priority
     * @return Item
     */
    public int getLastNoteItemPriorityPosition(long priority) {
        int result = -1;

        BasicNoteItemA noteItem = null;

        for (BasicNoteItemA item : mItems) {
            result ++;

            // first found with required priority
            if ((noteItem == null) && (item.getPriority() == priority)) {
                noteItem = item;
                continue;
            }

            if ((noteItem != null) && (item.getPriority() != priority))
                break;
        }

        if (noteItem == null)
            return -1;
        else
            return result;
    }

    @Nullable
    public static BasicNoteA findByTitle(Iterable<BasicNoteA> noteList, String title) {
        if (noteList != null) {
            for (BasicNoteA note : noteList) {
                if ((note.mTitle != null) && (note.mTitle.equals(title))) {
                    return note;
                }
            }
        }

        return null;
    }

    private BasicNoteA() {
        setDBManagementProvider(new BasicNoteDBManagementProvider(this));
    }

    /**
     * Creates notes without totals
     * @param id note id
     * @param lastModified last modified date
     * @param lastModifiedString last modified date as string
     * @param orderId order id
     * @param groupId group id
     * @param noteType note type
     * @param title note title
     * @param encrypted flag if a not is encrypted
     * @param encryptedString not used
     * @return new instance
     */
    public static BasicNoteA newInstance(long id, long lastModified, String lastModifiedString, long orderId, int groupId, int noteType, String title, boolean encrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.setId(id);
        instance.setLastModified(lastModified);
        instance.setLastModifiedString(lastModifiedString);
        instance.setOrderId(orderId);
        instance.mGroupId = groupId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }

    /**
     * Creates notes with calculated fields
     * @param id note id
     * @param lastModified last modified date
     * @param lastModifiedString last modified date as string
     * @param orderId order id
     * @param groupId group id
     * @param noteType note type
     * @param title note title
     * @param encrypted flag if a not is encrypted
     * @param encryptedString not used
     * @param itemCount number of items
     * @param checkedItemCount number of checked items
     * @return new instance
     */
    public static BasicNoteA newInstanceWithTotals(long id, long lastModified, String lastModifiedString, long orderId, int groupId, int noteType, String title, boolean encrypted, String encryptedString, int itemCount, int checkedItemCount) {
        BasicNoteA instance = new BasicNoteA();

        instance.setId(id);
        instance.setLastModified(lastModified);
        instance.setLastModifiedString(lastModifiedString);
        instance.setOrderId(orderId);
        instance.mGroupId = groupId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
        instance.mEncryptedString = encryptedString;
        instance.mSummary = BasicNoteSummary.fromItemCounts(itemCount, checkedItemCount);
        instance.summaryChanged();

        return instance;
    }

    /**
     * Creates notes for editor
     * most fields are empty
     * @param noteType note type
     * @param groupId group id
     * @param title note title
     * @param encrypted flag if a not is encrypted
     * @param encryptedString not used
     * @return new instance
     */
    public static BasicNoteA newEditInstance(long groupId, int noteType, String title, boolean encrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mGroupId = groupId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }

    @Override
    public String toString() {
        return "{" +
                "[id=" + getId() + "]," +
                "[groupId=" + mGroupId + "]," +
                "[lastModified=" + getLastModified() + "]," +
                "[orderId=" + getOrderId() + "]," +
                "[noteType=" + mNoteType + "]," +
                "[title=" + mTitle + "]," +
                "[encrypted=" + mEncrypted + "]," +
                "[encryptedString=" + mEncryptedString + "]" +
                "[summary=" + mSummary + "]" +
                "}";
    }

    private BasicNoteA(@NonNull Parcel in) {
        setId(in.readLong());
        setLastModified(in.readLong());
        setLastModifiedString(in.readString());
        setOrderId(in.readLong());
        mGroupId = in.readLong();
        mNoteType = in.readInt();
        mTitle = in.readString();
        mEncrypted = BooleanUtils.fromInt(in.readInt());
        mEncryptedString = in.readString();
        mSummary = in.readParcelable(BasicNoteSummary.class.getClassLoader());

        in.readTypedList(mItems, BasicNoteItemA.CREATOR);

        String[] values = in.createStringArray();
        setValues(Arrays.asList(values));

        in.readTypedList(mHistoryItems, BasicNoteHistoryItemA.CREATOR);

        summaryChanged();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getLastModified());
        dest.writeString(getLastModifiedString());
        dest.writeLong(getOrderId());
        dest.writeLong(mGroupId);
        dest.writeInt(mNoteType);
        dest.writeString(mTitle);
        dest.writeInt(BooleanUtils.toInt(mEncrypted));
        dest.writeString(mEncryptedString);
        dest.writeParcelable(mSummary, 0);
        dest.writeTypedList(mItems);
        dest.writeStringArray(mValues.toArray(new String[0]));
        dest.writeTypedList(mHistoryItems);
    }

    public static final Parcelable.Creator<BasicNoteA> CREATOR = new Parcelable.Creator<BasicNoteA>() {
        @Override
        public BasicNoteA createFromParcel(Parcel source) {
            return new BasicNoteA(source);
        }

        @Override
        public BasicNoteA[] newArray(int size) {
            return new BasicNoteA[size];
        }
    };

}

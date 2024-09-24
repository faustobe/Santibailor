package it.faustobe.santibailor.presentation.features.settings;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class SettingItemList implements Parcelable {
    private List<SettingItem> items;

    public SettingItemList(List<SettingItem> items) {
        this.items = items;
    }

    protected SettingItemList(Parcel in) {
        items = new ArrayList<>();
        in.readList(items, SettingItem.class.getClassLoader());
    }

    public static final Creator<SettingItemList> CREATOR = new Creator<SettingItemList>() {
        @Override
        public SettingItemList createFromParcel(Parcel in) {
            return new SettingItemList(in);
        }

        @Override
        public SettingItemList[] newArray(int size) {
            return new SettingItemList[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<SettingItem> getItems() {
        return items;
    }
}
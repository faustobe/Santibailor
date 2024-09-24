package it.faustobe.santibailor.presentation.features.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class SettingItem implements Parcelable {
    private final String title;
    private final String description;
    private final SettingType type;
    private Integer navigationAction;
    private boolean toggleState;

    public enum SettingType {
        NAVIGATION, TOGGLE, ACTION
    }

    // Costruttore per item di navigazione
    public SettingItem(String title, String description, Integer navigationAction) {
        this.title = title;
        this.description = description;
        this.type = SettingType.NAVIGATION;
        this.navigationAction = navigationAction;
    }

    // Costruttore per item toggle
    public SettingItem(String title, String description, boolean toggleState) {
        this.title = title;
        this.description = description;
        this.type = SettingType.TOGGLE;
        this.toggleState = toggleState;
    }

    // Costruttore per item azione (senza toggle)
    public SettingItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.type = SettingType.ACTION;
    }

    // Getter
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public SettingType getType() { return type; }
    public Integer getNavigationAction() { return navigationAction; }
    public boolean isToggleState() { return toggleState; }

    // Setter
    public void setToggleState(boolean toggleState) { this.toggleState = toggleState; }

    // Implementazione Parcelable
    protected SettingItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        type = SettingType.valueOf(in.readString());
        navigationAction = in.readInt();
        toggleState = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(type.name());
        dest.writeInt(navigationAction != null ? navigationAction : -1);
        dest.writeByte((byte) (toggleState ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SettingItem> CREATOR = new Creator<SettingItem>() {
        @Override
        public SettingItem createFromParcel(Parcel in) {
            return new SettingItem(in);
        }

        @Override
        public SettingItem[] newArray(int size) {
            return new SettingItem[size];
        }
    };

}

package fr.diabhelp.suiviprochepatient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sumbers on 27/11/2015.
 */
public class Proche implements Parcelable {
    private Integer id;
    private String nickName;
    private String avatarLink;


    public Proche(Integer id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    //get object from parcel
    public Proche(Parcel in){
        String[] data = new String[2];

        this.id = in.readInt();
        in.readStringArray(data);
        this.nickName = data[0];
        this.avatarLink = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeStringArray(new String[]{
                this.nickName,
                this.avatarLink});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Proche createFromParcel(Parcel in) {
            return new Proche(in);
        }

        @Override
        public Proche[] newArray(int size) {
            return new Proche[size];
        }
    };


        public Integer getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getAvatarLink() {
        return avatarLink;
    }
}

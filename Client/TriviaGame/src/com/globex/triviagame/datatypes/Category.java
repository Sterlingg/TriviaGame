package com.globex.triviagame.datatypes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Category implements Parcelable {

	private String category;
	
	public static final Creator<Category> CREATOR = new Creator<Category>() {

		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		public Category[] newArray(int size) {
			return new Category[size];
		}
	};
	
	public Category(String category){
		this.category=category;
	}
	
	private Category(Parcel in) {
		readFromParcel(in);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private void readFromParcel(Parcel in) {
		this.category = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Log.v("","writingToParcel" + flags);
		out.writeString(category);
	}

	public String toString(){
		
		return category;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

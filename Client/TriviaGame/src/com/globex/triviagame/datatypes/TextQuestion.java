package com.globex.triviagame.datatypes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * TextQuestion: Object for storing questions.
 * @author sterling
 *
 */
public class TextQuestion implements Parcelable {
	private String question;
	private String category;
	private String distractorA;
	private String distractorB;
	private String distractorC;
	private String answer;
	
	public TextQuestion(String question,
			String category,
			String distractorA,
			String distractorB,
			String distractorC,
			String answer){
		this.question = question;
		this.category = category;
		this.distractorA = distractorA;
		this.distractorB = distractorB;
		this.distractorC = distractorC;
		this.answer = answer; 
	}
		
	private TextQuestion(Parcel in) {
		readFromParcel(in);
	}

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDistractorA() {
		return distractorA;
	}
	public void setDistractorA(String distractorA) {
		this.distractorA = distractorA;
	}
	public String getDistractorB() {
		return distractorB;
	}
	public void setDistractorB(String distractorB) {
		this.distractorB = distractorB;
	}
	public String getDistractorC() {
		return distractorC;
	}
	public void setDistractorC(String distractorC) {
		this.distractorC = distractorC;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public static final Creator<TextQuestion> CREATOR = new Creator<TextQuestion>() {

		public TextQuestion createFromParcel(Parcel source) {
			return new TextQuestion(source);
		}

		public TextQuestion[] newArray(int size) {
			return new TextQuestion[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void readFromParcel(Parcel in) {
		this.question = in.readString();
		this.category = in.readString();
		this.distractorA = in.readString();
		this.distractorB = in.readString();
		this.distractorC = in.readString();
		this.answer = in.readString();	
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		Log.v("","writingToParcel" + flags);
		out.writeString(question);
		out.writeString(category);
		out.writeString(distractorA);
		out.writeString(distractorB);
		out.writeString(distractorC);
		out.writeString(answer);
	}

	@Override
	public String toString() {
		return "TextQuestion [question=" + question + ", category=" + category
				+ ", distractorA=" + distractorA + ", distractorB="
				+ distractorB + ", distractorC=" + distractorC + ", answer="
				+ answer + "]";
	}

	
}

package com.dnd.privacyapp;

import java.util.ArrayList;

public class Question {
	private String question;
	private ArrayList<String> choices;
	private String answer;
	
	public Question(String question, ArrayList<String> choices, String answer)
	{
		this.question = question;
		this.choices = choices;
		this.answer = answer;
	}
	
	public String getQuestion()
	{
		return question;
	}
	
	public ArrayList<String> getChoices()
	{
		return choices;
	}
	
	public String getAnswer(){
		return answer;
	}
}

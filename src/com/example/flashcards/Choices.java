package com.example.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Choices extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		
		Button bEqual = (Button)findViewById(R.id.equal);
		Button bFactor = (Button)findViewById(R.id.factor);
		final RadioButton rbEasy = (RadioButton)findViewById(R.id.easy);
		final RadioButton rbMed = (RadioButton)findViewById(R.id.average);
		final RadioButton rbDiff = (RadioButton)findViewById(R.id.difficult);
		
		bEqual.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Choices.this,MainActivity.class);
				if(rbEasy.isChecked())
					i.putExtra("time", 2000);
				else if(rbMed.isChecked())
					i.putExtra("time", 1500);
				else
					i.putExtra("time", 1000);
				i.putExtra("choice", 1);
				startActivity(i);
			}
		});
		
		bFactor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Choices.this,MainActivity.class);
				if(rbEasy.isChecked())
					i.putExtra("time", 2000);
				else if(rbMed.isChecked())
					i.putExtra("time", 1500);
				else
					i.putExtra("time", 1000);
				i.putExtra("choice", 2);
				startActivity(i);
			}
		});
	}

}

package com.caug.failblog.activity;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
    }
	
	@Override
	protected void onResume()
    {
		super.onResume();
    }
}
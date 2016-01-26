package com.agentdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.util.Config;

public class Login extends Activity {

	Button submit, exit;
	String username, password;
	EditText userinput, passinput;
	SharedPreferences sh_Pref;
	Editor toEdit;
	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		submit = (Button) findViewById(R.id.btnlogin);
		userinput = (EditText) findViewById(R.id.userinput);
		passinput = (EditText) findViewById(R.id.passinput);
	}

	public void login(View v) {

		// read preferencies
		sh_Pref = this.getSharedPreferences(("credentials"),
				Context.MODE_WORLD_READABLE);
		String usernamefromfile = sh_Pref.getString(Config.USERNAME_KEY, "empty");
		String passwordfromfile = sh_Pref.getString(Config.PASSWORD_KEY, "empty");

		Log.d("Login", usernamefromfile);
		Log.d("Login", passwordfromfile);

		username = userinput.getText().toString().trim();
		password = passinput.getText().toString().trim();

		if (username.equalsIgnoreCase(usernamefromfile)
				&& password.equalsIgnoreCase(passwordfromfile)) {
			userinput.setText("");
			passinput.setText("");
			i = new Intent(this, MainActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
		startActivity(intent);
		finish();
		System.exit(0);

   	}
}

	

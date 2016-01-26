package com.agentdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.util.Config;

public class FirstLogin extends Activity {

	// preferencies
	SharedPreferences sh_Pref;
	String username, password, confirm;
	Intent i;
	EditText user, pass, con;
	private String TAG="FirstLogin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_login);
		user = (EditText) findViewById(R.id.usernamefl);
		pass = (EditText) findViewById(R.id.passwordfl);
		con = (EditText) findViewById(R.id.confirmpassfl);
	}

	public void signup(View v) {
		username = user.getText().toString().trim();
		password = pass.getText().toString().trim();
		confirm = con.getText().toString().trim();

		Log.d(TAG,password);

		if (username.equalsIgnoreCase("")) {
			Toast.makeText(this, "The username cannot be empty", Toast.LENGTH_SHORT).show();
		} else if (password.equalsIgnoreCase("")) {
			Toast.makeText(this, "The Password cannot be empty", Toast.LENGTH_SHORT).show();
		}  else if (!confirm.equalsIgnoreCase(password)) {
			Toast.makeText(this, "Password confirmation failled", Toast.LENGTH_SHORT).show();
		} else {

			sh_Pref = getSharedPreferences(("credentials"), Context.MODE_WORLD_READABLE);
			SharedPreferences.Editor editor = sh_Pref.edit();
			editor.putString(Config.USERNAME_KEY, username);
			editor.putString(Config.PASSWORD_KEY, password);
			editor.commit();
			Toast.makeText(this, "Sign UP Successful", Toast.LENGTH_SHORT).show();
			i = new Intent(this, Login.class);
			startActivity(i);
		}

	}

}

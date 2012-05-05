package video.search;

import video.protocol.Engine;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		listenLoginButton();
		listenRegisterButton();
	}
	
	private void listenLoginButton(){
		Button btnlogin = (Button) findViewById(R.id.login);
		btnlogin.setOnClickListener(new LoginListener());
	}
	
	private void listenRegisterButton(){
		Button b = (Button) findViewById(R.id.register);
		b.setOnClickListener(new RegisterListener());
	}
	
	private class RegisterListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(i);
		}
	}
	
	private class LoginListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String userName = getUserName();
			String password = getPassword();
			if(login(userName, password)){
				loginSuccess();
			} else {
				loginFail();
			}
		}
		
		private String getUserName(){
			return getText(R.id.userName);
		}
		
		private String getPassword(){
			return getText(R.id.password);
		}
		
		private String getText(int id){
			TextView v = (TextView)findViewById(id);
			return v.getText().toString();
		}
		
		private boolean login(String userName, String password){
			String userIdString = new Engine().Login(userName, password);
			int userId = Integer.parseInt(userIdString);
			if(userId == 0){
				return false;
			} else {
				return true;
			}
		}
		
		private void loginSuccess(){
			toast("µÇÂ¼³É¹¦");
			LoginActivity.this.finish();
		}
		
		private void toast(String a){
			Toast.makeText(LoginActivity.this, a, Toast.LENGTH_LONG);
		}
		
		private void loginFail(){
			toast("µÇÂ¼Ê§°Ü");
		}
	}
}
package video.search;

import android.app.Activity;
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
	}
	
	private void listenLoginButton(){
		Button btnlogin = (Button) findViewById(R.id.loginButton);
		btnlogin.setOnClickListener(new LoginListener());
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
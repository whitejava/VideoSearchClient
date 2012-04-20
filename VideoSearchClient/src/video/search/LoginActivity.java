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
		Button btnlogin = (Button) findViewById(R.id.loginButton);
		btnlogin.setOnClickListener(new LoginListener());
	}
	
	private class LoginListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			TextView a = (TextView) findViewById(R.id.userName);
			TextView b = (TextView) findViewById(R.id.password);
			String userName = a.getText().toString();
			String password = b.getText().toString();
			//if(new Engine().Login(userName, password)!="Error")
			//{
				Toast.makeText(LoginActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_LONG);

				LoginActivity.this.finish();
			//}
		}
	}
}
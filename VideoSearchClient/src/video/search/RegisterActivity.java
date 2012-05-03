package video.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		listenRegisterButton();
	}
	
	private void listenRegisterButton(){
		Button b = (Button)findViewById(R.id.registerButton);
		b.setOnClickListener(new Register());
	}
	
	private class Register implements OnClickListener {
		@Override
		public void onClick(View v) {
			String userName = getText(R.id.userName);
			String password = getText(R.id.password);
			if(register(userName, password)){
				toast("ע��ɹ�");
				RegisterActivity.this.finish();
			} else {
				toast("ע��ʧ��");
			}
		}
		
		private boolean register(String userName, String password){
			
		}
		
		private String getText(int id){
			return ((TextView)findViewById(id)).getText().toString();
		}
		
		private void toast(String m){
			Toast.makeText(RegisterActivity.this, m, Toast.LENGTH_LONG);
		}
	}
}
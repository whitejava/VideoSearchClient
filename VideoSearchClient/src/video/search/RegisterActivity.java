package video.search;

import video.protocol.Engine;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		listenRegisterButton();
	}
	
	private void listenRegisterButton(){
		Button b = (Button)findViewById(R.id.register);
		b.setOnClickListener(new Register());
	}
	
	private class Register implements OnClickListener {
		@Override
		public void onClick(View v) {
			String userName = getText(R.id.userName);
			String password = getText(R.id.password);
			String sex = getSex();
			String email = getText(R.id.email);
			if(register(userName, password,sex,email)){
				toast("ע��ɹ�");
				RegisterActivity.this.finish();
			} else {
				toast("ע��ʧ��");
			}
		}
		
		private String getSex(){
			RadioGroup a = (RadioGroup)findViewById(R.id.sex);
			if(a.getCheckedRadioButtonId() == R.id.male){
				return "true";
			} else {
				return "false";
			}
		}
		
		private boolean register(String userName, String password, String sex,String email){
			String r = new Engine().Register(userName, password, sex, email);
			return Integer.parseInt(r) != 0;
		}
		
		private String getText(int id){
			return ((TextView)findViewById(id)).getText().toString();
		}
		
		private void toast(String m){
			Toast.makeText(RegisterActivity.this, m, Toast.LENGTH_LONG);
		}
	}
}
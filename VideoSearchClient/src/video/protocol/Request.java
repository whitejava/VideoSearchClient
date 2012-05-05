package video.protocol;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class Request {
	// 命名空间
	final static String SERVICE_NS = "http://yhtt2020.vicp.cc/VideoSearchService";
	// 服务地址
	final static String SERVICE_URL = "http://www.coolsou.com/SearchEngine.asmx";
	// ACTION
	final String SERVICE_ACTION = SERVICE_NS;
	private String methodName=""; 
	private SoapObject soapObject = null;
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param operation 操作名称，对应于webservice的函数
	 */
	public Request(String operation) {
		this.methodName=operation;
		soapObject = new SoapObject(SERVICE_NS, operation);
	}

	/**使用内置属性的方式添加参数，添加顺序和名称必须与webservice完全相同
	 * @param name 欲添加的参数名称
	 * @param value 欲添加参数的值
	 */
	public void put(String name, Object value) {
		soapObject.addProperty(name, value);
	}

	/**使用propertyinfo方式添加参数
	 * @param pi propertyinfo
	 */
	public void AddPropertyParameter(PropertyInfo pi) {
		soapObject.addProperty(pi);
	}

	/**添加特性形式的参数，一般不使用这种方法
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void AddAttributeParameter(String name, Object value) {
		soapObject.addAttribute(name, value);
	}

	/**创建HttpTransferportSE，该方法是私有方法
	 * @return 返回一个HttpTransferportSE对象
	 */
	private HttpTransportSE CreateTransportSE() {
		HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
		httpTransportSE.debug = true;
		return httpTransportSE;
	}

	/**创建一个序列化后的Soap封包，该方法是私有方法
	 * @return
	 */
	private SoapSerializationEnvelope CreateEnvelope() {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		return envelope;
	}

	/**去的SoapObject对象，通过该对象可取得可操作性较强的结果
	 * @return 结果对象
	 */
	public SoapObject GetResult() {
		String methodNameString = soapObject.getName();
		SoapObject resultObject=null;
		SoapSerializationEnvelope envelope = CreateEnvelope();
		try {
			HttpTransportSE se = CreateTransportSE();
			//se.call(null, envelope);
			se.call(SERVICE_ACTION+"/"+ methodNameString, envelope);
			
			if (envelope.getResponse() != null) {
				//结果不为空，直接返回Soap对象
				resultObject = (SoapObject) envelope.bodyIn;
				
				return resultObject;
			}
			//结果为空
		} 
		catch (XmlPullParserException  e) {
			Log.e("错误",e.getMessage());
			return null;
		}
		catch (IOException e) {
			Log.e("错误",e.getMessage());
			return null;
		}
		//
		return resultObject;
	}
	public String getStringResult(SoapObject soapObject,Request r){
	//	String str=soapObject.getProperty(0).toString();
		String resultString=soapObject.getProperty(r.getMethodName()+"Result").toString();
		return resultString;
	}
}

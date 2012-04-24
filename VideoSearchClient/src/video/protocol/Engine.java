package video.protocol;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

public class Engine {
	public Good[] SearchByKeyFeatures(String featureCodes,String userPos,String alpha,String sameDegree) {
		featureCodes = featureCodes.trim().replace(" ", ",");
		Request r = new Request("SearchByKeyFeatures");
		r.put("featureCode",featureCodes);
		r.put("userPos","00");
		r.put("str_alpha",alpha);
		r.put("str_sameDegree", sameDegree);
		try {
			SoapObject soap = send(r);
			return toGoods(soap);
		} catch(Exception e){
			return null;
		}
	}
	
	private SoapObject send(Request r){
		return (SoapObject) r.GetResult().getProperty(0);
	}
	
	public Good[] SearchByKeyWords(String keyWords,String userPos){
		Request r = new Request("SearchByKeyWords");
		r.put("keyWords",keyWords);
		r.put("userPos",userPos);
		try{
			SoapObject detail = null;
			detail=(SoapObject)r.GetResult().getProperty(0);
			return toGoods(detail);
		}
		catch (Exception e) {
			Log.e("´íÎó", e.getMessage());
			return null;
		}
	}
	
	public String Login(String userName, String password){
		Request r = new Request("Login");
		r.put("userName", userName);
		r.put("password", password);
		SoapObject detail = r.GetResult();
		return getResult(detail);
	}
	
	private String getResult(SoapObject soapObject){
		return soapObject.getProperty(0).toString();
	}
	
	private Good[] toGoods(SoapObject detail){
		Good[] goodEntities=new Good[detail.getPropertyCount()];
		int index=0;
		for(int i=0;i<detail.getPropertyCount();i++){
			SoapObject good=(SoapObject)detail.getProperty(i);
			Good geEntity=new Good();
			geEntity.setId(good.getProperty(0).toString());
			geEntity.setName(good.getProperty(1).toString());
			geEntity.setUrl(good.getProperty(2).toString());
			geEntity.setKind(good.getProperty(3).toString());
			geEntity.setPrice((Float.parseFloat(good.getProperty(4).toString())));
			geEntity.setPosition(good.getProperty(5).toString());
			geEntity.setGrade(good.getProperty(6).toString());
			geEntity.setFullUrl(good.getProperty(7).toString());
			geEntity.setDescribe(Boolean.getBoolean(good.getProperty(8).toString()));
			geEntity.setRetire(Boolean.getBoolean(good.getProperty(9).toString()));
			geEntity.setExactPosition(good.getProperty(10).toString());
			goodEntities[index]=geEntity;
			index++;
		}
		return goodEntities;
	}
}

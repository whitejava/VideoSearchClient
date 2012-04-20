package video.protocol;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

public class Engine {
	private WebServiceUtil webServiceUtil=null;
	public Good[] SearchByKeyFeatures(String featureCodes,String userPos,String alpha,String sameDegree) 
	{
		//创建参数对象
		//featureCodes = 
		//		 "93,168,142,122,148,176,213,208,208,208,204,190,240,222,226,234,229,234,225,189,211,208,231,186,227,246,303,351,321,368,363,371,335,374,368,363,278,193,129,106,77,80,70,68,71,55,53,58,47,44,41,40,40,42,32,30,36,44,46,43,39,37,42,35,37,56,94,124,110,73,85,246,589,1105,137,79,54,66,48,39,44,40,47,40,42,47,39,41,45,33,40,28,36,30,27,42,13,15,21,22,13,14,8,24,15,12,20,19,17,19,16,73,76,69,12,19,18,16,20,14,19,26,12,29,12,16,14,15,12,16,17,9,15,16,15,16,11,15,20,15,22,16,18,21,12,16,13,29,17,22,14,20,23,13,14,21,15,13,9,7,13,11,9,15,10,8,13,11,6,19,8,12,19,13,13,31,72,19,84,26,14,17,24,8,17,12,7,16,11,20,18,13,23,8,18,14,12,17,22,15,27,27,36,89,151,308,554,946,1761,1499,1327,1133,618,493,358,244,222,186,132,194,156,183,189,185,212,163,193,198,127,358,318,459,984,1361,1403,2204,1200,1240,1691,1714,2015,2253,1112,1392,2525,1821,2058,2669,1438,1576,3648,1147,399,520,31,7|81,142,171,161,184,188,211,207,212,220,261,283,280,307,346,377,446,535,595,540,592,608,596,602,627,630,622,601,557,532,584,632,672,811,809,883,998,1037,1061,955,955,931,967,906,838,735,814,761,786,749,764,724,775,669,630,605,537,514,449,482,397,397,406,362,365,336,313,349,319,281,270,240,264,227,205,194,212,182,178,173,160,163,167,152,147,133,115,94,111,106,87,87,99,80,100,81,73,64,84,61,58,54,59,58,50,46,52,53,52,57,59,58,51,44,51,36,48,49,57,60,60,58,81,92,95,108,141,108,119,153,119,130,131,112,112,124,112,108,114,106,95,114,93,127,104,82,59,71,92,84,70,69,51,49,62,49,48,56,51,44,39,38,45,47,42,37,41,47,45,41,37,45,46,40,33,42,39,46,32,24,27,23,26,30,23,21,24,21,24,13,17,22,15,16,21,10,17,16,17,14,24,17,24,17,18,21,17,14,18,19,14,13,28,18,19,16,22,19,11,29,19,16,26,17,21,20,20,31,30,42,37,68,76,102,140,149,118,110,117,99,95,94,85,104,126,177,357,594,2175,9217,1116,601,327,201,118,24|47,87,79,105,112,140,150,200,266,385,441,494,574,575,576,622,548,579,535,555,570,588,540,566,547,600,557,547,525,457,437,438,430,396,402,390,347,328,333,314,286,326,253,267,262,237,244,195,199,177,191,176,199,177,198,162,150,168,156,147,158,185,176,199,189,197,203,185,198,207,224,192,228,245,295,297,316,365,399,360,362,379,343,383,344,346,313,350,338,366,332,360,377,337,378,410,452,419,413,404,391,453,428,436,432,399,393,380,377,375,401,460,442,452,476,464,451,478,448,446,423,458,422,444,460,368,427,397,388,365,320,340,358,371,354,333,361,361,363,340,314,331,347,367,371,363,356,344,335,320,341,324,288,299,268,227,280,235,217,265,228,283,267,259,220,196,175,205,196,188,209,206,165,202,181,167,194,202,211,214,171,191,212,225,247,218,225,214,211,217,185,191,189,206,176,163,183,157,138,129,117,130,130,125,105,91,99,98,78,63,60,36,23,31,41,73,125,169,162,115,68,55,45,25,23,15,20,12,16,23,17,19,21,20,21,20,12,4,7,2,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		featureCodes=featureCodes.trim().replace(" ", ",");
		webServiceUtil=new WebServiceUtil("SearchByKeyFeatures");//SearchByKeyFeatures
		webServiceUtil.AddPropertyParameter("featureCode",featureCodes);
		webServiceUtil.AddPropertyParameter("userPos","00");
		webServiceUtil.AddPropertyParameter("str_alpha",alpha);
		webServiceUtil.AddPropertyParameter("str_sameDegree",sameDegree);
		try{
			
		SoapObject detail=(SoapObject)webServiceUtil.GetResult().getProperty(0);
		return getGood(detail);
		}
		catch (Exception e) {
			
		}
		return null;
		
	}
	public Good[] SearchByKeyWords(String keyWords,String userPos)
	{
		webServiceUtil=new WebServiceUtil("SearchByKeyWords");
		
		//添加参数
		webServiceUtil.AddPropertyParameter("keyWords",keyWords);
		webServiceUtil.AddPropertyParameter("userPos",userPos);
		SoapObject detail = null;
		try{
			detail=(SoapObject)webServiceUtil.GetResult().getProperty(0);
		}
		catch (Exception e) {
			Log.e("错误", e.getMessage());
		}
		return getGood(detail);
	}
	public String Login(String userName,String password)
	{
		webServiceUtil=new WebServiceUtil("Login");
		webServiceUtil.AddPropertyParameter("userName", userName);
		webServiceUtil.AddPropertyParameter("password", password);
		SoapObject detail=webServiceUtil.GetResult();
		return getResult(detail);
	}
	private String getResult(SoapObject soapObject)
	{
		return soapObject.getProperty(0).toString();
	}
	/*从soapObject获取到goodentity实体表
	 * */
	private Good[] getGood(SoapObject detail)
	{
		Good[] goodEntities=new Good[detail.getPropertyCount()];
		int index=0;
		for(int i=0;i<detail.getPropertyCount();i++)
		{
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

package com.robot_brain.nlu.bean;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


/**
 * 内容摘要：咨询结构体类，主要用来存储咨询的结构化信息
 *
 * @Author：CC
 * @Date：2020-03-10 12:07
 * @Version: V1.0
 */
public class ConsultationInfo {
	private String userId = "";// 用户全局唯一id
	private String consultation = "";// 咨询问题 用户的咨询内容
	private String industry = "";// 行业名称
	private String company = "sys";// 服务的公司名称
	private String application = "";// 应用名称
	private String applicationCode = "";// 应用编码
	private String channel = "";// 渠道名称
	private String channelCode = "";// 渠道编码
	private String accessTime = "";// 访问时间
	private String province = "";// 用户访问的省份
	private String city = "";// 用户访问的城市
	private ArrayList<HashMap<String, ArrayList<String>>> parameterList = new ArrayList<HashMap<String, ArrayList<String>>>();// 关于用户记忆的key-values参数集合

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getConsultation() {
		return consultation;
	}

	public void setConsultation(String consultation) {
		this.consultation = consultation;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ArrayList<HashMap<String, ArrayList<String>>> getParameterList() {
		return parameterList;
	}

	public void setParameterList(
			ArrayList<HashMap<String, ArrayList<String>>> parameterList) {
		this.parameterList = parameterList;
	}


	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	/**
	 * 构造函数
	 */
	public ConsultationInfo() {
	}

	/**
	 * 全拷贝构造函数
	 *
	 * @param consultationInfo
	 */
	public ConsultationInfo(ConsultationInfo consultationInfo) {
		this.userId = consultationInfo.userId;// 用户全局唯一id
		this.consultation = consultationInfo.consultation;// 咨询问题 用户的咨询内容
		this.company = consultationInfo.company;// 服务的公司应用名称
		this.application = consultationInfo.application;// 应用渠道名称
		this.accessTime = consultationInfo.accessTime;// 访问时间
		this.province = consultationInfo.province;// 用户访问的省份
		this.city = consultationInfo.city;// 用户访问的城市
		for (int i = 0; i < consultationInfo.parameterList.size(); i++) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			for (String key : consultationInfo.parameterList.get(i).keySet()) {
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(consultationInfo.parameterList.get(i).get(key));
				map.put(key, list);
			}
			this.parameterList.add(map);
		}
		// --
	}

	/**
	 * 自定义咨询结构体变成string的方法
	 *
	 * @return
	 */
	public String ConsultationInfotoString() {
		return consultation + "==" + company + "==" + userId;
	}

	/**
	 * 根据关键字key，获取参数列表中对应的value值，key一般对应的value是list，如果有多个只获取第一个
	 *
	 * @Author：CC
	 * @Param：key
	 * @Return：String
	 *
	 */
	public String getParameterValueByKey(String key) {
		if (key.equals("channel")) {
			return getApplication();
		}
		ArrayList<String> list = getParameterValuesByKey(key);
		if (list.size() > 0) {
			return list.get(0);
		}
		return "";
	}

	/**
	 * 根据关键字key，获取参数列表中对应的value值，key一般对应的value是list
	 *
	 * @Author：CC
	 * @Param：key
	 * @Return：ArrayList
	 *
	 */
	public ArrayList<String> getParameterValuesByKey(String key) {
		if (key.equals("channel")) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(getApplication());
			return list;
		}
		for (Map<String, ArrayList<String>> parameter : parameterList) {
			if (parameter.containsKey(key)) {
				return parameter.get(key);
			}
		}
		return new ArrayList<String>();
	}

	/**
	 *
	 * 根据关键字key和value，更新parameterList中的值。如果key已存在，新值覆盖旧值。如果key不存在，
	 * value加入到一个list了，直接添加key到list的映射
	 *
	 * @author: CC
	 * @param key
	 * @param value
	 *
	 */
	public void updateParameterValueByKey(String key, String value) {
		if (key.length() == 0)
			return;
		if (parameterList.size() == 0) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			parameterList.add(map);
		}
		HashMap<String, ArrayList<String>> map = parameterList.get(0);
		if (map.containsKey(key)) {
			if (map.get(key).size() == 0) {
				map.get(key).add(value);
			} else {
				map.get(key).clear();
				map.get(key).add(value);
			}
		} else {
			ArrayList<String> list = new ArrayList<String>();
			list.add(value);
			map.put(key, list);
		}
	}

	/**
	 *
	 * 根据关键字key和value，更新parameterList中的值。如果key已存在，新值覆盖旧值。如果key不存在，
	 * value加入到一个list了，直接添加key到list的映射
	 *
	 * @author: CC
	 * @param key
	 * @param valueList
	 */
	public void updateValuesByKey(String key, ArrayList<String> valueList) {
		if (parameterList.size() == 0) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			map.put(key, valueList);
			parameterList.add(map);
		} else {
			parameterList.get(0).put(key, valueList);
		}
	}

	/**
	 * 删除parameterList中对应的key和value值
	 *
	 * @param key
	 */
	public void deleteKey(String key) {
		if (parameterList.size() != 0) {
			for (Map<String, ArrayList<String>> parameter : parameterList) {
				if (parameter.containsKey(key)) {
					parameter.remove(key);
					;
				}
			}
		}
	}

	/**
	 *
	 * 根据关键字key和value，添加到parameterList中的值。如果key已存在，新值添加到key对应的valuelist的最后面。
	 * 如果key不存在，value加入到一个list了，直接添加key到list的映射
	 *
	 * @author: CC
	 * @param key
	 * @param value
	 */
	public void addParameterValueByKey(String key, String value) {
		if (key.length() == 0)
			return;
		if (parameterList.size() == 0) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			parameterList.add(map);
		}
		HashMap<String, ArrayList<String>> map = parameterList.get(0);
		if (map.containsKey(key)) {
			if (!map.containsKey(value)) {
				map.get(key).add(value);
			}
		} else {
			ArrayList<String> list = new ArrayList<String>();
			list.add(value);
			map.put(key, list);
		}

	}

	/**
	 *
	 * 根据关键字key和value，添加parameterList中的值。如果key已存在，valueList中的值添加到key对应的list中，
	 * 如果key不存在，直接添加key到list的映射
	 *
	 * @author: CC
	 * @param key
	 * @param valueList
	 */
	public void addValuesByKey(String key, ArrayList<String> valueList) {
		if (parameterList.size() == 0) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			map.put(key, valueList);
			parameterList.add(map);
		} else {
			ArrayList<String> list = new ArrayList<String>();
			if (parameterList.get(0).containsKey(key)) {
				list = parameterList.get(0).get(key);
				list.addAll(valueList);
			} else {
				list = valueList;
			}
			parameterList.get(0).put(key, list);
		}
	}

	/**
	 *
	 * 将map类型的数据整体加入到parameterList中
	 *
	 * @author: CC
	 * @return void
	 */
	public void addValuesByMap(HashMap<String, ArrayList<String>> map) {
		if (parameterList.size() == 0) {
			parameterList.add(map);
		} else {
			parameterList.get(0).putAll(map);
		}
	}

	/**
	 *
	 * 描述：判断键key是否存在
	 *
	 * @author: CC
	 * @return boolean
	 */
	public boolean IsContainsKeyInParameterList(String key) {
		if (key.equals("channel")) {
			return true;
		}
		if (parameterList.size() == 0)
			return false;
		else {
			for (Map<String, ArrayList<String>> parameter : parameterList) {
				if (parameter.containsKey(key)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * 删除parameterList中的键值对
	 *
	 * @return void
	 */
	public void removeParameterValuesbyKey(String key) {
		if (parameterList.size() > 0) {
			for (Map<String, ArrayList<String>> parameter : parameterList) {
				if (parameter.containsKey(key)) {
					parameter.remove(key);
				}
			}
		}
	}

	/**
	 *
	 * 描述：判断参数是不是为空
	 *
	 * @author:CC
	 * @return Boolean
	 */
	public Boolean parameterListIsEmptry() {
		if (this.parameterList.size() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * 描述：清空对象中所有的内容信息，回复成最原始的默认值
	 *
	 * @author: CC
	 * @return void
	 *
	 */
	public void clear() {
		this.userId = "";// 用户全局唯一id
		this.consultation = "";// 咨询问题 用户的咨询内容
		this.company = "";// 服务的公司应用名称
		this.application = "sys";// 应用渠道名称
		this.accessTime = "";// 访问时间
		this.province = "";// 用户访问的省份
		this.city = "";// 用户访问的城市
		this.parameterList.clear();
	}

	/**
	 *
	 * 获取地市ID
	 *
	 * @author: CC
	 * @return String
	 */
	public String getCityId() {
		return this.getParameterValueByKey("cityID");
	}

}

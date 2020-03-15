package com.robot_brain.nlu.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *用户意图识别的返回的意图的详细信息，并包含后续的回答文本，还有关于用户的信息实体的抽取、识别等信息
 *
 * @author: CC
 *@date： 日期：2020-3-10 时间：上午12:29:58
 */
public class CentralQuestionInfo {
	private String centralQuestionName = "";// 中心问题名称
	private String centralQuestionId = "0";// 中心问题名称id
	private String categoryName = "";// 问题分类名称
	private String categoryId = "";// 问题分类id
	private double probability = 0;// 中心问题概率值
	private String segment = "";// 用户询问匹配此中心问题对应的分词结果
	private ArrayList<HashMap<String, ArrayList<String>>> parameterList = new ArrayList<HashMap<String, ArrayList<String>>>();// 关于用户记忆的key-values参数集合
	private String replyTxt = "";// 回复用户的文本


	public String getCentralQuestionName() {
		return centralQuestionName;
	}

	public void setCentralQuestionName(String centralQuestionName) {
		this.centralQuestionName = centralQuestionName;
	}

	public String getCentralQuestionId() {
		return centralQuestionId;
	}

	public void setCentralQuestionId(String centralQuestionId) {
		this.centralQuestionId = centralQuestionId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public ArrayList<HashMap<String, ArrayList<String>>> getParameterList() {
		return parameterList;
	}

	public void setParameterList(
			ArrayList<HashMap<String, ArrayList<String>>> parameterList) {
		this.parameterList = parameterList;
	}

	public String getReplyTxt() {
		return replyTxt;
	}

	public void setReplyTxt(String replyTxt) {
		this.replyTxt = replyTxt;
	}

	/**
	 * 构造函数
	 */
	public CentralQuestionInfo(){

	}

	/**
	 * 全拷贝构造函数
	 * @param centralQuestionInfo
	 */
	public CentralQuestionInfo(CentralQuestionInfo centralQuestionInfo) {
		this.centralQuestionName = centralQuestionInfo.centralQuestionName;
		this.centralQuestionId = centralQuestionInfo.centralQuestionId;
		this.categoryName = centralQuestionInfo.categoryName;
		this.categoryId = centralQuestionInfo.categoryId;
		this.probability = centralQuestionInfo.probability;
		this.segment = centralQuestionInfo.segment;
		this.replyTxt = centralQuestionInfo.replyTxt;
		for (int i = 0; i < centralQuestionInfo.parameterList.size(); i++) {
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			for (String key : centralQuestionInfo.parameterList.get(i).keySet()) {
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(centralQuestionInfo.parameterList.get(i).get(key));
				map.put(key, list);
			}
			this.parameterList.clear();
			this.parameterList.add(map);
		}
		// --
	}

	/**
	 *
	 * 获取真实的中心问题名称，也是就用户的标准咨询
	 *
	 * @author: CC
	 * @return
	 */
	public String acquireStandardonsultation() {
		String centralQuestionStr = acquireRealCentralQuestionString();
		if (centralQuestionStr.contains("<")
				&& centralQuestionStr.contains(">")) {
			int index = centralQuestionStr.indexOf(">");
			return centralQuestionStr.substring(index + 1,
					centralQuestionStr.length());
		} else {
			return "";
		}
	}

	/**
	 *
	 * 描述：获取真实中心问题名称
	 *
	 * @author: CC
	 * @return String
	 */
	public String acquireRealCentralQuestionString() {
		if (IsContainsKeyInParameterList("realCentralQuestion")) {
			ArrayList<String> list = getParameterValuesByKey("realCentralQuestion");
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return this.centralQuestionName;
	}


	/**
	 *
	 *描述：获取分类名称
	 *
	 * @author: CC
	 *@return String
	 */
	public String acquireRealService() {
		String centralQuestionStr = acquireRealCentralQuestionString();
		if (centralQuestionStr.contains("<") && centralQuestionStr.contains(">")) {
			int indexLeft = centralQuestionStr.indexOf("<");
			int indexRight = centralQuestionStr.indexOf(">");
			if(indexLeft<indexRight)
				return centralQuestionStr.substring(indexLeft+1, indexRight);
			else
				return "";
		} else {
			return "";
		}
	}

	/**
	 *
	 *描述：获取真实中心问题的id
	 * @author: CC
	 *@return int
	 */
	public String acquireRealAbstractID() {
		if (IsContainsKeyInParameterList("realCategoryId")) {
			ArrayList<String> list = getParameterValuesByKey("realCategoryId");
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return categoryId;
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
		ArrayList<String> list = getParameterValuesByKey(key);
		if (list.size() > 0)
			return list.get(0);
		return "";
	}

	/**
	 *
	 * 根据关键字获取值对应的list，如果list存在多个，用"|"连接
	 *
	 * @author: CC
	 * @param key
	 * @return String
	 */
	public String getParameterListValuesByKey(String key) {
		ArrayList<String> list = getParameterValuesByKey(key);
		if (list.size() == 0) {
			return "";
		}
		if (list.size() == 1) {
			return list.get(0);
		} else {
			String result = "";
			for (String value : list) {
				result += value + "|";
			}
			if (result.endsWith("|")) {
				result = result.substring(0, result.length()-1);
			}
			return result;
		}
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
	 * 描述：判断一个中心问题的信息是否是有效的，判断是否有效的标准是问题分类和中心问题
	 *
	 * @author: CC
	 * @return Boolean
	 */
	public Boolean isAvailable() {
		if (this.categoryName.length() > 0
				&& this.centralQuestionName.length() > 0) {
			return true;
		}
		return false;
	}



	/**
	 *
	 *描述：获取parameterList的所有键key集合
	 *
	 * @author: CC
	 *@return ArrayList<String>
	 */
	public ArrayList<String> acquireParameterListKeySet() {
		ArrayList<String> list = new ArrayList<String>();
		if (!this.parameterListIsEmptry()) {
			list.addAll(this.parameterList.get(0).keySet());
		}
		return list;
	}

	/**
	 *
	 *描述：获取键值对形式的Parameter，拷贝一个新的对象出来，不和之前的一样，所以可以任意操作
	 *
	 * @author: CC
	 *@return Map<String,String>
	 */
	public Map<String, String> acquireParameterListKeyValue() {
		Map<String, String> map = new HashMap<String, String>();
		if (!this.parameterListIsEmptry()) {
			ArrayList<String> keySet = this.acquireParameterListKeySet();
			for (String key : keySet) {
				map.put(key, this.getParameterValueByKey(key));
			}
		}
		return map;
	}

}

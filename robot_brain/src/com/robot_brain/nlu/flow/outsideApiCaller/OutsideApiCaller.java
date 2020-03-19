package com.robot_brain.nlu.flow.outsideApiCaller;

import com.robot_brain.nlu.communal.myInterface.StandardModule;
import redis.clients.jedis.Jedis;

import javax.servlet.jsp.jstl.sql.Result;


public class OutsideApiCaller implements StandardModule {
    public void _init_() {
        Result dt;
        try {
            dt = CommonLibMetafieldmappingDAO.select("第三方接口信息配制");
            if (dt == null) {
                GlobalValue.myLog
                        .error("LoadTheThirdInterfaceInfo(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");
                return;
            }
            for (SortedMap<String, String> row : dt.getRows()) {
                CalledPartyInfo partyInfo = null;
                String key = row.get("mkey").toString().trim(); // eg:基金行业->天弘基金->多渠道应用::获取基金净值
                String value = row.get("mvalue").toString().trim();
                if (!S_CalledPartyInfoDic.containsKey(key)) {
                    partyInfo = new CalledPartyInfo();
                    S_CalledPartyInfoDic.put(key, partyInfo);
                } else {
                    partyInfo = S_CalledPartyInfoDic.get(key);
                }

                if (StringUtils.contains(value, ":=")) {
                    int p = value.indexOf(":=");
                    if (p == -1) {
                        continue;
                    }
                    String fieldName = value.substring(0, p); // 对应实体类CalledPartyInfo的属性
                    String fieldValue = value.substring(p + 2); // 对应实体类CalledPartyInfo的属性值
                    if (partyInfo.getInnerFuncName().length() == 0) {
                        ArrayList<String> businessAndFuncName = StringOper
                                .StringSplit(key, "::");
                        if (businessAndFuncName.size() == 2) {
                            partyInfo.setBusiness(businessAndFuncName.get(0));
                            partyInfo.setInnerFuncName(businessAndFuncName
                                    .get(1));
                        }
                    }

                    if (fieldName.equals("CalledRes2InnerResParas")) {
                        if (fieldValue.contains("->")) {
                            int p1 = value.indexOf("->");
                            if (p1 == -1) {
                                continue;
                            }
                            String key1 = value.substring(0, p1);
                            int pos2 = key1.indexOf(":=");
                            if (pos2 != -1) {
                                key1 = key1.substring(pos2 + 2);
                            }
                            String value1 = value.substring(p1 + 2);

                            partyInfo.getCalledRes2InnerResParas().put(key1,
                                    value1);

                        } else if (fieldValue.contains("<-")) {
                            int p2 = value.indexOf("<-");
                            if (p2 == -1) {
                                continue;
                            }
                            String key2 = value.substring(p2 + 2);
                            String value2 = value.substring(0, p2);
                            int pos2 = value2.indexOf(":=");
                            if (pos2 != -1) {
                                value2 = value2.substring(pos2 + 2);
                            }
                            partyInfo.getCalledRes2InnerResParas().put(key2,
                                    value2);
                        }
                    } else if (fieldName.equals("Inner2CalledParasMap")) {
                        if (fieldValue.contains("->")) {
                            int p1 = value.indexOf("->");
                            if (p1 == -1) {
                                continue;
                            }
                            String key1 = value.substring(0, p1);
                            String value1 = value.substring(p1 + 2);
                            int pos2 = value1.indexOf(":=");
                            if (pos2 != -1) {
                                value1 = value1.substring(pos2 + 2);
                            }
                            if (key1.endsWith("()")) {
                                partyInfo.getInner2CalledParasMap().put(value1,
                                        value1);// 如果Key为"()"，表示系统内部参数没有该对应的参数，默认填空；
                            } else
                                partyInfo.getInner2CalledParasMap().put(key1,
                                        value1);
                        } else if (fieldValue.contains("<-")) {
                            int p2 = value.indexOf("<-");
                            if (p2 == -1) {
                                continue;
                            }
                            String key2 = value.substring(p2 + 2);
                            String value2 = value.substring(0, p2);
                            int pos2 = value2.indexOf(":=");
                            if (pos2 != -1) {
                                value2 = value2.substring(pos2 + 2);
                            }
                            if (key2.endsWith("()")) {
                                partyInfo.getInner2CalledParasMap().put(value2,
                                        value2);// 如果Key为"()"，表示系统内部参数没有该对应的参数，默认填空；
                            } else
                                partyInfo.getInner2CalledParasMap().put(key2,
                                        value2);
                        }
                    } else {
                        // 使用类的Set方法，初始化类对象
                        if (isHasField(partyInfo, fieldName)) {
                            // 获取set方法中的参数字段(实体类的属性)
                            Field field = CalledPartyInfo.class
                                    .getDeclaredField(fieldName);
                            // 获取set方法
                            Method method = CalledPartyInfo.class
                                    .getDeclaredMethod("set" + fieldName, field
                                            .getType());
                            // 调用set方法
                            method.invoke(partyInfo, fieldValue);
                        }
                    }

                }
            }
        } catch (Exception e) {
            GlobalValue.myLog.error(
                    "初始化LoadTheThirdInterfaceInfo(第三方接口信息配制)出错，", e);
        }
        //判断redis中key对应value是否为空，非空则清空redis对应value
        Jedis checkvalue = new Jedis();
        //从数据库中查表获取第三方接口配置名，存入javabean结构中
        //
        //        字段	描述
        //        id	主键
        //        interface_name	接口名称
        //        interface_url	接口地址
        //        invocation_way	调用方式（HTTP，WEBSERVICE）
        //        function_name	请求方式（调用方式为http时，为GET、POST；调用方式为webservice时，
        //        namespace	命名空间 调用方式为webservice时填写
        //        req_param	请求参数 多个参数名称以,连接
        //        resp_param	响应参数 多个参数名称以,连接
        //        application_id	应用ID
        //sql: select application_id,invocation_way, function_name, namespace, req_param,resp_param from flow_interface_t where interface_name =?;
        //将结构体转换为json格式存入redis的key下
    }
    /*
    *
    *
    *
     */
    public String _main_(String conversationID) {
        //根据第三方配置名从redis中将json串取出到map中
        //判断map中元素invocation_way判断使用哪种解析接口方式HTTP，WEBSERVICE
        return null;
    }
}

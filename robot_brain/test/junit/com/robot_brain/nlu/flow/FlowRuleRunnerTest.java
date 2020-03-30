package junit.com.robot_brain.nlu.flow;

import com.robot_brain.nlu.flow.FlowRuleRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/** 
* FlowRuleRunner Tester. 
* 
* @author <Authors name> 
* @since <pre>03/23/2020</pre>
* @version 1.0 
*/ 
public class FlowRuleRunnerTest { 

        FlowRuleRunner flowRuleRunner=new FlowRuleRunner();
@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: runRule(String rule, Map<String, String> infoMap) 
* 
*/ 
@Test
public void testRunRule() throws Exception { 
    String ruleTxt="通信网络=本网 and 第一次来电=是 and 计费状态=001 and 余额判断>600 ==> 应答(\"您的话费余额是700分\");信息获取(\"下发短信\",\"参数2\");应答(\"<@结果>\")";
    Map<String,String> infoMap_true=new HashMap<>();
    infoMap_true.put("通信网络","本网");
    infoMap_true.put("第一次来电","是");
    infoMap_true.put("计费状态","001");
    infoMap_true.put("余额判断","790");
    Map<String,String> infoMap_false=new HashMap<>();
    infoMap_false.put("通信网络","本网");
    infoMap_false.put("第一次来电","是");
    infoMap_false.put("计费状态","001");
    infoMap_false.put("余额判断","200");
    flowRuleRunner.runRule(ruleTxt,infoMap_true);
    flowRuleRunner.runRule(ruleTxt,infoMap_false);
    System.out.println(flowRuleRunner.getLog());
    assertEquals("您的话费余额是700分下发短信",infoMap_true.getOrDefault("answer",""));
    assertEquals("",infoMap_false.getOrDefault("answer",""));
}

/** 
* 
* Method: doVerb(String verbName, String[] arrayParas, Map<String, String> infoMap) 
* 
*/ 
@Ignore
public void testDoVerb() throws Exception {
    /*
try {
   Method method = FlowRuleRunner.getClass().getMethod("isTrue", String.class, String.class, String.class, Map<String,.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
} 


/** 
* 
* Method: isTrue(String attr1, String operator, String attr2, Map<String, String> infoMap) 
* 
*/ 
@Ignore
public void testIsTrue() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = FlowRuleRunner.getClass().getMethod("isTrue", String.class, String.class, String.class, Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 

package junit.com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.flow.Verbs.Extend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
* Extend Tester. 
* 
* @author <Authors name> 
* @since <pre>03/17/2020</pre>
* @version 1.0 
*/ 
public class ExtendTest {
    Extend e=new Extend();
@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: variablesReplace(String sentence, Map<String, String> infoMap) 
* 
*/ 
@Test
public void testVariablesReplace() throws Exception { 
    Map<String,String> infoMap=new HashMap<String,String>();
    infoMap.put("省","江苏");
    infoMap.put("市","镇江");
    infoMap.put("","");
    ArrayList<String> testStr=new ArrayList<String>();
    testStr.add("我跟您确认下您的寄件地址是[<@省>省,][<@市>市,][<@区>区,]您可以说是或者不是？");
    testStr.add("我跟您确认下您的寄件地址是<@省>省,[<@市>市,][<@区>区,]您可以说是或者不是？");
    testStr.add("我跟您确认下您的寄件地址是<@省>省,<@市>市,<@区>区，您可以说是或者不是？");
    testStr.add("[<@省>省,]城市：<@市>市,行政区：<@区>区");

    for(String s:testStr) {
        System.out.println(e.variablesReplace(s, infoMap));
    }

}


/** 
* 
* Method: requiredVariablesReplace(String sentence, Map<String, String> infoMap) 
* 
*/ 
@Test
public void testRequiredVariablesReplace() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = Extend.getClass().getMethod("requiredVariablesReplace", String.class, Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 

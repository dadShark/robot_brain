package junit.com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.flow.Verbs.Extend;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

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
* Method: variablesReplace(String sentence, InfoMap infoMap)
*
*/
@Test
public void testVariablesReplace() throws Exception {
    HashMap<String,String> infoMap=new HashMap<>();
    infoMap.put("省","江苏");
    infoMap.put("市","镇江");
    infoMap.put("区","");

    String testStr1=("我跟您确认下您的寄件地址是[<@省>省,][<@市>市,][<@区>区,]您可以说是或者不是？");
    String testStr2=("我跟您确认下您的寄件地址是<@省>省,[<@市>市,][<@区>区,]您可以说是或者不是？");
    String testStr3=("我跟您确认下您的寄件地址是<@省>省,<@市>市,<@区>区，您可以说是或者不是？");
    String testStr4=("[<@省>省,]城市：<@市>市,行政区：<@区>区");

    assertEquals("我跟您确认下您的寄件地址是江苏省,镇江市,您可以说是或者不是？",e.variablesReplace(testStr1,infoMap));
    assertEquals("我跟您确认下您的寄件地址是江苏省,镇江市,您可以说是或者不是？",e.variablesReplace(testStr2,infoMap));
    assertEquals("",e.variablesReplace(testStr3,infoMap));
    assertEquals("",e.variablesReplace(testStr4,infoMap));
}


/**
*
* Method: requiredVariablesReplace(String sentence, InfoMap infoMap)
*
*/
@Ignore
public void testRequiredVariablesReplace() throws Exception {
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

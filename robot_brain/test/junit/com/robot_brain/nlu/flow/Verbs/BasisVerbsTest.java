package junit.com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.flow.Verbs.BasisVerbs;
import com.robot_brain.nlu.flow.temp.SaveLikeRedis;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * BasisVerbs Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>03/23/2020</pre>
 */
public class BasisVerbsTest {
    BasisVerbs bVerbs = new BasisVerbs();
    SaveLikeRedis saveLikeRedis=new SaveLikeRedis();//初始化内存数据

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: answer(String[] arrayParas, InfoMap infoMap)
     */
    @Test
    public void testAnswer() throws Exception {
        HashMap<String,String> infoMap = new HashMap<>();
        infoMap.put("省", "江苏");
        infoMap.put("市", "镇江");
        infoMap.put("区", "京口");
        String[] args = new String[]{"我跟您确认下您的寄件地址是[<@省>省,][<@市>市,][<@区>区,]您可以说是或者不是？"};
        bVerbs.answer(args, infoMap);
        assertEquals("我跟您确认下您的寄件地址是江苏省,镇江市,京口区,您可以说是或者不是？", infoMap.get("answer"));
    }

/**
*
* Method: informationRetrieval(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Ignore
public void testInformationRetrieval() throws Exception {
}

/**
*
* Method: setInfo(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testSetInfo() throws Exception {
    HashMap<String,String> infoMap = new HashMap<>();
    String[] args = new String[]{"新增","2020年4月2日"};
    bVerbs.setInfo(args,infoMap);
    assertEquals("2020年4月2日",infoMap.get("新增"));
}

/**
*
* Method: next(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testNext() throws Exception {
    HashMap<String,String> infoMap = new HashMap<>();
    //流程中断测试
    String[] args1 = new String[]{"break"};
    infoMap.put("流程ID","1.1");
    bVerbs.next(args1,infoMap);
    assertEquals("break",infoMap.get("flowState"));
    assertEquals(null,infoMap.get("当前节点名"));
    //跳转到不存在节点测试
    String[] args2 = new String[]{"11"};
    infoMap.clear();
    infoMap.put("流程ID","1.1");
    bVerbs.next(args2,infoMap);
    assertEquals(null,infoMap.get("flowState"));
    assertEquals(null,infoMap.get("当前节点名"));
    //跳转到现有节点测试
    String[] args3 = new String[]{"2"};
    infoMap.clear();
    infoMap.put("流程ID","1.1");
    bVerbs.next(args3,infoMap);
    assertEquals("2",infoMap.get("当前节点名"));
    assertEquals("goto",infoMap.get("flowState"));
}

/**
*
* Method: nextFlow(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testNextFlow() throws Exception {
    Map<String,String> infoMap=new HashMap<>();
    //流程名称存在
    String[] args1=new String[]{"入口：询问用户类型"};
    bVerbs.nextFlow(args1,infoMap);
    assertEquals("1.1",infoMap.get("流程ID"));
    assertEquals("入口：询问用户类型",infoMap.get("流程名称"));
    assertEquals("goto",infoMap.get("flowState"));
    //流程名称不存在
    infoMap.clear();
    String[] args2=new String[]{"询问用户类型"};
    assertEquals(null,infoMap.get("流程ID"));
    assertEquals(null,infoMap.get("流程名称"));
    assertEquals(null,infoMap.get("flowState"));
}

/**
*
* Method: offerMenu(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testOfferMenu() throws Exception {
    Map<String,String> infoMap=new HashMap<>();
    infoMap.put("名下车牌号","苏L13322||苏B34423||苏E15654");
    String[] args1=new String[]{"请选择其中一个车牌号：<@选项文本>","<@名下车牌号>"};
    bVerbs.offerMenu(args1,infoMap);
    assertEquals("请选择其中一个车牌号：[1]苏L13322 [2]苏B34423 [3]苏E15654",infoMap.get("answer"));
}

/**
*
* Method: endDialog(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testEndDialog() throws Exception {
    Map<String,String> infoMap=new HashMap<>();
    String[] args=new String[]{};
    bVerbs.endDialog(args,infoMap);
    assertEquals("true",infoMap.get("流程结束标志"));
    assertEquals("true",infoMap.get("会话结束标志"));
    assertEquals("break",infoMap.get("flowState"));
}

/**
*
* Method: endFlow(String[] arrayParas, Map<String, String> infoMap)
*
*/
@Test
public void testEndFlow() throws Exception {
    Map<String,String> infoMap=new HashMap<>();
    String[] args=new String[]{};
    bVerbs.endFlow(args,infoMap);
    assertEquals("true",infoMap.get("流程结束标志"));
    assertEquals(null,infoMap.get("会话结束标志"));
    assertEquals("break",infoMap.get("flowState"));
}


} 

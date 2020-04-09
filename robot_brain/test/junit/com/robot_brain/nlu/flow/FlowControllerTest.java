package junit.com.robot_brain.nlu.flow;

import com.robot_brain.nlu.flow.FlowController;
import com.robot_brain.nlu.flow.temp.SaveLikeRedis;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * FlowController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>04/06/2020</pre>
 */
public class FlowControllerTest {
    SaveLikeRedis saveLikeRedis = new SaveLikeRedis();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: runningFlow(@NotNull Map<String, String> infoMap)
     */
    @Ignore
    public void testRunningFlow() throws Exception {

    }

    /**
     * 极端测试用例测试
     * Method: runningFlow(@NotNull Map<String, String> infoMap)
     */
    @Test
    public void testRunningFlowExtreme() throws Exception {
        Map<String, String> infoMap = new HashMap<>();
        FlowController flowController = new FlowController();
        flowController.runningFlow(infoMap);
        System.out.println(flowController.getLog());
        infoMap.put("流程名称", "话费查询");
        FlowController flowController2 = new FlowController();
        flowController2.runningFlow(infoMap);
        System.out.println(flowController2.getLog());
    }


    /**
     * Method: loadNodeRules(String flowID, String nodeID)
     */
    @Ignore
    public void testLoadNodeRules() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = FlowController.getClass().getMethod("loadNodeRules", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: loadFlowHistory(String dialogID)
     */
    @Ignore
    public void testLoadFlowHistory() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = FlowController.getClass().getMethod("loadFlowHistory", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: saveFlowHistory(String dialogID, FlowRunningHistory history)
     */
    @Ignore
    public void testSaveFlowHistory() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = FlowController.getClass().getMethod("saveFlowHistory", String.class, FlowRunningHistory.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: loadFlowIDbyFlowName(String flowName)
     */
    @Ignore
    public void testLoadFlowIDbyFlowName() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = FlowController.getClass().getMethod("loadFlowIDbyFlowName", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 

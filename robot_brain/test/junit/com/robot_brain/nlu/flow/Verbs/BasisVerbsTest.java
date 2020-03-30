package junit.com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.flow.Verbs.BasisVerbs;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

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
     * Method: informationRetrieval(String[] arrayParas, InfoMap infoMap)
     */
    @Ignore
    public void testInformationRetrieval() throws Exception {
    }


} 

package com.robot_brain.nlu.flow.kit;

import com.robot_brain.nlu.flow.outsideApiCaller.OutsideApiCaller;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GenericUntilTest {

    @Test
    public void getGlobalProfileInfo() {
        assertEquals("10",GenericUntil.getGlobalProfileInfo("redisMaxTotal"));
    }

    @Test
    public void getXMLInfo() {
        assertEquals("10",GenericUntil.getXMLInfo("redisMaxTotal","GlobalDeploy"));
    }

    @Test
    public void getAppPath() {
        System.out.println(GenericUntil.getAppPath(OutsideApiCaller.class,"GlobalDeploy"));
    }

    @Test
    public void error() {
    }
}
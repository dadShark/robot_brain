package com.robot_brain.nlu.flow.kit;

import jdk.nashorn.internal.objects.Global;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GenericUntilTest {
    GenericUntil genericUntil =new GenericUntil();

    @Test
    public void getGlobalProfileInfo() {
        String str = GenericUntil.getGlobalProfileInfo("connectFrom");
        System.out.println(str);
    }

    @Test
    public void getXMLInfo() {
        String str = GenericUntil.getXMLInfo("connectFrom","GlobalDeploy");
        System.out.println(str);


    }

    @Test
    public void getAppPath() {
        String str =genericUntil.getAppPath(GenericUntil.class,"GlobalDeploy");
        System.out.println(str);
    }

    @Test
    public void getGlobalInfo() {
    }
}
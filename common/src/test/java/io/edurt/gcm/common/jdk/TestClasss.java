package io.edurt.gcm.common.jdk;

import io.edurt.gcm.common.annotation.TestAnnotation;
import org.junit.Test;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class TestClasss
{

    @Test
    public void scanClassInPackage()
    {
        Classs.scanClassInPackage("")
                .forEach(System.out::println);
    }

    @Test
    public void scanJarInProject()
            throws IOException
    {
        Enumeration<URL> urls = Classs.scanJarInProject();
        while (urls.hasMoreElements()) {
            System.out.println(urls.nextElement().getPath());
        }
    }

    @Test
    public void test()
            throws Exception
    {
        Classs.scanClassesInProject(TestAnnotation.class).forEach(System.out::println);
    }
}
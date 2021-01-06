package io.edurt.gcm.common.jdk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * ObjectBuilder Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 6, 2021</pre>
 */
public class TestObjectBuilder
{
    @Before
    public void before()
    {
    }

    @After
    public void after()
    {
    }

    /**
     * Method: of(Supplier<T> supplier)
     */
    @Test
    public void testOf()
    {
        ObjectBuilder.of(TestClass::new).build();
        assertTrue(true);
    }

    /**
     * Method: with(Consumers<T, List<Object>> consumer, List<Object> list)
     */
    @Test
    public void testWith()
    {
        ObjectBuilder.of(TestClass::new).with(TestClass::printMessage, "Hello").build();
        assertTrue(true);
    }

    /**
     * Method: build()
     */
    @Test
    public void testBuild()
    {
    }

    /**
     * Method: accept(T t, List p)
     */
    @Test
    public void testAccept()
    {
    }
}

class TestClass
{
    public void printMessage(String message)
    {
        System.out.println(message);
    }
}

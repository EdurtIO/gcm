package io.edurt.gcm.common.jdk;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.String.join;
import static java.lang.String.valueOf;

/**
 * Iterables Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 5, 2021</pre>
 */
public class TestIterables
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
     * Method: forEach(Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action)
     */
    @Test
    public void testForEach()
    {
        Iterables.forEach(Arrays.asList("Hello", "Gcm"), (index, value) -> {
            System.out.println(join(":", valueOf(index), value));
        });
        Assert.assertTrue(Boolean.TRUE);
    }
} 

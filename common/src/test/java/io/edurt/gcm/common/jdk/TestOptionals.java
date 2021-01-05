package io.edurt.gcm.common.jdk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Optionals Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 5, 2021</pre>
 */
public class TestOptionals
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
     * Method: isHasValue(Optional value)
     */
    @Test
    public void testIsHasValue()
    {
        Optional<String> source = Optional.of("Hello");
        Optional<String> empty = Optional.of("");
        assertTrue(Optionals.isHasValue(source));
        assertFalse(Optionals.isHasValue(empty));
    }
} 

package io.edurt.gcm.common.jdk;

import org.junit.Test;

public class TestSwitch
{
    @Test
    public void test()
    {
        Switch.on("Java")
                .is("Java")
                .iIf(v -> System.out.println("Java"))
                .is("Switch")
                .iIf(v -> System.out.println("Switch"))
                .iElse(v -> System.out.println("else"));
    }
}

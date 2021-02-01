/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.edurt.gcm.common.jdk;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Switch<T, R>
{
    private final T input;
    private Predicate<T> condition;
    // Has the tag been executed
    //  True: executed
    //  False: not yet executed
    private boolean isExecute;

    private Switch(T input)
    {
        this.input = input;
    }

    public static <T, R> Switch<T, R> on(T value)
    {
        return new Switch<>(value);
    }

    public Switch<T, R> is(T target)
    {
        return when(Predicate.isEqual(target));
    }

    /**
     * Process and record the compliance of conditions
     *
     * @param condition Whether the conditional logic is met or not
     * @return instance
     */
    public Switch<T, R> when(Predicate<T> condition)
    {
        this.condition = condition;
        return this;
    }

    /**
     * Processing logic when certain conditions are met
     */
    public Switch<T, R> iIf(Consumer<T> action)
    {
        if (condition.test(input)) {
            action.accept(input);
            // The tag was executed
            isExecute = true;
        }
        return this;
    }

    /**
     * An operation performed when all conditions are not met
     */
    public void iElse(Consumer<T> action)
    {
        if (isExecute) {
            // If it has been executed, it will return directly.
            // If it has not been executed, it will perform subsequent operations
            return;
        }
        action.accept(input);
    }
}

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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectBuilder<T>
{
    private final Supplier<T> supplier;
    private List<Consumer<T>> modifiers = new ArrayList<>();

    public ObjectBuilder(Supplier<T> supplier)
    {
        this.supplier = supplier;
    }

    public static <T> ObjectBuilder<T> of(Supplier<T> supplier)
    {
        return new ObjectBuilder<>(supplier);
    }

    public <P> ObjectBuilder<T> with(Consumers<T, P> consumer, P p)
    {
        Consumer<T> c = instance -> consumer.accept(instance, p);
        modifiers.add(c);
        return this;
    }

    /**
     * builder class instance
     *
     * @return instance
     */
    public T build()
    {
        T value = supplier.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    @FunctionalInterface
    public interface Consumers<T, P>
    {
        void accept(T t, P p);
    }
}

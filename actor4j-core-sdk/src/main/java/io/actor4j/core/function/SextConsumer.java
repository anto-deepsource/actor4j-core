/*
 * Copyright (c) 2015-2022, David A. Bauer. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.actor4j.core.function;

import java.util.Objects;

@FunctionalInterface
public interface SextConsumer<T, U, V, W, X, Y> {
	void accept(T t, U u, V v, W w, X x, Y y);

    default SextConsumer<T, U, V, W, X, Y> andThen(SextConsumer<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y> after) {
        Objects.requireNonNull(after);

        return (t, u, v, w, x, y) -> {
            accept(t, u, v, w, x, y);
            after.accept(t, u, v, w, x, y);
        };
    }
}

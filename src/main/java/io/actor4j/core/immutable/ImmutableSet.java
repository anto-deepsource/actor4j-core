/*
 * Copyright (c) 2015-2020, David A. Bauer. All rights reserved.
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
package io.actor4j.core.immutable;

import java.util.Collections;
import java.util.Set;

import io.actor4j.core.utils.Shareable;

public class ImmutableSet<T> implements Shareable {
	protected final Set<T> set;
	
	public ImmutableSet() {
		super();
		
		this.set = Collections.emptySet();
	}
	
	public ImmutableSet(Set<T> set) {
		super();
		
		this.set = Collections.unmodifiableSet(set);
	}

	public Set<T> get() {
		return set;
	}

	@Override
	public String toString() {
		return "ImmutableSet [set=" + set + "]";
	}
}

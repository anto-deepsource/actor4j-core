/*
 * Copyright (c) 2015-2017, David A. Bauer. All rights reserved.
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
package io.actor4j.core.actors;

import io.actor4j.core.messages.ActorMessage;
import io.actor4j.core.utils.Cache;
import io.actor4j.core.utils.CacheLRUWithGC;

import static io.actor4j.core.utils.ActorUtils.*;

public class ActorWithCache<K, V> extends Actor {
	protected int cacheSize;
	protected Cache<K, V> cache;
	
	public static final int GC      = checkTag(300);
	public static final int EVICT   = GC;
	public static final int GET     = checkTag(301);
	public static final int SET     = checkTag(302);
	public static final int UPDATE  = checkTag(303);
	public static final int DEL     = checkTag(304);
	public static final int DEL_ALL = checkTag(305);
	public static final int CLEAR   = checkTag(306);
	public static final int CAS     = checkTag(307); // CompareAndSet
	public static final int CAU     = checkTag(308); // CompareAndUpdate
	
	public static final int SUBSCRIBE_SECONDARY = checkTag(309);
	
	public ActorWithCache(String name, int cacheSize) {
		super(name);
		
		this.cacheSize = cacheSize;
		cache = new CacheLRUWithGC<>(cacheSize);
	}
	
	public ActorWithCache(int cacheSize) {
		this(null, cacheSize);
	}
	
	@Override
	public void receive(ActorMessage<?> message) {
		if (message.value()!=null && message.tag()==GC)
			cache.gc(message.valueAsLong());
	}
}

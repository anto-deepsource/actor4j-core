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
package io.actor4j.core.persistence;

import java.util.UUID;

import io.actor4j.core.utils.Shareable;

public class ActorPersistenceObject implements Shareable {
	public UUID persistenceId;
	public long timeStamp;
	public int index; // only used, if it has the same timestamp as the last one

	public ActorPersistenceObject() {
		super();
		timeStamp = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "ActorPersistenceObject [persistenceId=" + persistenceId + ", timeStamp=" + timeStamp + ", index="
				+ index + "]";
	}
}

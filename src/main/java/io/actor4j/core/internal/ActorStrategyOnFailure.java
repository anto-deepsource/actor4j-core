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
package io.actor4j.core.internal;

import static io.actor4j.core.internal.protocols.ActorProtocolTag.*;
import static io.actor4j.core.logging.ActorLogger.*;
import static io.actor4j.core.supervisor.SupervisorStrategyDirective.*;
import static io.actor4j.core.utils.ActorUtils.actorLabel;

import java.util.Iterator;
import java.util.UUID;

import io.actor4j.core.messages.ActorMessage;
import io.actor4j.core.supervisor.OneForAllSupervisorStrategy;
import io.actor4j.core.supervisor.OneForOneSupervisorStrategy;
import io.actor4j.core.supervisor.SupervisorStrategy;
import io.actor4j.core.supervisor.SupervisorStrategyDirective;

public class ActorStrategyOnFailure {
	protected final ActorSystemImpl system;
	
	public ActorStrategyOnFailure(ActorSystemImpl system) {
		this.system = system;
	}
	
	protected void oneForOne_directive_resume(ActorCell cell) {
		systemLogger().log(INFO, String.format("[LIFECYCLE] actor (%s) resumed", actorLabel(cell.actor)));
	}
	
	protected void oneForOne_directive_restart(ActorCell cell, Exception reason) {
		cell.preRestart(reason);
	}
	
	protected void oneForOne_directive_stop(ActorCell cell) {
		cell.stop();
	}
	
	protected void oneForAll_directive_resume(ActorCell cell) {
		oneForOne_directive_resume(cell);
	}
	
	protected void oneForAll_directive_restart(ActorCell cell, Exception reason) {
		if (!cell.isRoot()) {
			ActorCell parent = system.cells.get(cell.parent);
			if (parent!=null) {
				Iterator<UUID> iterator = parent.children.iterator();
				while (iterator.hasNext()) {
					UUID dest = iterator.next();
					if (!dest.equals(cell.id))
						system.sendAsDirective(ActorMessage.create(reason, INTERNAL_RESTART, parent.id, dest));
				}
				cell.preRestart(reason);
			}
		}
		else 
			cell.preRestart(reason);
	}
	
	protected void oneForAll_directive_stop(ActorCell cell) {
		if (!cell.isRoot()) {
			ActorCell parent = system.cells.get(cell.parent);
			if (parent!=null) {
				Iterator<UUID> iterator = parent.children.iterator();
				while (iterator.hasNext()) {
					UUID dest = iterator.next();
					if (!dest.equals(cell.id))
						system.sendAsDirective(ActorMessage.create(null, INTERNAL_STOP, parent.id, dest));
				}
				cell.stop();
			}
		}
		else 
			cell.stop();
	}
	
	public void handle(ActorCell cell, Exception e) {
		ActorCell parent = system.cells.get(cell.parent);
		if (cell.parentSupervisorStrategy==null)	
			cell.parentSupervisorStrategy = parent.supervisorStrategy();
		
		SupervisorStrategy supervisorStrategy = cell.parentSupervisorStrategy;
		SupervisorStrategyDirective directive = supervisorStrategy.handle(e);
		
		while (directive==ESCALATE && !parent.isRoot()) {
			parent = system.cells.get(parent.parent);
			supervisorStrategy = cell.parentSupervisorStrategy = parent.supervisorStrategy();
			directive = supervisorStrategy.handle(e);
		}
		
		if (supervisorStrategy instanceof OneForOneSupervisorStrategy) { 
			if (directive==RESUME)
				oneForOne_directive_resume(cell);
			else if (directive==RESTART)
				oneForOne_directive_restart(cell, e);
			else if (directive==STOP)
				oneForOne_directive_stop(cell);
		}
		else if (supervisorStrategy instanceof OneForAllSupervisorStrategy) { 
			if (directive==RESUME)
				oneForAll_directive_resume(cell);
			else if (directive==RESTART)
				oneForAll_directive_restart(cell, e);
			else if (directive==STOP)
				oneForAll_directive_stop(cell);
		}
	}
}

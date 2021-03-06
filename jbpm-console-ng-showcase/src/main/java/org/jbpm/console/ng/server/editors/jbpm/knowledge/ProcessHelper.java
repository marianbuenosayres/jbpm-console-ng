/*
 * Copyright 2012 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.console.ng.server.editors.jbpm.knowledge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jbpm.console.ng.client.model.ProcessSummary;
import org.drools.definition.process.Process;
/**
 *
 * @author salaboy
 */
public class ProcessHelper {
    public static Collection<ProcessSummary> adaptCollection(Collection<Process> processes){
        List<ProcessSummary> processesSummary = new ArrayList<ProcessSummary>();
        for(Process p : processes){
            processesSummary.add(new ProcessSummary(p.getId(), p.getName(),p.getPackageName(), p.getType(), p.getVersion()));
        }
        
        return processesSummary;
    }
    
    public static ProcessSummary adapt(Process p){
        return new ProcessSummary(p.getId(), p.getName(),p.getPackageName(), p.getType(), p.getVersion());
    }
}

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
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.console.ng.client.model.ProcessInstanceSummary;

/**
 *
 * @author salaboy
 */
public class ProcessInstanceHelper {
    public static Collection<ProcessInstanceSummary> adaptCollection(Collection<ProcessInstance> processInstances){
        List<ProcessInstanceSummary> processInstancesSummary = new ArrayList<ProcessInstanceSummary>();
        for(ProcessInstance pi : processInstances){
            processInstancesSummary.add(new ProcessInstanceSummary(pi.getId(), pi.getProcessId(), 
                    pi.getProcessName(), pi.getState()));
        }
        
        return processInstancesSummary;
    }
    
    public static ProcessInstanceSummary adapt(ProcessInstance processInstance){
        return new ProcessInstanceSummary(processInstance.getId(), processInstance.getProcessId(), 
                processInstance.getProcessName(), processInstance.getState());
    }
}

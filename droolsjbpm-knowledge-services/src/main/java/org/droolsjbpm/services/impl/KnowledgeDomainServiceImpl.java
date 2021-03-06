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
package org.droolsjbpm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.droolsjbpm.services.api.KnowledgeDomainService;
import org.jbpm.task.wih.CDIHTWorkItemHandler;
import org.drools.definition.process.Process;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class KnowledgeDomainServiceImpl implements KnowledgeDomainService{

    Map<String, StatefulKnowledgeSession> ksessions = new HashMap<String, StatefulKnowledgeSession>();
    
    @Inject 
    private CDIHTWorkItemHandler handler;
    private Long   id;
    private String domainName;
    private Long   parentId;
    
    
    @PostConstruct
    public void init(){
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //kbuilder.add(new ClassPathResource(""), ResourceType.DRL);
        kbuilder.add(new ClassPathResource("example/humanTask.bpmn"), ResourceType.BPMN2);
        
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        
        handler.setSession(ksession);
        handler.init();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
        
        ksessions.put("default", ksession);
        
        
        
    }
    
    public void registerSession(String businessKey, StatefulKnowledgeSession ksession) {
        ksessions.put(businessKey, ksession);
    }

    public StatefulKnowledgeSession getSession(long sessionId) {
        throw new NotImplementedException();
    }

    public StatefulKnowledgeSession getSessionByBusinessKey(String businessKey) {
        return ksessions.get(businessKey);
    }

    
    public Collection<StatefulKnowledgeSession> getSessions() {
        return ksessions.values();
    }

    public Collection<String> getSessionsNames() {
        return ksessions.keySet();
    }
    
    public int getAmountOfSessions() {
        return ksessions.size();
    }
    
    public Collection<ProcessInstance> getProcessInstances(){
        List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
        for(StatefulKnowledgeSession ksession : ksessions.values()){
            processInstances.addAll(ksession.getProcessInstances());
        }
        return processInstances;
    }
    
    public Collection<ProcessInstance> getProcessInstancesBySessionId(String sessionId){
        StatefulKnowledgeSession ksession = ksessions.get(sessionId);
        return ksession.getProcessInstances();
    }
    
    public Collection<Process> getProcessesBySessionId(String sessionId){
        StatefulKnowledgeSession ksession = ksessions.get(sessionId);
        return ksession.getKnowledgeBase().getProcesses();
    }
    
     public Collection<Process> getProcesses(){
        List<Process> processes = new ArrayList<Process>();
        for(StatefulKnowledgeSession ksession : ksessions.values()){
            processes.addAll(ksession.getKnowledgeBase().getProcesses());
        }
        return processes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
     
     
    
    
}

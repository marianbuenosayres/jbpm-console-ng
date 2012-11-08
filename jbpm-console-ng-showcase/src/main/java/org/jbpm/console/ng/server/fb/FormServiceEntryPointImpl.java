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
package org.jbpm.console.ng.server.fb;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.console.ng.shared.fb.FormServiceEntryPoint;
import org.jbpm.console.ng.shared.fb.events.PaletteItemAddedEvent;
import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.services.api.FileException;
import org.jbpm.form.builder.services.api.FileService;
import org.jbpm.form.builder.services.api.FormBuilderService;
import org.jbpm.form.builder.services.api.FormDisplayService;
import org.jbpm.form.builder.services.api.MenuService;
import org.jbpm.form.builder.services.api.MenuServiceException;

/**
 *
 * @author salaboy
 */
@Service
@ApplicationScoped
public class FormServiceEntryPointImpl implements FormServiceEntryPoint { 

    @Inject
    private MenuService menuService;
    @Inject
    private FileService fileService;
    @Inject
    private FormDisplayService displayService;
    
    @Inject 
    private FormBuilderService formService;
    
    @Inject
    Event<PaletteItemAddedEvent> itemAddedEvents;
    
    @Override
    public List<Map<String, Object>> listOptions() throws MenuServiceException {
        return menuService.listOptionsGWT();
    }

    @Override
    public void listMenuItems() throws MenuServiceException {
        Map<String, List<Map<String, Object>>> listMenuItems = menuService.listMenuItemsGWT();
        
        try {
            for (String groupName : listMenuItems.keySet()) {
                for (Map<String, Object> itemDesc : listMenuItems.get(groupName)) {
                        itemAddedEvents.fire(new PaletteItemAddedEvent(itemDesc, groupName));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(FormServiceEntryPointImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Map<String, String> getFormBuilderProperties() throws MenuServiceException {
        return menuService.getFormBuilderProperties();
    }

    public String storeFile(String packageName, String fileName, byte[] content) throws FileException {
        return fileService.storeFile(packageName, fileName, content);
    }

    public void deleteFile(String packageName, String fileName) throws FileException {
        fileService.deleteFile(packageName, fileName);
    }

    public List<String> loadFilesByType(String packageName, String fileType) throws FileException {
        return fileService.loadFilesByType(packageName, fileType);
    }

    public byte[] loadFile(String packageName, String fileName) throws FileException {
        return fileService.loadFile(packageName, fileName);
    }

    public String getFormDisplay(long taskId) {
        return displayService.getFormDisplay(taskId);
    }

  

    public String saveForm(Map<String, Object> form) {
        try {
            return formService.saveFormGWT(form);
        } catch (FormBuilderException ex) {
            Logger.getLogger(FormServiceEntryPointImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveFormItem(Map<String, Object> formItem, String formItemName) {
        try {
            formService.saveFormItemGWT(formItem, formItemName);
        } catch (FormBuilderException ex) {
            Logger.getLogger(FormServiceEntryPointImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, Object> loadForm(String json)  {
        return formService.loadForm(json);
    }
    
   
    
    
}

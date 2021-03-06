/*
 * Copyright 2011 JBoss Inc 
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
package org.jbpm.console.ng.client.fb.view.palette;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.jbpm.console.ng.shared.fb.FormServiceEntryPoint;
import org.jbpm.console.ng.client.fb.command.DisposeDropController;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.shared.api.RepresentationFactory;
import org.jbpm.form.builder.services.api.MenuServiceException;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.client.workbench.widgets.dnd.WorkbenchPickupDragController;

@Dependent
@WorkbenchScreen(identifier = "Form Builder - Palette")
public class FormBuilderPalettePresenter {

    @Inject
    private FormBuilderView view;
    @Inject
    private Caller<FormServiceEntryPoint> formServices;
    
    @Inject WorkbenchPickupDragController dndController;

    public interface FormBuilderView
            extends
            UberView<FormBuilderPalettePresenter> {

        ScrollPanel getMenuView();

        
        AbsolutePanel getPanel();
    }

    @PostConstruct
    public void init() {
        CommonGlobals.getInstance().registerI18n((I18NConstants) GWT.create(I18NConstants.class));
        PickupDragController dragController = new PickupDragController(dndController.getBoundaryPanel(), true);
        dragController.registerDropController(new DisposeDropController(view.getPanel()));
        CommonGlobals.getInstance().registerDragController(dragController);
        try {
            formServices.call(new RemoteCallback<Map<String, String>>() {
                @Override
                public void callback(Map<String, String> properties) {
                    for (String key : properties.keySet()) {
                        RepresentationFactory.registerItemClassName(key, properties.get(key));
                    }

                }
            }).getFormBuilderProperties();
            
            
            
            formServices.call(new RemoteCallback<Void>() {
                @Override
                public void callback(Void nothing) {
                    
                }
            }).listMenuItems();


        } catch (MenuServiceException ex) {
            Logger.getLogger(FormBuilderPalettePresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @WorkbenchPartTitle
    public String getTitle() {
        return "Form Builder - Palette";
    }

    @WorkbenchPartView
    public UberView<FormBuilderPalettePresenter> getView() {
        return view;
    }
}

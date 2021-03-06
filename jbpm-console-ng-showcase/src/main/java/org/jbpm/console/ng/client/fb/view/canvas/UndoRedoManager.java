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
package org.jbpm.console.ng.client.fb.view.canvas;

import java.util.LinkedList;
import java.util.List;


import com.google.gwt.event.shared.EventBus;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.bus.UndoableEvent;
import org.jbpm.form.builder.ng.model.client.bus.UndoableHandler;
import org.jbpm.form.builder.ng.model.client.bus.ui.UndoRedoEvent;

/**
 * Handles undo / redo actions 
 */
public class UndoRedoManager {

    /* static methods */
    
    private static final UndoRedoManager INSTANCE = new UndoRedoManager();

    private final EventBus bus = CommonGlobals.getInstance().getEventBus();
    
    public static UndoRedoManager getInstance() {
        return INSTANCE;
    }
    
    /* instance methods */
    
    private List<UndoableEvent> undoRedoWindow = new LinkedList<UndoableEvent>();
    private int index = -1;
    
    /**
     * Registers itself to listen every {@link UndoableEvent}
     */
    private UndoRedoManager() {
        bus.addHandler(UndoableEvent.TYPE, new UndoableHandler() {
            @Override
            public void onEvent(UndoableEvent event) {
                syncAdd(event);
            }
            @Override
            public void undoAction(UndoableEvent event) { }
            @Override
            public void doAction(UndoableEvent event) { }
        });
    }
    
    protected synchronized void syncAdd(UndoableEvent event) {
        this.index++;
        while (this.index < this.undoRedoWindow.size()) { //delete all posterior actions when a new action happens
            this.undoRedoWindow.remove(this.index);
        }
        this.undoRedoWindow.add(event);
    }
    
    /**
     * gets the previous action in the history and reverts it
     */
    public synchronized void undo() {
        if (canUndo()) {
            UndoableEvent event = undoRedoWindow.get(index);
            index--;
            event.getRollbackHandler().undoAction(event);
            bus.fireEvent(new UndoRedoEvent());
        }
    }
    
    /**
     * gets the next action in the history and redoes it
     */
    public synchronized void redo() {
        if (canRedo()) {
            index++;
            UndoableEvent event = undoRedoWindow.get(index);
            event.getRollbackHandler().doAction(event);
            bus.fireEvent(new UndoRedoEvent());
        }
    }
    
    public boolean canUndo() {
        return undoRedoWindow.size() > 0 && index >= 0;
    }
    
    public boolean canRedo() {
        return undoRedoWindow.size() > 0 && index < (undoRedoWindow.size() - 1);
    }
}

/*
 * Copyright 2016 David Aarons.
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
package com.daarons.util;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;

/**
 *
 * @author David
 */
public class HandleTab implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            TextArea text = (TextArea) event.getSource();
            TextAreaSkin skin = (TextAreaSkin) text.getSkin();
            if (skin.getBehavior() instanceof TextAreaBehavior) {
                TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                behavior.callAction("TraverseNext");
                event.consume();
            }
        }
    }

}

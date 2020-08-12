/*******************************************************************************
 * Copyright 2020 Ramkumar Yoganathan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.algo.trading.autobot.carpe.diem.config;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;

public class Notifications extends TrayIcon
{

    private final PopupMenu menuPopup;

    private final SystemTray systemTray;

    public Notifications(final String imagePath, final String toolTip)
    {
        super(createImage(imagePath, toolTip), toolTip);
        menuPopup = new PopupMenu();
        systemTray = SystemTray.getSystemTray();
    }

    @PostConstruct
    private void setup() throws AWTException
    {
        setPopupMenu(menuPopup);
        systemTray.add(this);
    }

    protected static Image createImage(final String path, final String description)
    {
        final URL imageURL = Notifications.class.getResource(path);
        return new ImageIcon(imageURL, description).getImage();
    }
}

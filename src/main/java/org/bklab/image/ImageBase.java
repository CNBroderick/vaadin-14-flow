/*
 * Class: org.bklab.image.ImageBase
 * Modify date: 2020/3/20 上午10:47
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.image;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

public class ImageBase {
    public static Image getImage(String name) {
        return new Image(getResource(name), name);
    }

    public static Image getImage(String name, String alt) {
        return new Image(getResource(name), alt);
    }

    public static AbstractStreamResource getResource(String name) {
        return new StreamResource(name, () -> ImageBase.class.getResourceAsStream(name));
    }
}

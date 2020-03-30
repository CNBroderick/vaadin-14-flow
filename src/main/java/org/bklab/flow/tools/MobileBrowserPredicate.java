/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 10:20:01
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.tools.MobileBrowserPredicate
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.tools;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;

import java.util.function.Predicate;

public class MobileBrowserPredicate implements Predicate<WebBrowser> {
    @Override
    public boolean test(WebBrowser b) {
        return b.isAndroid() || b.isIPhone() || b.isWindowsPhone();
    }

    public boolean test(VaadinSession vaadinSession) {
        return vaadinSession != null && test(vaadinSession.getBrowser());
    }
}

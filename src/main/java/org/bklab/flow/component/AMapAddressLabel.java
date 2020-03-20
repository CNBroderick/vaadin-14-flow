/*
 * Class: org.bklab.flow.component.AMapAddressLabel
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.Html;
import org.bklab.util.UrlEncoder;

public class AMapAddressLabel extends Html {

    public AMapAddressLabel(String address) {
        super(String.format("<a href='https://ditu.amap.com/search?query=%s' target='_blank'>%s</>", UrlEncoder.encode(address), address));
    }

}

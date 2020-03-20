/*
 * Class: org.bklab.common.action.http.XmlResultFormatter
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.http;

import org.bklab.common.action.RawResultFormatter;
import org.jsoup.Jsoup;

public class XmlResultFormatter implements RawResultFormatter {
    @Override
    public String format(String content) {
        try {
            return Jsoup.parseBodyFragment(content).body().html();
        } catch (Exception e) {
            return content;
        }
    }

    @Override
    public String getName() {
        return "XML";
    }
}

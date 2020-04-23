/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-17 18:31:23
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.util.EnumFactoryTest
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import com.vaadin.flow.component.button.ButtonVariant;
import junit.framework.TestCase;

import java.lang.reflect.Field;

public class EnumFactoryTest extends TestCase {

    public void testGetInstanceViaClass() throws Exception {
        test(new EnumFactory<>(ButtonVariant.class));
    }

    public void testGetInstanceViaPath() throws Exception {
        test(new EnumFactory<>("com.vaadin.flow.component.button.ButtonVariant"));
    }

    public void testGetInstanceFields() throws Exception {
        Field[] declaredFields = ButtonVariant.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            System.out.println(i + ". " + declaredFields[i].getName());
        }

    }

    private void test(EnumFactory<ButtonVariant> enumFactory) throws Exception {
        System.err.println("-------- START TEST GET INSTANCES ----------");
        ButtonVariant[] references = enumFactory.getInstances();
        for (ButtonVariant reference : references) {
            assertNotNull(reference);
        }
        System.err.println("-------- END TEST GET INSTANCES ----------");
        System.err.println("-------- START TEST GET INSTANCE ----------");

        ButtonVariant buttonVariant = ButtonVariant.LUMO_SMALL;

        assertEquals(buttonVariant, enumFactory.getInstance(buttonVariant.name()).orElse(null));
        assertEquals(buttonVariant, enumFactory.getInstance("variant", buttonVariant.getVariantName()).orElse(null));

        // 谨慎使用此方法获得
        assertEquals(buttonVariant, enumFactory.getInstance(11, buttonVariant.getVariantName()).orElse(null));

        assertEquals(buttonVariant, enumFactory.getInstance("variant", variant -> variant.equals(buttonVariant.getVariantName())).orElse(null));
        assertEquals(buttonVariant, enumFactory.getInstance(variant -> variant.getVariantName().equals(buttonVariant.getVariantName())).orElse(null));

        System.err.println("-------- END TEST GET INSTANCE ----------");
    }
}
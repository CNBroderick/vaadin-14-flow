/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-10 10:43:00
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.util.MySqlParameter
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import java.sql.PreparedStatement;

public class MySqlParameter {

    private final PreparedStatement statement;

    public MySqlParameter(PreparedStatement statement, Object... objects) throws Exception {
        this.statement = statement;
        insert(1, objects);
    }

    public MySqlParameter insert(Object... objects) throws Exception {
        return insert(1, objects);
    }

    /**
     * @param position init position should be >= 1
     * @param objects  parameters
     * @return this
     */
    public MySqlParameter insert(int position, Object... objects) throws Exception {
        if (objects == null) return this;
        if (position < 1) throw new IllegalArgumentException("position should be should be >= 1.");

        for (int i = 0; i < objects.length; i++) {
            statement.setObject(i + position, objects[i]);
        }

        return this;
    }
}

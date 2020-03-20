/*
 * Class: org.bklab.util.MySqlServerExistTenantHelper
 * Modify date: 2020/3/20 上午11:12
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import org.bklab.entity.multi.MySqlServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySqlServerExistTenantHelper {

    private final List<MySqlServer> servers;
    private final Map<MySqlServer, Integer> map = new HashMap<>();

    public MySqlServerExistTenantHelper(List<MySqlServer> servers) {
        this.servers = servers;
        for (MySqlServer server : servers) {
            try {
                Connection connection = server.connect();
                ResultSet rs = connection.prepareStatement(
                        "show databases Where `Database` like 'building%' AND `Database` NOT IN('building-master', 'building_000000');"
                ).executeQuery();
                rs.last();
                map.put(server, rs.getRow());
                rs.close();
            } catch (Exception e) {
                map.put(server, Integer.MAX_VALUE);
            }
        }
    }

    public int getTenantNumber(MySqlServer mySqlServer) {
        return map.getOrDefault(mySqlServer, Integer.MAX_VALUE);
    }

    public List<MySqlServer> byTenantNumber() {
        return servers.stream().filter(m -> map.get(m) < Integer.MAX_VALUE)
                .sorted(Comparator.comparingInt(map::get)).collect(Collectors.toList());
    }
}

/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.hbase.test_env;

import com.google.bigtable.repackaged.com.google.cloud.bigtable.config.BigtableOptions;
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;


class BigtableEnv extends SharedTestEnv {

  private static final Set<String> KEYS = Sets.newHashSet(
      "hbase.client.connection.impl",
      BigtableOptionsFactory.BIGTABLE_PORT_KEY,
      BigtableOptionsFactory.BIGTABLE_HOST_KEY,
      BigtableOptionsFactory.BIGTABLE_INSTANCE_ADMIN_HOST_KEY,
      BigtableOptionsFactory.BIGTABLE_TABLE_ADMIN_HOST_KEY,
      BigtableOptionsFactory.PROJECT_ID_KEY,
      BigtableOptionsFactory.INSTANCE_ID_KEY,
      BigtableOptionsFactory.BIGTABLE_USE_BULK_API,
      BigtableOptionsFactory.BIGTABLE_USE_PLAINTEXT_NEGOTIATION
  );
  private Configuration configuration;

  @Override
  protected void setup() throws IOException {
    configuration = HBaseConfiguration.create();


    String connectionClass = System.getProperty("google.bigtable.connection.impl");
    if (connectionClass != null) {
      configuration.set("hbase.client.connection.impl", connectionClass);
    }

    for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
      if (KEYS.contains(entry.getKey())) {
        configuration.set(entry.getKey().toString(), entry.getValue().toString());
      }
    }

    try (Connection connection = createConnection() ) {
      createConnection().getAdmin().deleteTables("test_table-.*");
    }
  }

  @Override
  protected void teardown() {
    // noop
  }

  @Override
  public Connection createConnection() throws IOException {
    return ConnectionFactory.createConnection(configuration);
  }
}

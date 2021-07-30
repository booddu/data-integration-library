// Copyright 2021 LinkedIn Corporation. All rights reserved.
// Licensed under the BSD-2 Clause license.
// See LICENSE in the project root for license information.

package com.linkedin.dil.source;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.gobblin.configuration.State;
import org.apache.gobblin.configuration.WorkUnitState;
import com.linkedin.dil.connection.HdfsConnection;
import com.linkedin.dil.extractor.MultistageExtractor;
import com.linkedin.dil.keys.HdfsKeys;
import org.apache.gobblin.source.extractor.Extractor;


/**
 * This class supports HDFS as just another protocol. The main function
 * of it is to launch a proper extractor with a HdfsConnection
 */
@Slf4j
public class HdfsSource extends MultistageSource<Schema, GenericRecord> {
  @Getter @Setter
  private HdfsKeys hdfsKeys;

  public HdfsSource() {
    hdfsKeys = new HdfsKeys();
    jobKeys = hdfsKeys;
  }

  protected void initialize(State state) {
    super.initialize(state);
    hdfsKeys.logUsage(state);
    hdfsKeys.logDebugAll();
  }

  /**
   * Create extractor based on the input WorkUnitState, the extractor.class
   * configuration, and a new HdfsConnection
   *
   * @param state WorkUnitState passed in from Gobblin framework
   * @return the MultistageExtractor object
   */

  @Override
  public Extractor<Schema, GenericRecord> getExtractor(WorkUnitState state) {
    initialize(state);
    MultistageExtractor<Schema, GenericRecord> extractor =
        (MultistageExtractor<Schema, GenericRecord>) super.getExtractor(state);
    extractor.setConnection(new HdfsConnection(state, hdfsKeys, extractor.getExtractorKeys()));
    return extractor;

  }
}
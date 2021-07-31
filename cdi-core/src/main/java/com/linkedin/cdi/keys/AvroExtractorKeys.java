// Copyright 2021 LinkedIn Corporation. All rights reserved.
// Licensed under the BSD-2 Clause license.
// See LICENSE in the project root for license information.

package com.linkedin.cdi.keys;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericRecord;
import org.apache.gobblin.configuration.State;
import com.linkedin.cdi.configuration.MultistageProperties;
import org.apache.gobblin.source.workunit.WorkUnit;


/**
 * These attributes are defined and maintained in AvroExtractor
 *
 * @author esong
 */
@Slf4j
@Getter(AccessLevel.PUBLIC)
@Setter
public class AvroExtractorKeys extends ExtractorKeys {
  final private static List<MultistageProperties> ESSENTIAL_PARAMETERS = Lists.newArrayList(
      MultistageProperties.MSTAGE_DATA_FIELD,
      MultistageProperties.MSTAGE_TOTAL_COUNT_FIELD);

  private DataFileStream<GenericRecord> avroRecordIterator = null;
  private long processedCount;
  private long totalCount;
  // TODO: move this to ExtractorKeys if pagination is needed
  private long currentPageNumber = 0;
  private Schema avroOutputSchema = null;
  private Boolean isValidOutputSchema = true;

  public void incrCurrentPageNumber() {
    currentPageNumber++;
  }
  public void incrProcessedCount() {
    processedCount++;
  }



  @Override
  public void logDebugAll(WorkUnit workUnit) {
    super.logDebugAll(workUnit);
    log.debug("These are values of JsonExtractor regarding to Work Unit: {}",
        workUnit == null ? "testing" : workUnit.getProp(MultistageProperties.DATASET_URN_KEY.toString()));
    log.debug("Total rows expected or processed: {}", totalCount);
    log.debug("Total rows processed: {}", processedCount);
  }

  @Override
  public void logUsage(State state) {
    super.logUsage(state);
    for (MultistageProperties p: ESSENTIAL_PARAMETERS) {
      log.info("Property {} ({}) has value {} ", p.toString(), p.getClassName(), p.getValidNonblankWithDefault(state));
    }
  }
}
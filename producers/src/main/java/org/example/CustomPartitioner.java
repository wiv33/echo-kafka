package org.example;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.StickyPartitionCache;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CustomPartitioner implements Partitioner {
  //    DefaultPartitioner 기본 파티셔너
  private static final Logger logger = LoggerFactory.getLogger(CustomPartitioner.class);

  private final StickyPartitionCache stickyPartitionCache = new StickyPartitionCache();
  private String specialKeyName;

  @Override
  public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    // topic의 전체 파티션을 가지고올 때 Cluster를 활용.
    var partitionInfos = cluster.partitionsForTopic(topic);
    var numPartitions = partitionInfos.size();
    var partitionIdx = 0;
    if (keyBytes == null) {
//      return stickyPartitionCache.partition(topic, cluster);
      throw new InvalidRecordException("key cannot be null");
    }

    // partition의 절반을 P001에게 할당.
    var numSpecialPartitions = (int) (numPartitions * 0.5);
    // null point exception 주의
    if (key.equals(specialKeyName)) {
      // key가 항상 P001이기 때문에 keyBytes를 사용하지 않음.
      partitionIdx = Utils.toPositive(Utils.murmur2(valueBytes)) % numSpecialPartitions;
    } else {
      partitionIdx = Utils.toPositive(Utils.murmur2(keyBytes)) % (numPartitions - numSpecialPartitions) + numSpecialPartitions;
    }
    logger.info("key: {} is sent to partition: {}", key, partitionIdx);
    return partitionIdx;
  }

  @Override
  public void configure(Map<String, ?> configs) {
    specialKeyName = configs.get("custom.specialKey").toString();
  }

  @Override
  public void close() {

  }
}

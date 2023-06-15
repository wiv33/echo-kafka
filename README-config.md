# Kafka Config info

```yaml
ProducerConfig values:
  acks = 1
  batch.size = 16384
  bootstrap.servers = [broker:9092]
  buffer.memory = 33554432
  client.dns.lookup = use_all_dns_ips
  client.id = producer-1
  compression.type = none
  connections.max.idle.ms = 540000
  delivery.timeout.ms = 120000 # 2분, 시간만큼 재전송(retry) 후 더 이상 재전송하지 않고 종료. # send accumulate가 전부 다 찬 경우 + max.block.ms가 초과하면 sender에 가지도 못하고 exception 됨. # request.timeout.ms 
  enable.idempotence = true
  interceptor.classes = []
  key.serializer = class org.apache.kafka.common.serialization.StringSerializer
  linger.ms = 0 # 최대 20ms 권장 / 배치가 다 차거나, linger.ms가 지나면 전송  
  max.block.ms = 60000
  max.in.flight.requests.per.connection = 5 # 최대 5개의 배치를 가져와 보낼 수 있다. 
  max.request.size = 1048576 # 1MB # record가 1Mb인 경우 레코드 하나씩만 보낼 수 있다.
  metadata.max.age.ms = 300000
  metadata.max.idle.ms = 300000
  metric.reporters = []
  metrics.num.samples = 2
  metrics.recording.level = INFO
  metrics.sample.window.ms = 30000
  partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
  receive.buffer.bytes = 32768
  reconnect.backoff.max.ms = 1000
  reconnect.backoff.ms = 50
  request.timeout.ms = 30000
  retries = 2147483647
  retry.backoff.ms = 100
  sasl.client.callback.handler.class = null
  sasl.jaas.config = null
  sasl.kerberos.kinit.cmd = /usr/bin/kinit
  sasl.kerberos.min.time.before.relogin = 60000
  sasl.kerberos.service.name = null
  sasl.kerberos.ticket.renew.jitter = 0.05
  sasl.kerberos.ticket.renew.window.factor = 0.8
  sasl.login.callback.handler.class = null
  sasl.login.class = null
  sasl.login.connect.timeout.ms = null
  sasl.login.read.timeout.ms = null
  sasl.login.refresh.buffer.seconds = 300
  sasl.login.refresh.min.period.seconds = 60
  sasl.login.refresh.window.factor = 0.8
  sasl.login.refresh.window.jitter = 0.05
  sasl.login.retry.backoff.max.ms = 10000
  sasl.login.retry.backoff.ms = 100
  sasl.mechanism = GSSAPI
  sasl.oauthbearer.clock.skew.seconds = 30
  sasl.oauthbearer.expected.audience = null
  sasl.oauthbearer.expected.issuer = null
  sasl.oauthbearer.jwks.endpoint.refresh.ms = 3600000
  sasl.oauthbearer.jwks.endpoint.retry.backoff.max.ms = 10000
  sasl.oauthbearer.jwks.endpoint.retry.backoff.ms = 100
  sasl.oauthbearer.jwks.endpoint.url = null
  sasl.oauthbearer.scope.claim.name = scope
  sasl.oauthbearer.sub.claim.name = sub
  sasl.oauthbearer.token.endpoint.url = null
  security.protocol = PLAINTEXT
  security.providers = null
  send.buffer.bytes = 131072
  socket.connection.setup.timeout.max.ms = 30000
  socket.connection.setup.timeout.ms = 10000
  ssl.cipher.suites = null
  ssl.enabled.protocols = [TLSv1.2, TLSv1.3]
  ssl.endpoint.identification.algorithm = https
  ssl.engine.factory.class = null
  ssl.key.password = null
  ssl.keymanager.algorithm = SunX509
  ssl.keystore.certificate.chain = null
  ssl.keystore.key = null
  ssl.keystore.location = null
  ssl.keystore.password = null
  ssl.keystore.type = JKS
  ssl.protocol = TLSv1.3
  ssl.provider = null
  ssl.secure.random.implementation = null
  ssl.trustmanager.algorithm = PKIX
  ssl.truststore.certificates = null
  ssl.truststore.location = null
  ssl.truststore.password = null
  ssl.truststore.type = JKS
  transaction.timeout.ms = 60000
  transactional.id = null
  value.serializer = class org.apache.kafka.common.serialization.StringSerializer
```

## ProducerConfig 메시지 전송/재전송 시간 파라미터

- **delivery.timeout.ms**
    - send() 호출 시 record accumulator에 적재하지 못하고 block되는 최대 시간
    - 이후 TimeoutException 발생
- **linger.ms**
    - 배치가 다 차거나, linger.ms가 지나면 전송
    - 기본값은 0
    - 최대 20ms 권장
- **max.block.ms**
    - send accumulate가 전부 다 찬 경우 + max.block.ms가 초과하면 sender에 가지도 못하고 exception 됨.
- **request.timeout.ms**
    - broker에게 메시지를 보내고 ack를 받는데 걸리는 최대 시간
    - 기본값은 30초
- **retries**
    - 메시지 전송 실패 시 재전송 횟수
    - 기본값은 2147483647
    - 0이면 재전송 안함
    - -1이면 무한 재전송
    - 1이상이면 해당 횟수만큼 재전송
    - **delivery.timeout.ms**만큼의 시간이 초과되면 종료되기 때문에 값이 큼.
    - sync, async 동일하게 적용
- **retry.backoff.ms**
    - 재전송 시간 간격
    - 기본값은 100ms
- **max.in.flight.requests.per.connection**
    - 한번에 보낼 수 있는 메시지의 최대 개수
    - 기본값은 5
    - 1이면 순서대로 보내지만, 성능이 떨어짐
    - 5이상이면 순서가 보장되지 않지만, 성능이 좋아짐
- **acks**
    - 메시지 전송 후 ack를 받는 방식
    - 기본값은 1
    - 0이면 전송만 하고 ack를 기다리지 않음
    - 1이면 leader에게 ack를 받음
    - `all`이면 모든 replica에게 ack를 받음
    - sync, async 동일하게 적용
- **max.request.size**
    - 메시지 최대 크기
    - 기본값은 1MB
- **delivery.timeout.ms**
    - 메시지 전송 후 ack를 받는데 걸리는 최대 시간
    - 기본값은 120000ms

    delivery.timeout.ms >= linger.ms + request.timeout.ms
    해당 조건을 항상 만족해야 한다.

```java
class Producer {

  public static void main(String[] args) {
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFLUENT_BROKER);
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    // acks = -1
    props.setProperty(ProducerConfig.ACKS_CONFIG, "1");
    // batch.size = 16384
    props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "32000");
    // linger.ms = 0 # 최대 20ms 권장 / 배치가 다 차거나, linger.ms가 지나면 전송 
    props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
  }
}
```

## ConsumerConfig

- **fetch.max.bytes**
    - Fetcher가 한번에 가져올 수 있는 최대 데이터 크기.
    - 기본값 50MB
- **max.partition.fetch.bytes**
    - Fetcher가 파티션별 한번에 최대로 가져올 수 있는 byte
- **max.poll.records**
    - Fetcher가 한번에 가져올 수 있는 레코드 수.
    - 기본값 500
- **fetch.min.bytes**
  - Fetcher가 record들을 읽어오는 최소 byte
  - 브로커는 지정된 fetch.min.bytes 이상의 새로운 메시지가 쌓일 때까지 전송하지 않음.
  - 기본값 1
- **fetch.max.wait.ms**
  - 브로커에 fetch.min.bytes 이상의 메시지가 쌓일 때까지 최대 대기 시간.
  - 기본은 500ms


## example

1. KafkaConsumer.poll(1000)
2. fetch.min.bytes = 16384 (16KB)
3. fetch.max.wait.ms = 500
4. fetch.max.bytes = 52428800 (50MB)
5. max.partition.fetch.bytes = 1048576 (1MB)
6. max.poll.records = 500

### 해설
1. 가져올 데이터가 한 건도 없으면 1000ms 동안 대기
2. 가져올 과거의 데이터가 많을 경우 `max.partition.fetch.bytes`의 배치 크기 설정 (공식 문서에 나와있는 얘기는 아님.)
3. 데이터가 많지 않을 경우 `fetch.min.bytes`의 배치 크기 설정
4. 가장 최신의 Offset 데이터를 가져오고 있다면 `fetch.min.bytes`만큼 가져오고, 
5. `fetch.min.bytes`만큼 쌓이지 않았다면, `fetch.max.wait.ms`만큼 대기 후 결과 반환
6. 오랜 과거 offset 데이터를 가져온다면 최대 `max.partition.fetch.bytes`만큼 파티션에서 읽은 뒤 반환
7. `max.partition.fetch.bytes`에 도달하지 못해도 가장 최신의 offset에 도달하면 반환
8. 토픽에 파티션이 많아도 가져오는 데이터양은 `fetch.max.bytes`로 제한
9. Fetcher가 Linked Queue에서 가져오는 레코드의 개수는 `max.poll.records`로 제한
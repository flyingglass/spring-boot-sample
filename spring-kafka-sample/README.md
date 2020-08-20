#### Kafka Sample

##### Producer
1. 调优参数设置
- acks
```markdown
- 在消息被认为是“已提交”之前，producer需要leader确认的produce请求的应答数。该参数用于控制消息的持久性，目前提供了3个取值：
    acks = 0: 表示produce请求立即返回，不需要等待leader的任何确认。这种方案有最高的吞吐率，但是不保证消息是否真的发送成功。
    acks = -1: 表示分区leader必须等待消息被成功写入到所有的ISR副本(同步副本)中才认为produce请求成功。这种方案提供最高的消息持久性保证，但是理论上吞吐率也是最差的。
    acks = 1: 表示leader副本必须应答此produce请求并写入消息到本地日志，之后produce请求被认为成功。如果此时leader副本应答请求之后挂掉了，消息会丢失。这是个这种的方案，提供了不错的持久性保证和吞吐。
```
- buffer.memory
 ```markdown
- 该参数用于设置Producer端的缓存消息的缓冲区大小，默认大小32M。Kafka消息发送采用异步模式(类似生产消费模型结构)，消息会首先进入缓冲区，由后台专属线程负责从缓冲区读取然后进行消息发送。
- 消息持续发送过程中，当缓冲区被填满后，Producer会进入阻塞状态，如果超过了max.blocks.ms的值，producer会抛出TimeoutException异常。
- 多个线程共享Kafka Producer容易打满buffer.memory
```
- compression.type
```markdown
- producer压缩器，目前支持none（不压缩），gzip，snappy和lz4。
- 基于公司物联网平台，试验过目前lz4的效果最好。当然2016年8月，FaceBook开源了Ztandard。 官网测试： Ztandard压缩率为2.8，snappy为2.091，LZ4 为2.101。
```
- retries
```markdown
- producer重试的次数设置。
- 重试时producer会重新发送之前由于瞬时原因出现失败的消息，瞬时失败的原因可能包括：元数据信息失效、副本数量不足、超时、位移越界或未知分区等。倘若设置了retries > 0，那么这些情况下producer会尝试重试。
```
- batch.size(吞吐量和延时性能)
```markdown
- producer都是按照batch进行发送的，因此batch大小的选择对于producer性能至关重要。
- producer会把发往同一分区的多条消息封装进一个batch中，当batch满了后，producer才会把消息发送出去。但是也不一定等到满了，这和另外一个参数linger.ms有关。默认值为16K，合计为16384.
```
- linger.ms(吞吐量和延时性能)
```markdown
- producer是按照batch进行发送的，取决于linger.ms的值，默认是0，表示不做停留。
- 这种情况下，可能有的batch中没有包含足够多的produce请求就被发送出去了，造成了大量的小batch，给网络IO带来的极大的压力。
```
- max.in.flight.requests.per.connection
```markdown
- producer的IO线程在单个Socket连接上能够发送未应答producer请求的最大数量，默认值为5。
- 增加此值应该可以增加IO线程的吞吐量，从而整体上提升producer的性能。不过就像之前说的如果开启了重试机制，那么设置该参数大于1的话有可能造成消息的乱序。
```

2. 源码分析

Producer send的源码步骤：
```markdown
1. 处理消息分区(DefaultPartitioner默认分区处理器)
2. 把消息放入RecordAccumulator
3. 立即返回
4. 后台Sender和IOThread线程处理真正的发送(NetworkClient处理)
```


##### Consumer
1. 调优参数设置
- fetch.min.bytes
```markdown
server发送到消费端的最小数据，若是不满足这个数值则会等待直到满足指定大小。默认为1表示立即接收。
```


2. 源码分析

Consumer poll的源码步骤:
```markdown
1. 在Timeout时间内，循环从Fetcher获取消息
2. 有消息立即返回
3. 处理消息分区(ConsumerPartitionAssignor按分区策略处理)
4. Fetcher处理真正的消息拉取(ConsumerNetworkClient处理)

```

package dev.glory.demo.common.util.idgenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.function.IntFunction;

import dev.glory.demo.common.util.idgenerator.tsid.Tsid;
import dev.glory.demo.common.util.idgenerator.tsid.TsidFactory;

public class TraceIdGenerator {

    private static final int                 NODE_BITS       = 10;
    private static final Instant             CUSTOM_EPOCH    = Instant.parse("2015-01-01T00:00:00.000Z");
    private static final IntFunction<byte[]> RANDOM_FUNCTION = byte[]::new;
    private final        int                 worker;  // max: 2^5-1 = 31
    private final        int                 process; // max: 2^5-1 = 31
    private final        int                 node; // max: 2^10-1
    private final        TsidFactory         factory;

    /**
     * @param worker  0 ~ 31
     * @param process 0 ~ 31
     */
    public TraceIdGenerator(int worker, int process) {
        this.worker = worker;
        this.process = process;
        this.node = (worker << 5 | process);

        this.factory = TsidFactory.builder()
                .withRandomFunction(RANDOM_FUNCTION)
                .withCustomEpoch(CUSTOM_EPOCH)
                .withNodeBits(NODE_BITS)
                .withNode(node)
                .build();
    }

    /**
     * @param id 64bit
     * @return [timestamp, worker, process, sequence]
     */
    public static long[] parse(long id) {
        final long[] array = new long[4];
        array[0] = (id >>> 22) + CUSTOM_EPOCH.toEpochMilli();
        array[1] = (id & 0x3E0000) >> 17;
        array[2] = (id & 0x1F000) >> 12;
        array[3] = (id & 0xFFF);
        return array;
    }

    /**
     * @param id 64bit
     * @return LocalDateTime
     */
    public static LocalDateTime generatedAt(long id) {
        long timestamp = (id >>> 22) + CUSTOM_EPOCH.toEpochMilli();
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                java.time.ZoneId.systemDefault()
        );
    }

    /**
     * @return 64bit noticeId
     */
    public long generate() {
        Tsid tsid = factory.create();
        return tsid.toLong();
    }

}

package com.libraries.disruptor.example;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started#basic-event-produce-and-consume
 */
public class LongEventMain {

    public static void main(String[] args) throws Exception {

        LongEventFactory factory = new LongEventFactory();


        /**
         * Must be power of 2
         * https://stackoverflow.com/questions/10527581/why-must-a-ring-buffer-size-be-a-power-of-2
         */
        int bufferSize = 1024;


        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, DaemonThreadFactory.INSTANCE);


        disruptor.handleEventsWith(new LongEventHandler());

        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);

        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            Thread.sleep(1000);
        }
    }

}

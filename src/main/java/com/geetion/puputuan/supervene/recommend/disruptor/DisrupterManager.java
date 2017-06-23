package com.geetion.puputuan.supervene.recommend.disruptor;


import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mac on 16/3/31.
 */
public class DisrupterManager<T> {
    // Specify the size of the ring buffer, must be power of 2.
    private static final int BUFFER_SIZE = 1024;

    // Construct the Disruptor
    private Disruptor<T> disruptor;
    private ProducerType producerType = ProducerType.SINGLE;
    private WaitStrategy waitStrategy;
    private EventFactory factory;
    private ExecutorService executor;


    public DisrupterManager<T> setFactory(EventFactory factory) {
        this.factory = factory;
        return this;
    }

    public DisrupterManager setMaxThread(int maxTheard) {
        return this;
    }

    public DisrupterManager setWaitStrategy(WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
        return this;
    }

    public void build() {
        executor = Executors.newCachedThreadPool();
        if (producerType == null) {
            producerType = ProducerType.MULTI;
        }

        if (waitStrategy == null) {
            waitStrategy = new BlockingWaitStrategy();
        }

        disruptor = new Disruptor(factory,
                BUFFER_SIZE,
                executor, // Single producer
                producerType,
                waitStrategy);
    }

    public Disruptor<T> getDisruptor() throws Exception {
        if (disruptor != null)
            return disruptor;
        else
            throw new Exception("please call build method first");
    }

    public ExecutorService getExecutor() throws Exception {
        if (executor != null)
            return executor;
        else
            throw new Exception("please call build method first");
    }

    public EventHandlerGroup handleEventsWith(EventHandler... eventHandler) throws Exception {
        if (disruptor == null)
            throw new Exception("please call build method first");

        return disruptor.handleEventsWith(eventHandler);
    }

    public RingBuffer startEvent() throws Exception {
        if (disruptor == null)
            throw new Exception("please call build method first");

        return disruptor.start();
    }

    public void destory() {
        if (disruptor != null)
            disruptor.shutdown();
        if (executor != null)
            executor.shutdown();
    }

    public void pushEvent(EventTranslator eventTranslator) {
        if (disruptor != null)
            disruptor.publishEvent(eventTranslator);
    }


}

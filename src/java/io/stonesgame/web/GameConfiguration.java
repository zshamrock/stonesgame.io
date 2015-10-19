package io.stonesgame.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.dropwizard.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class GameConfiguration extends Configuration {
    public static final String QUEUE_CAPACITY = "QUEUE_CAPACITY";

    protected static final int MAX_QUEUE_CAPACITY = 100;

    @Min(0)
    @Max(MAX_QUEUE_CAPACITY)
    private int queueCapacity;

    public int getQueueCapacity() {
        final int capacity = Math.min(
                // protect max queue capacity
                MAX_QUEUE_CAPACITY,
                Integer.parseInt(
                    // look for the queue capacity in the following order: env property -> yaml property
                    MoreObjects.firstNonNull(
                            System.getenv(QUEUE_CAPACITY),
                            Integer.toString(this.queueCapacity))));
        return capacity >= 0 ? capacity : MAX_QUEUE_CAPACITY;
    }

    @JsonProperty
    public void setQueueCapacity(final int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean isBotsEnabled() {
        return true;
    }
}

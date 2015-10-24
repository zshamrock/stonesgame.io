package io.stonesgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class BotsConfiguration {
    public static final int DEFAULT_POOL_SIZE = 5;

    @Min(10)
    private int scheduledPeriodInSeconds;

    private boolean enabled;

    @Max(10)
    private int poolSize = DEFAULT_POOL_SIZE;

    @JsonProperty
    public void setScheduledPeriodInSeconds(final int scheduledPeriodInSeconds) {
        this.scheduledPeriodInSeconds = scheduledPeriodInSeconds;
    }

    @JsonProperty
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public void setPoolSize(final int poolSize) {
        this.poolSize = poolSize;
    }

    @JsonProperty
    public int getScheduledPeriodInSeconds() {
        return this.scheduledPeriodInSeconds;
    }

    @JsonProperty
    public boolean isEnabled() {
        return this.enabled;
    }

    @JsonProperty
    public int getPoolSize() {
        return this.poolSize;
    }
}

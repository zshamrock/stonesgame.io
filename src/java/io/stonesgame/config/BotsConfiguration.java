package io.stonesgame.config;

import io.stonesgame.bots.Bots;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class BotsConfiguration {

    @Min(10)
    private int scheduledPeriodInSeconds;

    private boolean enabled;

    @Max(Bots.MAX_POOL_SIZE)
    private int poolSize = Bots.MIN_POOL_SIZE;

    @JsonProperty
    public void setScheduledPeriodInSeconds(final int scheduledPeriodInSeconds) {
        this.scheduledPeriodInSeconds = scheduledPeriodInSeconds;
    }

    public int getScheduledPeriodInSeconds() {
        return this.scheduledPeriodInSeconds;
    }

    @JsonProperty
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @JsonProperty
    public void setPoolSize(final int poolSize) {
        this.poolSize = poolSize;
    }

    public int getPoolSize() {
        return this.poolSize;
    }
}

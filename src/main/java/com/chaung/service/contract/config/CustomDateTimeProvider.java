package com.chaung.service.contract.config;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component("zonedDateTimeProvider")
public class CustomDateTimeProvider implements DateTimeProvider {
    @Override
    @Nonnull
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now());
    }
}

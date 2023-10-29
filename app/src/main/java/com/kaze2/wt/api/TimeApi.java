package com.kaze2.wt.api;

import java.io.IOException;
import java.time.ZonedDateTime;

public interface TimeApi {
    ZonedDateTime getTimeAtZone(String timeZone) throws IOException;
}

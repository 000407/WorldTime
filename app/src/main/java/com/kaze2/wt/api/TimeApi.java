package com.kaze2.wt.api;

import java.io.IOException;

public interface TimeApi {
    String getTimeAtZone(String timeZone) throws IOException;
}

package com.bitoasis.ticker.util;

import java.util.List;
import java.util.Set;

public class AppConstants {

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final Set<String> SORT_BY_PARAMETERS = Set.of("id","bid","bidSize","ask","askSize"
            ,"dailyChange","dailyChangeRelative","lastPrice","volume","high","low","created");

}

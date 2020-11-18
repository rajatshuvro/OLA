package com.ola.NativeSearch;

public interface IWordMatcher {
    float getSimilarity(String s1, String s2);
    void setThreshold(float threshold);
    float getThreshold();
}

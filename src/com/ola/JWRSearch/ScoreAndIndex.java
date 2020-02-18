package com.ola.JWRSearch;

public class ScoreAndIndex implements Comparable<ScoreAndIndex> {
    public final Float Score;
    public final int Index;

    public ScoreAndIndex(float score, int index){
        Score = score;
        Index = index;
    }
    @Override
    public int compareTo(ScoreAndIndex other) {
        return Score.compareTo(other.Score);
    }
}

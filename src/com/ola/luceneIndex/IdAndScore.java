package com.ola.luceneIndex;

import java.util.ArrayList;

public class IdAndScore implements Comparable<IdAndScore> {
    public final String Id;
    public final Float Score;

    public IdAndScore(String id, float score){
        Id = id;
        Score = score;
    }

    public static String[] GetIds(ArrayList<IdAndScore> idAndScores){
        var ids = new String[idAndScores.size()];
        for (var i=0; i < idAndScores.size(); i++)
            ids[i]= idAndScores.get(i).Id;

        return ids;
    }
    @Override
    public int compareTo(IdAndScore other) {
        return Score.compareTo(other.Score);
    }
}

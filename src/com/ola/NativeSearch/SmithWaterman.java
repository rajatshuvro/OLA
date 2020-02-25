package com.ola.NativeSearch;



public class SmithWaterman implements IWordMatcher {
    public final int SubstitutionCost = 1;
    public final int DeletionCost =1;
    public final int InsertionCost =1;

    public float threshold = 0.8f;

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
    public float getThreshold() {
        return threshold;
    }

    public float getSimilarity(String s1, String s2){
        var distance = GetDistance(s1, s2);
        if (distance == 0) return 1;
        var maxLength = Math.max(s1.length(), s2.length());
        var similarity =  1.0f - (1.0f*distance)/maxLength;
        return similarity > threshold ? similarity : 0;
    }
    //determine the edit distance between strings using dynamic programming
    public int GetDistance(String s1, String s2){
        if(s1==null && s2==null) return 0;
        if(s1.length() > s2.length()) {
            var temp = s1;
            s1 = s2;
            s2 = temp;
        }
        //now min is the smaller string
        var distance = new int[2][s1.length()+1];

        //initializing the distance buffer
        distance[0][0]= 0;
        for(var i = 1; i <= s1.length(); i++){
            distance[0][i] = distance[0][i-1]+DeletionCost;//score of deleting chars from s1
        }

        for(var j=1; j <= s2.length(); j++){
            for(var i=0; i <= s1.length(); i++){
                if(i==0){
                    distance[j%2][i] = distance[(j-1)%2][i]+ InsertionCost;
                    continue;
                }
                var subCost = distance[(j-1)%2][i-1] + GetSubCost(s1.charAt(i-1), s2.charAt(j-1));
                var delCost = distance[j%2][i-1] + DeletionCost;
                var insCost = distance[(j-1)%2][i] + InsertionCost;
                distance[j%2][i]= Math.min(subCost, Math.min(delCost, insCost));
            }
        }
        return distance[s2.length()%2][s1.length()];
    }

    private int GetSubCost(char c1, char c2) {
        return c1==c2? 0 : SubstitutionCost;
    }
}

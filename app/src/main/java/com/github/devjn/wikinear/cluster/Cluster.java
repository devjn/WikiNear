package com.github.devjn.wikinear.cluster;

/**
 * Class to represent a cluster of coordinates.
 */
public class Cluster {

    // Indices of the member coordinates.
    private int[] mMemberIndexes;
    // The cluster center.
    private String mCenter;
    
    /**
     * Constructor.
     * 
     * @param memberIndexes indices of the member coordinates.
     * @param center the cluster center.
     */
    public Cluster(int[] memberIndexes, String center) {
        mMemberIndexes = memberIndexes;
        mCenter = center;
    }
    
    /**
     * Get the member indices.
     * 
     * @return an array containing the indices of the member coordinates.
     */
    public int[] getMemberIndexes() {
        return mMemberIndexes;
    }
    
    /**
     * Get the cluster center.
     * 
     * @return a reference to the cluster center array.
     */
    public String getCenter() {
        return mCenter;
    }
    
}

package com.tianhaolin.groovy.dsl

class Account {
    String subscriber
    String plan
    int points
    double spend
    Map mediaList = [:]
    void addMedia (media, expiry) {
        mediaList[media] = expiry
    }
    void extendMedia(media, length) {
        mediaList[media] += length
    }
    Date getMediaExpiry(media) {
        if(mediaList[media] != null) {
            return mediaList[media]
        }
    }

    @Override
    String toString() {
        String str = "subscriber:"+subscriber+"\n" +
                "plan:"+plan+"\n" +
                "points:"+points+"\n" +
                "spend:"+spend+"\n"

        mediaList.keySet().each {
            str +=  it.title+","+mediaList.get(it)+"\n"
        }
        return str
    }
}
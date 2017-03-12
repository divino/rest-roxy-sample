package com.marklogic.rest.pojo;

import java.util.Arrays;

/**
 * Created by dbagayau on 12/03/2017.
 */
public class Synonyms {

    private String[] synonyms;

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        return "Synonyms{" +
                "synonyms=" + Arrays.toString(synonyms) +
                '}';
    }
}

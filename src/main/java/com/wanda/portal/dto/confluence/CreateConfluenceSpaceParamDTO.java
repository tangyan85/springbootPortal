package com.wanda.portal.dto.confluence;

import java.io.Serializable;

/*
 * { 
 * "key" : "ZZRR", 
 * "name" : "打时空的金卡", 
 * "description": { 
 * "plain": { 
 *  "representation":"software-project", 
 *  "value": "very good" } 
 *  } 
 *  }
 */
public class CreateConfluenceSpaceParamDTO implements Serializable {

    private static final long serialVersionUID = 2819483236683551079L;

    private String key;
    private String name;
    private String representation;
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CreateConfluenceSpaceParamDTO [key=" + key + ", name=" + name + ", representation=" + representation
                + ", description=" + description + "]";
    }

}

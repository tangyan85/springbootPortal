package com.wanda.portal.dto.confluence;

import java.io.Serializable;

import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.utils.ConversionUtil;

/* 查询/rest/api/space输出的所有space列表，含在results里面 */
/*
 * { "id":98305, "key":"ds", "name":"Demonstration Space", "type":"global", "_links":{
 * "webui":"/display/ds", "self":"http://10.214.170.68:8080/rest/api/space/ds" }, "_expandable":{
 * "metadata":"", "icon":"", "description":"", "homepage":"/rest/api/content/65547" } },
 */
public class GenericConfluenceSpaceDTO implements Serializable {

    private static final long serialVersionUID = -5485444759385126260L;

    private Long id;
    private String key;
    private String name;
    private String type;
    private Links _links;
    private Expandable _expandable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    public Expandable get_expandable() {
        return _expandable;
    }

    public void set_expandable(Expandable _expandable) {
        this._expandable = _expandable;
    }
    
    public ConfluenceSpaceInputParam getConfluenceSpace(String serverIP){
    	return ConversionUtil.Con2ConfluenceSpace(this,serverIP);
    }
    
    public ConfluenceSpaceInputParam getConfluenceSpace(){
    	return ConversionUtil.Con2ConfluenceSpace(this);
    }

    public static class Links implements Serializable{

        private static final long serialVersionUID = -8538674241579986349L;
        private String webui;

        public String getWebui() {
            return webui;
        }

        public void setWebui(String webui) {
            this.webui = webui;
        }

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        private String self;

        @Override
        public String toString() {
            return "Links [webui=" + webui + ", self=" + self + "]";
        }

    }

    public static class Expandable implements Serializable{

        private static final long serialVersionUID = 5750185285584531176L;
        private String metadata;
        private String icon;
        private String description;
        private String homepage;

        public String getMetadata() {
            return metadata;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        @Override
        public String toString() {
            return "Expandable [metadata=" + metadata + ", icon=" + icon + ", description=" + description
                    + ", homepage=" + homepage + "]";
        }

    }

    @Override
    public String toString() {
        return "GenericConfluenceSpaceDTO [id=" + id + ", key=" + key + ", name=" + name + ", type=" + type
                + ", _links=" + _links + ", _expandable=" + _expandable + "]";
    }

}

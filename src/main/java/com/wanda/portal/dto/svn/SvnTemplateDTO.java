package com.wanda.portal.dto.svn;

import java.io.Serializable;
/*
 * {
    "templates": [
        {
            "id": 2,
            "name": "Create standard trunk/branches/tags structure"
        },
        {
            "id": 1,
            "name": "Empty repository"
        },
        {
            "id": 10,
            "name": "HookIt-Naked"
        },
        {
            "id": 11,
            "name": "HookIt-TrunkBranchTag"
        }
    ]
}
 * */
public class SvnTemplateDTO implements Serializable {

	private static final long serialVersionUID = -1553605022881616599L;
	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "SvnTemplateDTO [id=" + id + ", name=" + name + "]";
	}

}

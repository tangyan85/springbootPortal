package com.wanda.portal.dto.svn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SvnTemplateWrapperDTO implements Serializable {

	private static final long serialVersionUID = 8460040486093534433L;
	private List<SvnTemplateDTO> templates = new ArrayList<>();
	public List<SvnTemplateDTO> getTemplates() {
		return templates;
	}
	public void setTemplates(List<SvnTemplateDTO> templates) {
		this.templates = templates;
	}
	@Override
	public String toString() {
		return "SvnTemplateWrapperDTO [templates=" + templates + "]";
	}

}

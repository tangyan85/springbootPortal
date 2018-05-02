jQuery(document).ready(function () {
    //var actionJira=jQuery(".actionJira").children('option:selected').val();
    
    var jiraProjectKey = jQuery("#jiraProjectKey").val();
    if (!isEmpty(jiraProjectKey)) {
        jQuery("#jiraStep").find(".actionJira,#serverIP").attr("disabled", true);
        jQuery("#jiraStep").find(".selectJiraRow").hide();
    }

    jQuery("#confluenceSpaceTable>.list-item_copy>tr").each(function () {
        var spaceKey = $(this).find("#spaceKey").val();
        if (!isEmpty(spaceKey)) {
            $(this).find(".actionConfluence,.selectConfluence,#serverIP").attr("disabled", true);
        }
    });

    jQuery("#sCMRepoTable>.list-item_copy>tr").each(function () {
        var repoName = $(this).find("#repoName").val();
        if (!isEmpty(repoName)) {
            $(this).find(".actionSvns,#serverIP,#selectSvns,#selectSvnTemplats").attr("disabled", true);
        }
    });

    jQuery("#jenkinsProjectTable>.list-item_copy>tr").each(function () {
        var jenkinsProjKey = $(this).find("#jenkinsProjKey").val();
        if (!isEmpty(jenkinsProjKey)) {
            $(this).find(".actionJenkins,#serverIP,.selectJenkins").attr("disabled", true);
        }
    });
});
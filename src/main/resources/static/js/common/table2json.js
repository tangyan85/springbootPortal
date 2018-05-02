function tableToJson(table) {
    var table = $(table);
    var data = [];

    // first row needs to be headers
    var headers = [];
    for (var i = 0; i < table[0].rows[0].cells.length; i++) {
        var isIngore = "" || table[0].rows[0].cells[i].dataset.ingore;
        if (isIngore && isIngore == 'true') {
            continue;
        }
        headers[i] = table[0].rows[0].cells[i].id;
    }

    // go through cells
    for (var i = 1; i < table[0].rows.length; i++) {
        var tableRow = table[0].rows[i];
        var rowData = {};
        for (var j = 0; j < headers.length; j++) {
            //var childLength=tableRow.cells[j].children().length;
            if (headers[j] && headers[j] != "") {
                if (tableRow.cells[j].children && tableRow.cells[j].children.length > 0) {
                    rowData[headers[j]] = tableRow.cells[j].children[0].value || "";
                } else {
                    rowData[headers[j]] = tableRow.cells[j].innerHTML;
                }
            }
        }
        data.push(rowData);
    }

    return data;
}

function tableToJson2(table) {
    var table = $(table);
    var data = [];


    // go through cells
    for (var i = 1; i < table[0].rows.length; i++) {
        var tableRow = table[0].rows[i];
        var ingoreTarget=tableRow.dataset.ingoreTarget;

        if (ingoreTarget == null || ingoreTarget == "" || ingoreTarget == "undefind") {
            
        }else{
            var target=$(tableRow).find("#"+ingoreTarget).val();
            if (target == null || target == "" || target == "undefind") {
                continue;
            }
        }
        
        var rowData = {};
        for (var j = 0; j < tableRow.cells.length; j++) {
            if (tableRow.cells[j].children && tableRow.cells[j].children.length > 0) {
                var ingore = tableRow.cells[j].children[0].dataset.ingore;
                //var ingore1 = tableRow.cells[j].children[0].data("ingore");
                var name = tableRow.cells[j].children[0].name;
                if (ingore &&ingore == "true") {
                    continue;
                }
                if (name == null || name == "" || name == "undefind") {
                    continue;
                }
                rowData[name] = tableRow.cells[j].children[0].value || "";
            } else {
                var name = tableRow.cells[j].name;
                if (name == null || name == "" || name == "undefind") {
                    continue;
                }
                rowData[tableRow.cells[j].name] = tableRow.cells[j].innerHTML;
            }
        }
        data.push(rowData);
    }

    return data;
}
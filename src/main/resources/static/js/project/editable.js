/*
 * cloud he
 * 1.0.0 2018/5/28
 */
$(function () {
    $.fn.handleTable = function (options) {
        let c = $.extend({
            "operatePos": -1,
            "handleFirst": false,
            "add": "添加",
            "del": "删除",
            "copyBefore": function(data) {

            },
            "copyAfter": function (data) {

            }
        }, options);
        let colsNum = $(this).find('tr').last().children().length;
        if (c.operatePos === -1) {
            c.operatePos = colsNum - 1;
        }
        let rows = $(this).find('tr');
        if (!c.handleFirst) {
            rows = rows.not(":eq(0)");
        }
        let rowsTd = [];
        let allTd = rows.children();
        for (let i = c.operatePos; i <= allTd.length; i += colsNum) {
            if (c.handleFirst) {
                allTd.eq(i).html("");
            }
            rowsTd.push(allTd.eq(i)[0]);
        }
        let addLink = "", delLink = "";
        initLink();
        initFunc(rowsTd);

        function createLink(str) {
            return "<a href=\"javascript:void(0)\">" + str + "</a>";
        }

        function initLink() {
            addLink = createLink(c.add);
            delLink = createLink(c.del);
        }

        function initFunc(cols) {
            for (let i = 0; i < cols.length; i++) {
                if (i === 0) {
                    createAdd(cols[i]);
                } else {
                    createDel(cols[i]);
                }
            }
        }

        function createAdd(operateCol) {
            $(addLink).appendTo(operateCol).on("click", function () {
                let lastRow = $(this).parents('table').find('tr:last');
                c.copyBefore(lastRow);
                let copyRow = lastRow.clone();
                let ltd = copyRow.find('td:last');
                ltd.html("");
                copyRow.find("input,select").val("");
                lastRow.after(copyRow);
                c.copyAfter(copyRow);
                createDel(ltd);
            });
        }

        function createDel(operateCol) {
            $(operateCol).attr('style', 'vertical-align: middle;');
            $(delLink).appendTo(operateCol).on("click", function () {
                let size = $(this).parents('table').find('tr').length;
                if (size > 2) {
                    $(this).parents('tr').remove();
                }
            });
        }

    };
});
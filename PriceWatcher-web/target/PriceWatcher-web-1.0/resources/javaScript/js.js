/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Element */

var url = window.location;
var anchor = url.hash; //anchor with the # character  
var anchor2 = url.hash.substring(1); //anchor without the # character


function getScreenDimensions() {
    document.getElementById('menu_form:sreenH').setAttribute("value", window.innerHeight);
    document.getElementById('menu_form:sreenW').setAttribute("value", window.innerWidth);
}

function getScreenDimensions_in() {
    document.getElementById('menu_form_in:sreenH').setAttribute("value", window.innerHeight);
    document.getElementById('menu_form_in:sreenW').setAttribute("value", window.innerWidth);
}

function getClientCert() {
    document.getElementById('menu_form_lg:cert').setAttribute("value", anchor2);
}

function liveSroll() {
    var div = document.getElementById('live_converse');
    div.scrollTop = div.scrollHeight;
}

function busyness(state) {
    if (state === "begin") { // turn on busy indicator
        document.activeElement.style.cursor = 'wait';
        document.body.style.cursor = 'wait';
    } else { // turn off busy indicator, on either "complete" or "success"
        document.activeElement.style.cursor = 'auto';
        document.body.style.cursor = 'auto';
    }
    updateTableColumnMinWidths();
}

window.addEventListener("DOMContentLoaded", function (e) {
    var form_being_submitted = false;
    var checkForm = function (e) {
        var form = e.target.form;
        if (form_being_submitted) {
            alert("The form is being submitted, please wait a moment...");
            form.submit_button.disabled = true;
            e.preventDefault();
            return;
        }
        //form.submit_button.value = "Submitting form...";
        form_being_submitted = true;
    };
    //var resetForm = function (e) {
        //var form = e.target.form;
        //form.submit_button.disabled = false;
        //form.submit_button.value = "Submit";
        //form_being_submitted = false;
    //};
    document.getElementById("form").addEventListener("submit", checkForm, false);
    //document.getElementById("reset_button").addEventListener("click", resetForm, false);
}, false);

//------------------------------------------------------------------------------------

//addEventListener("click", function() {
//    var el = document.documentElement , rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;
//    rfs.call(el);
//});

/**
 * ***************************************
 * Page height
 */
function setPageDimension() {
    document.getElementById('contentModule').setAttribute("style", "overflow-style: move; margin-top: 1px;");
}

function updateTableColumnMinWidths() {
    var header = $('div.ui-datatable-scrollable-header-box').find('table');
    var body = $('div.ui-datatable-scrollable-body').find('table');
    //Get the larger width of the table header and body
    $(body).width($(header).width());
    var columnHeaders = $(header).find('th');
    // set the minimum width for each column to the larger of the header and body
    for (var j = 0; j < columnHeaders.length; j++) {
        var headerCol = columnHeaders[j];
        var headerColClone = $("[id=\'" + $(headerCol).attr('id') + "_clone\']")[0];
        $(headerColClone).css({"min-width": ($(headerCol).outerWidth()) + "px"});
        $(headerCol).css({"min-width": ($(headerCol).outerWidth()) + "px"});
    }
}
function runDbOwlizer(base_url) {

    Noty.closeAll(); // Closes all notifications
    var myModal = $('#progressModal').modal({
            dismissible: false // Modal can be dismissed by clicking outside of the modal
        }
    );

    myModal.modal('open');

    var driver = $("#Input_driver").val();
    var schema = $("#Input_schema").val();
    var host = $("#Input_host").val();
    var port = $("#Input_port").val();
    var dbName = $("#Input_dbName").val();
    var username = $("#Input_userName").val();
    var password = $("#Input_password").val();
    $.ajax({
        type: "POST",
        url: base_url + "dbowlizer/run",
        dataType: "json",
        data: 'driver=' + driver + '&schema=' + schema + '&host=' + host + '&port=' + port + '&dbName=' + dbName + '&username=' + username + '&password=' + password,
        'success': function (result) {
            myModal.modal('open');
            if (result) {
                if (result["status"] === 'success') {
                    myModal.modal('close');
                    topNoty('success', 'Successfully finished creating ontologies!');
                    url = base_url + "dbowlizer/listOntologies";
                    setTimeout('window.location = url', 3000);
                }
                else {
                    myModal.modal('close');
                    $.each(result["errorMessageList"], function (index, error) {
                        topNoty('warning', error);
                    });
                }
            }
            else
                topNoty('error', 'An error has ocurred.');
        }
    });
}

$(function () {
    $("#Input_driver").on("change", function () {
        var driver = this.value;
        if (driver === 'postgress') {
            $('#schemaRow').removeClass('hide');
        }
        else
            $('#schemaRow').addClass('hide');
    });
});

function fillWithDefaultValues() {
    document.getElementById("Input_host").value = "ilinkbeta.cybershare.utep.edu";
     document.getElementById("Lbl_host").className += "active";
    document.getElementById("Input_userName").value = "employees";
    document.getElementById("Lbl_userName").className += "active";
    document.getElementById("Input_password").value = "emp@@ilink";
    document.getElementById("Lbl_password").className += "active";
    document.getElementById("Input_port").value = "3306";
    document.getElementById("Lbl_port").className += "active";
    document.getElementById("Input_dbName").value = "employees";
    document.getElementById("Lbl_dbName").className += "active";
    document.getElementById("Input_driver").value = "mysql";
    $('#Input_driver').material_select();
}

$(function () {
    $('#Btn_defaultValues').click(function () {
        $("Input_host").prop("value", "lol");
    });
});

// Noty
function topNoty(type, text) {

    Noty.setMaxVisible(10);
    new Noty({
        type: type,
        layout: 'topCenter',
        theme: 'mint',
        text: text,
        timeout: 2500,
        progressBar: true,
        closeWith: ['click'],
        animation: {
            open: 'noty_effects_open',
            close: 'noty_effects_close'
        },
        id: false,
        force: false,
        killer: false,
        queue: 'global',
        container: false,
        buttons: [],
        sounds: {
            sources: [],
            volume: 1,
            conditions: []
        },
        titleCount: {
            conditions: []
        },
        modal: false
    }).show();
}


//Materialize Css Initializations
$(document).ready(function () {
    $('select').material_select();
});

$(document).ready(function () {
    // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
    $('.modal').modal();
});

$(document).ready(function() {
    new Clipboard('.btn');
} );

'use strict';

$('#frmChangePassword').on("keyup keypress", function (e) {
    //disable unwanted submits via 'Enter'
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

$('form').on('submit', function (e) {
    // Since we are not using the standard functionality for HTML-form, 
    // we have to prevent default behavior
    return false;
});

$('#frmChangePassword').validate({
    rules: {
        txtNewPassword: "required",
        txtNewPasswordConfirm: {
            equalTo: "#txtNewPassword"
        }
    }
});

function DisplayServerResponseAsync_Succeed(jqXHR, textStatus, errorThrown, successMessage) {

    $('#successMessage b ~ span').replaceWith(successMessage);
    $('#pnlSuccessfulServerResponse').removeClass('hidden');
}

function DisplayServerResponseAsync_Failed(jqXHR, textStatus, errorThrown, failMessage) {
    //error
    $('#errorMessage').replaceWith(failMessage);

    $('#errorMessageJqXHRStatus b ~ span').replaceWith(jqXHR.status);
    $('#errorMessageTextStatus b ~ span').replaceWith(textStatus);
    $('#errorMessageErrorThrown b ~ span').replaceWith(errorThrown);

    $('#pnlUnsuccessfulServerResponse').removeClass('hidden');
}

function ChangePasswordAsync(idFromQS, tokenFromQS, languageFromQS, newPassword) {

    var successMessage = "Your password were successfully changed!";
    var failMessage = "Something went wrong!";

    var backendServiceURL = "/api/user/" + idFromQS + "/password";
    var jsonData = JSON.stringify({ "token"       : tokenFromQS,
                                    "newPassword" : newPassword });

    $.ajax({
        type: "POST",
        url: backendServiceURL,
        contentType: 'application/json; charset=utf-8',
        data: jsonData,
        dataType: 'json'
    })
        .done(function (data, textStatus, jqXHR) {
            DisplayServerResponseAsync_Succeed(data, textStatus, jqXHR, successMessage);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            DisplayServerResponseAsync_Failed(jqXHR, textStatus, errorThrown, failMessage);
        })
        .always(function () {
            $('#secFrmResetPassword').addClass('hidden');
        });
}

$('#btnSaveChanges').on('click', function () {

    if ($('#frmChangePassword').valid()) {
        // let's see what we got from query string
        var idFromQS = $.getQueryString("id");
        var tokenFromQS = $.getQueryString("token");
        var languageFromQS = $.getQueryString("language");
        var newPassword =  $('#txtNewPassword').val();


        ChangePasswordAsync(idFromQS, tokenFromQS, languageFromQS, newPassword);
    }
});
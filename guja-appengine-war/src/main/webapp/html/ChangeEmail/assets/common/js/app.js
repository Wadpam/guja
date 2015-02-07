'use strict';
function HideServerResponseTextPanels() {
    $('#pnlSuccessfulServerResponse').addClass('hidden');
    $('#pnlUnsuccessfulServerResponse').addClass('hidden');
}

function DisplayVerifyEmailAsync_Succeed(jqXHR, textStatus, errorThrown, successMessage) {

    $('#successMessage b ~ span').replaceWith(successMessage);
    $('#pnlSuccessfulServerResponse').removeClass('hidden');
}

function DisplayVerifyEmailAsync_Failed(jqXHR, textStatus, errorThrown, failMessage) {
    //error
    $('#errorMessage').replaceWith(failMessage);

    $('#errorMessageJqXHRStatus b ~ span').replaceWith(jqXHR.status);
    $('#errorMessageTextStatus b ~ span').replaceWith(textStatus);
    $('#errorMessageErrorThrown b ~ span').replaceWith(errorThrown);

    $('#pnlUnsuccessfulServerResponse').removeClass('hidden');
}

function VerifyEmailAsync() {
    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");
    var tokenFromQS = $.getQueryString("token");
    var languageFromQS = $.getQueryString("language");

    var successMessage;
    var failMessage;
    successMessage = "We have successfully received your data!";
    failMessage = "Something went wrong!";

    var backendServiceURL = "/api/user/" + idFromQS + "/email/confirm";
    var jsonData = JSON.stringify({ "token" : tokenFromQS });

    $.ajax({
        type: "POST",
        url: backendServiceURL,
        contentType: 'application/json; charset=utf-8',
        data: jsonData,
        dataType: 'json'
    }).done(function (data, textStatus, jqXHR) {
        DisplayVerifyEmailAsync_Succeed(data, textStatus, jqXHR, successMessage);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        DisplayVerifyEmailAsync_Failed(jqXHR, textStatus, errorThrown, failMessage);
    }).always(function () {
        //
    });
}

$(function () {
    //Okay, we have loaded DOM
    VerifyEmailAsync();
});

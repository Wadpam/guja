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

function ResendConfirmationAsync() {
    var successMessage;
    var failMessage;

    successMessage = "Please check your e-mail";
    failMessage = "Failed resending verification email!";

    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");

    var backendServiceURL = "/api/user/" + idFromQS + "/account/resend";
    $.ajax({
        type: "POST",
        url: backendServiceURL
    }).done(function (data, textStatus, jqXHR) {
        DisplayVerifyEmailAsync_Succeed(data, textStatus, jqXHR, successMessage);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        DisplayVerifyEmailAsync_Failed(jqXHR, textStatus, errorThrown, failMessage);
    }).always(function () {
        //
    });
}

function VerifyAccountAsync() {
    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");
    var tokenFromQS = $.getQueryString("token");
    var languageFromQS = $.getQueryString("language");

    var successMessage;
    var failMessage;
    successMessage = "We have successfully activated your account!";
    failMessage = "Something went wrong!";

    var backendServiceURL = "/api/user/" + idFromQS + "/account/confirm";
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

$('#btnResendConfirmation').on('click', function () {
    HideServerResponseTextPanels();
    ResendConfirmationAsync();
});

$(function () {
    //Okay, we have loaded DOM
    VerifyAccountAsync();
});

'use strict';

var translations = {};
translations["default"] = {
                    successTitle :"Your account has been activated!",
                    successDescription : "Go back to your app and sign in to start enjoying XXX.",
                    failureTitle : "Ops, something went wrong!",
                    failureDescription : "Try resending the activation email. Make sure you use the activation link in the email right away.",
                    resendFailureDescription : "We are not able to send you are new activation email. Open you app and try to register once more. ",
                    resendSuccessTitle : "We have sent you a new email!",
                    resendSuccessDescription : "Go to your inbox and look for the activation email."
                  };
translations["sv"] = {
                    successTitle : "Ditt konto har aktiverats!",
                    successDescription :"Öppna din app och logga in för att börja använda XXX.",
                    failureTitle : "Ops, något gick fel!",
                    failureDescription : "Försöka skicka om ditt aktiverings mejl. Använd aktiveringslänken i mejlet omgående.",
                    resendFailureDescription : "Vi kunde inte skicka ett nytt mejl. Öppna appen och registrar dig på nytt.",
                    resendSuccessTitle : "Vi har skickat ett nytt mejl",
                    resendSuccessDescription : "Gå till din inbox och leda efter aktiverings mejlet."
                  };

function HideServerResponseTextPanels() {
    $('#success').addClass('hidden');
    $('#failure').addClass('hidden');
}

function DisplaySuccess(jqXHR, textStatus, errorThrown, language) {

    var title = translate("successTitle", language);
    var description = translate("successDescription", language);

    $('#success-title').html(title);
    $('#success-description').html(description);

    $('#success').removeClass('hidden');
}


function translate(token, language) {
    var languageTranslations = translations[language];
    if (!languageTranslations) {
      languageTranslations = translations["default"];
    }
    return languageTranslations[token];
}


function DisplayFailure(jqXHR, textStatus, errorThrown, language) {

    var title = translate("failureTitle", language);
    var description = translate("failureDescription", language);

    $('#failure-title').html(title);
    $('#failure-description').html(description);

    $('#failure').removeClass('hidden');
}

function DisplayResendSuccess(jqXHR, textStatus, errorThrown, language) {

    var title = translate("resendTitle", language);
    var description = translate("resendDescription", language);

    $('#failure-title').html(title);
    $('#failure-description').html(description);

    $('#failure').removeClass('hidden');
}


function DisplayResendFailure(jqXHR, textStatus, errorThrown, language) {

    var title = translate("failureTitle", language);
    var description = translate("resendFailureDescription", language);

    $('#failure-title').html(title);
    $('#failure-description').html(description);

    $('#resend-button').attr("disabled", "disabled");
}


function ResendConfirmationAsync(language) {

    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");

    var backendServiceURL = "/api/user/" + idFromQS + "/account/resend";
    $.ajax({
        type: "POST",
        url: backendServiceURL
    }).done(function (data, textStatus, jqXHR) {
        DisplayResendSuccess(data, textStatus, jqXHR, language);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        DisplayResendFailure(jqXHR, textStatus, errorThrown, language);
    }).always(function () {
        //
    });
}


function VerifyAccountAsync() {

    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");
    var tokenFromQS = $.getQueryString("token");
    var languageFromQS = $.getQueryString("language");

    var backendServiceURL = "/api/user/" + idFromQS + "/account/confirm";
    var jsonData = JSON.stringify({ "token" : tokenFromQS });

    $.ajax({
        type: "POST",
        url: backendServiceURL,
        contentType: 'application/json; charset=utf-8',
        data: jsonData,
        dataType: 'json'
    }).done(function (data, textStatus, jqXHR) {
        DisplaySuccess(data, textStatus, jqXHR, languageFromQS);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        DisplayFailure(jqXHR, textStatus, errorThrown, languageFromQS);
    }).always(function () {
        //
    });
}


$('#resend-button').on('click', function () {
    ResendConfirmationAsync();
});


$(function () {
    //Okay, we have loaded DOM
    VerifyAccountAsync();
});


'use strict';

var translations = {};
translations["default"] = {
                    successTitle :"Your email has been changed!",
                    successDescription : "Go back to your app and continue enjoying XXX.",
                    failureTitle : "Ops, something went wrong!",
                    failureDescription : "Go back to your app and try again. Make sure you use the confirmation email right away.",
                  };
translations["sv"] = {
                    successTitle : "Din mejladress har ändrats!",
                    successDescription :"Öppna din app och försätt använda XXX",
                    failureTitle : "Ops, något gick fel!",
                    failureDescription : "Öppna din app och försök igen. Använd aktiveringslänken i mejlet omgående.",
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


function VerifyEmailAsync() {
    // let's see what we got from query string
    var idFromQS = $.getQueryString("id");
    var tokenFromQS = $.getQueryString("token");
    var languageFromQS = $.getQueryString("language");

    var backendServiceURL = "/api/user/" + idFromQS + "/email/confirm";
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

$(function () {
    //Okay, we have loaded DOM
    VerifyEmailAsync();
});


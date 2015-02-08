'use strict';


var translations = {};
translations["default"] = {
                    title : "Reset Password",
                    newPassword : "New Password",
                    confirmPassword : "Confirm Password",
                    reset : "Reset",
                    successTitle : "Your password has been changed!",
                    successDescription : "Go back to your app and sign in with your new password.",
                    failureTitle : "Ops, something went wrong!",
                    failureDescription : "Go back to your app and try again. Make sure you use the confirmation email right away.",
                  };
translations["sv"] = {
                    title : "Återställ Lösenord",
                    newPassword : "Nytt Password",
                    confirmPassword : "Repetera Password",
                    reset : "Återställ",
                    successTitle : "Ditt lösenord har ändrats!",
                    successDescription :"Öppna din app och logga in med ditt nya lösenord.",
                    failureTitle : "Ops, något gick fel!",
                    failureDescription : "Öppna din app och försök igen. Använd aktiveringslänken i mejlet omgående.",
                  };


$('#reset-password-button').on("keyup keypress", function (e) {
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


$('#change-password-form').validate({
    rules: {
        newPassword: {
          required: true,
          minlength: 5
        },
        confirmPassword: {
            equalTo: "#newPassword"
        }
    }
});


function DisplaySuccess(jqXHR, textStatus, errorThrown, language) {

    var title = translate("successTitle", language);
    var description = translate("successDescription", language);

    $('#result-title').html(title);
    $('#result-description').html(description);

    $('#change-password-form').addClass('hide');
    $('#result').removeClass('hidden');

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

    $('#result-title').html(title);
    $('#result-description').html(description);

    $('#change-password-form').addClass('hide');
    $('#result').removeClass('hidden');
}

function ChangePasswordAsync(idFromQS, tokenFromQS, languageFromQS, newPassword) {

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
        .done(function (data, textStatus, jqXHR, languageFromQS) {
            DisplaySuccess(data, textStatus, jqXHR);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            DisplayFailure(jqXHR, textStatus, errorThrown, languageFromQS);
        })
        .always(function () {
            //
        });
}


function localizeValidationMessages(language) {
  if (language == "sv") {
    messages_sv();
  }
}


function messages_sv() {
  $.extend($.validator.messages, {
    required: "Detta f&auml;lt &auml;r obligatoriskt.",
    maxlength: $.validator.format("Du f&aring;r ange h&ouml;gst {0} tecken."),
    minlength: $.validator.format("Du m&aring;ste ange minst {0} tecken."),
    rangelength: $.validator.format("Ange minst {0} och max {1} tecken."),
    email: "Ange en korrekt e-postadress.",
    url: "Ange en korrekt URL.",
    date: "Ange ett korrekt datum.",
    dateISO: "Ange ett korrekt datum (&Aring;&Aring;&Aring;&Aring;-MM-DD).",
    number: "Ange ett korrekt nummer.",
    digits: "Ange endast siffror.",
    equalTo: "Ange samma v&auml;rde igen.",
    range: $.validator.format("Ange ett v&auml;rde mellan {0} och {1}."),
    max: $.validator.format("Ange ett v&auml;rde som &auml;r mindre eller lika med {0}."),
    min: $.validator.format("Ange ett v&auml;rde som &auml;r st&ouml;rre eller lika med {0}."),
    creditcard: "Ange ett korrekt kreditkortsnummer."
  });
}


$('#reset-password-button').on('click', function () {

    var idFromQS = $.getQueryString("id");
    var tokenFromQS = $.getQueryString("token");
    var languageFromQS = $.getQueryString("language");
    var newPassword =  $("#newPassword").val();

    localizeValidationMessages(languageFromQS);

    if ($('#change-password-form').valid()) {
        ChangePasswordAsync(idFromQS, tokenFromQS, languageFromQS, newPassword);
    }

});

var language = $.getQueryString("language");
$('#reset-password-button').html(translate("reset", language));
$('#title').html(translate("title", language));
$('#newPassword').attr("placeholder", translate("newPassword", language));
$('#confirmPassword').attr("placeholder", translate("confirmPassword", language));
$('#change-password-form').removeClass("hidden");
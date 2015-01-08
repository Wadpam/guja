var ACCESS_TOKEN = "access_token";

function registerFederated(apiUrl, basicUsername, basicPassword, providerId) {
    var token = getParameterByName("access_token");
    var jConnection;

    //call to register
    $.ajax({
            async: false,
            type: "GET",
            url: apiUrl + "/federated",
            data:{
                    access_token: token,
                    providerId: providerId,
                    expires_in: getParameterByName("expires_in")
            },
            dataType: "json",
            username: basicUsername,
            password: basicPassword,
            error: function(jqXHR, textStatus, errorThrown){
                    console.log("register fail...")
            },
            success: function(data, textStatus, jqXHR){
                    jConnection = data;
                    jConnection.statusCode = jqXHR.status;
                    console.log("Register success " + jConnection);
            },
            complete: function(jqXHR, textStatus){
                    console.log(textStatus);
            }
    });
    return jConnection;
}


function crud_Create(url, body) {
    return $.ajax(url, {
        contentType: "application/json",
        data: JSON.stringify(body),
        type: "POST"
    });
}

function crud_Read(url, id) {
    return $.getJSON(url + '/' + id);
}

function crud_Update(url, id, body) {
    return $.ajax(url + "/" + id, {
        contentType: "application/json",
        data: JSON.stringify(body),
        type: "POST"
    });
}

function crud_Delete(url, id) {
    return $.ajax(url + "/" + id, {
        type: "DELETE"
    });
}

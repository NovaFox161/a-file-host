function changeUsername() {
    var bodyRaw = {
        "username": document.getElementById("username").value
    };

    $.ajax({
        url: "/api/v1/account/update/username",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (jqXHR) {
            showSnackbar(jqXHR.responseText);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}

function changeEmail() {
    var bodyRaw = {
        "email": document.getElementById("email").value
    };

    $.ajax({
        url: "/api/v1/account/update/email",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (jqXHR) {
            showSnackbar(jqXHR.responseText);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}
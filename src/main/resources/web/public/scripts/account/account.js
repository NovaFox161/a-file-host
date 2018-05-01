function changeUsername() {
    var bodyRaw = {
        "username": document.getElementById("change-username-username").value,
        "password": document.getElementById("change-username-password").value
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
            document.getElementById("change-username-password").value = "";
            $('#modal-change-username').modal('hide');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}

function changeEmail() {
    var bodyRaw = {
        "email": document.getElementById("change-email-email").value,
        "password": document.getElementById("change-email-password").value
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
            document.getElementById("change-email-password").value = "";
            $('#modal-change-email').modal('hide');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}
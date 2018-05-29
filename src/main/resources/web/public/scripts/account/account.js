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

function changePassword() {
    var bodyRaw = {
        "old": document.getElementById("change-password-old-password").value,
        "new": document.getElementById("change-password-new-password").value
    };

    $.ajax({
        url: "/api/v1/account/update/password",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (jqXHR) {
            showSnackbar(jqXHR.responseText);
            document.getElementById("change-password-old-password").value = "";
            document.getElementById("change-password-new-password").value = "";
            document.getElementById("change-password-confirm-password").value = "";

            $('#modal-change-password').modal('hide');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}

function validatePassword() {
    var password = document.getElementById("change-password-new-password");
    var confirm_password = document.getElementById("change-password-confirm-password");

    if (password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
        confirm_password.setCustomValidity('');
    }
}
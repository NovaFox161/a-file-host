function validatePassword() {
    var password = document.getElementById("password");
    var confirm_password = document.getElementById("password-confirm");

    if (password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

function onSubmit() {
    var bodyRaw = {
        "email": document.getElementById("email").value,
        "username": document.getElementById("username").value,
        "password": document.getElementById("password").value,
        "gcap": grecaptcha.getResponse()
    };

    $.ajax({
        url: "/api/v1/account/register",
        headers: {
            "Authorization": "REGISTER_ACCOUNT",
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (data) {
            window.location.replace("/account");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
            grecaptcha.reset();
        }
    });
}
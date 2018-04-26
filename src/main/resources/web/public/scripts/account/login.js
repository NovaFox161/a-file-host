function onSubmit() {
    var bodyRaw = {
        "email": document.getElementById("email").value,
        "password": document.getElementById("password").value,
        "gcap": grecaptcha.getResponse()
    };

    $.ajax({
        url: "/api/v1/account/login",
        headers: {
            "Authorization": "LOGIN_ACCOUNT",
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
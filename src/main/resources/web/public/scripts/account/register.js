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

function checkValidity(input) {
    if (input.value !== document.getElementById("password").value) {
        input.setCustomValidity("Passwords must match!");
    } else {
        input.setCustomValidity("");
    }
}
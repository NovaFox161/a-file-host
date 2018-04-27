function onSubmit() {
    var bodyRaw = {
        "subject": document.getElementById("subject").value,
        "message": document.getElementById("message").value,
        "name": document.getElementById("name").value,
        "email": document.getElementById("email").value,
        "gcap": grecaptcha.getResponse()
    };

    $.ajax({
        url: "/api/v1/contact",
        headers: {
            "Authorization": "CONTACT",
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (data) {
            document.getElementById("contact").hidden = true;
            showSnackbar("Email Sent! Thank you for contacting us!");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
            grecaptcha.reset();
        }
    });
}
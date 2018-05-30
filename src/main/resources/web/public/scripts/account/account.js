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

function getAPIKeys() {
    var bodyRaw = {};

    $.ajax({
        url: "/api/v1/account/key/get-all",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (json) {
            var table = document.getElementById("key-table-body");

            while (table.firstChild) {
                table.removeChild(table.firstChild);
            }

            //Add headers
            var mr = document.createElement("tr");
            mr.style.height = "50px";
            table.appendChild(mr);
            var kh = document.createElement("th");
            kh.innerHTML = "Key";
            mr.appendChild(kh);
            var uh = document.createElement("th");
            uh.innerHTML = "Uses";
            mr.appendChild(uh);
            var dh = document.createElement("th");
            dh.innerHTML = "Delete";
            mr.appendChild(dh);


            for (var i = 0; i < json.count; i++) {
                var key = json.keys[i];

                var row = document.createElement("tr");
                row.id = key.key;
                table.appendChild(row);

                var keyCol = document.createElement("td");
                keyCol.innerHTML = key.key;
                row.appendChild(keyCol);

                var useCol = document.createElement("td");
                useCol.innerHTML = key.uses;
                row.appendChild(useCol);

                var delCol = document.createElement("td");
                delCol.innerHTML = "Delete Key";
                delCol.id = "delete=" + key.key;
                delCol.onclick = function (ev) {
                    deleteAPIKey(this.id.split("=")[1]);
                };
                row.appendChild(delCol);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);
        }
    });
}

function createAPIKey() {
    var bodyRaw = {};

    $.ajax({
        url: "/api/v1/account/key/create",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (json) {
            showSnackbar("New API Key Successfully Created!");

            getAPIKeys();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);

            getAPIKeys();
        }
    });
}

function deleteAPIKey(key) {
    var bodyRaw = {
        "key": key
    };

    $.ajax({
        url: "/api/v1/account/key/delete",
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        dataType: "json",
        data: JSON.stringify(bodyRaw),
        success: function (json) {
            showSnackbar("Successfully deleted API Key!");

            getAPIKeys();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showSnackbar(jqXHR.responseText);

            getAPIKeys();
        }
    });
}
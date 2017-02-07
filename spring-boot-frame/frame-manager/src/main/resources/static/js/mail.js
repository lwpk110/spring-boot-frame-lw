define(function(){
    var Mail = {};

    Mail.defaults = {
        idCorp: 1,
        corpKey: "CDHGGBEH6B89D1GH",
        actionType: "login",
        action: "http://tendata.mailsou.com/it/",
        method: "post",
        target: "_self"
    };

    var createForm = function () {
        var form, hidden, idCorp, corpKey, actionType;
        form = document.createElement("form");
        form.action = Mail.defaults.action;
        form.method = Mail.defaults.method;
        form.target = Mail.defaults.target;

        idCorp = createHiddenEl("idCorp", Mail.defaults.idCorp);
        form.appendChild(idCorp);

        corpKey = createHiddenEl("corpKey", Mail.defaults.corpKey);
        form.appendChild(corpKey);

        actionType = createHiddenEl("actionType", Mail.defaults.actionType);
        form.appendChild(actionType);

        return form;
    };

    var form;

    Mail.login = function (name, pwd, target) {
        if (form) {
            document.body.removeChild(form);
        }

        form = createForm();

        var nameEl = createHiddenEl("idCst", name);
        form.appendChild(nameEl);

        var pwdEl = createHiddenEl("stPwd", pwd);
        form.appendChild(pwdEl);

        var subURL = createHiddenEl("subURL", target);
        form.appendChild(subURL);

        document.body.appendChild(form);

        form.submit();
    };

    function createHiddenEl(name, value) {
        var hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = hidden.id = name;
        hidden.value = value;
        return hidden;
    }

    window.Mail = Mail;
})
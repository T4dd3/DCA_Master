function callback(xhr)
{
    if (xhr.readyState === 2)
    {
        document.getElementById('richiediCodice').disabled = true;
        document.getElementById('username').disabled = true;
        document.getElementById('password').disabled = true;
        document.getElementById('email').disabled = true;
        document.getElementById('siglaFiat').disabled = true;
        document.getElementById('apiKey').disabled = true;
        document.getElementById('apiSecret').disabled = true;
        document.getElementById('tipoDeposito').disabled = true;
    }

    if (xhr.readyState === 4)
    {
        if (xhr.status === 200)
        {

            document.getElementById('codice').disabled = false;
        }

        else alert('Errore!');
    }
}

function myFunction()
{
    xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function(){callback(xhr);};

    try
    {
        xhr.open("POST", "/DCA_Master/request", true);
    }
    catch(e)
    {
        alert(e);
    }

            username = document.getElementById('username').value;
            password = document.getElementById('password').value;
            email = document.getElementById('email').value;
            siglaFiat = document.getElementById('siglaFiat').value;
            apiKey = document.getElementById('apiKey').value;
            apiSecret = document.getElementById('apiSecret').value;
            tipoDeposito = document.getElementById('tipoDeposito').value;

            var argument = "username=" + username + "&password=" + password + "&email=" + email + "&siglaFiat=" + siglaFiat + "&apiKey=" + apiKey + "&apiSecret=" + apiSecret + "&tipoDeposito=" + tipoDeposito + "&richiediCodice=true";


            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhr.send(argument);
}
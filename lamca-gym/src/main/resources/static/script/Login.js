document.addEventListener('DOMContentLoaded', function() {
    // Växla mellan inloggning och registrering
    document.getElementById('toggleToLogin').addEventListener('click', function() {
        document.getElementById('signupForm').style.display = 'none';
        document.getElementById('loginForm').style.display = 'block';
    });

    document.getElementById('toggleToSignup').addEventListener('click', function() {
        document.getElementById('loginForm').style.display = 'none';
        document.getElementById('signupForm').style.display = 'block';
    });

    // Hantera inloggning
    document.getElementById('loginForm').addEventListener('submit', async function(event) {
        event.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;
        await loginUser(email, password);
    });

    // Hantera registrering
    document.getElementById('signupForm').addEventListener('submit', async function(event) {
        event.preventDefault();
        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const fullName = document.getElementById('signupFullName').value;
        
        if (isValidPassword(password)) {
            await registerUser(email, password, fullName);
        } else {
            alert('Lösenordet måste innehålla minst 5 tecken, inklusive en stor bokstav och en siffra.');
            document.getElementById('signupPassword').value = ''; // Rensar lösenordsfältet
        }
    });
});

function loginUser() {
    var email = document.getElementById("loginEmail").value;
    var password = document.getElementById("loginPassword").value;

    // Skicka en förfrågan till din backend för att kontrollera inloggningen
    fetch('login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Invalid login credentials');
        }
        return response.text(); // Hämta texten från svaret
    })
    .then(data => {
        console.log(data); // Logga svaret från servern
        // Om inloggningen är framgångsrik, omdirigera till dashboard-sidan
        window.location.href = "personalpage.html";
    })
    .catch(error => {
        console.error('Error logging in:', error);
        // Visa felmeddelande på sidan om inloggningen misslyckades
        document.getElementById("message").innerText = "Invalid email or password";
    });
}
/*
async function loginUser(email, password) {
    try {
        const loginUrl = 'http://localhost:8080/login'; // Ändra till din Spring Boot-apps URL
        const loginData = { email, password };

        const response = await fetch(loginUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            alert('Inloggning lyckades!');
            const data = await response.json();
            localStorage.setItem('userToken', data.token); // Spara token, antar att backend svarar med en token
            window.location.href = 'personalpage.html'; // Eller vart du nu vill dirigera användaren
        } else {
            alert('Inloggning misslyckades.');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('Ett fel inträffade vid inloggning.');
    }*/
async function registerUser(email, password, fullName) {
    try {
        const registerUrl = 'http://localhost:8080/register'; // Ändra till din Spring Boot-apps URL
        const registerData = { email, password, fullName };

        const response = await fetch(registerUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(registerData)
        });

        const data = await response.json();
        if (response.ok) {
            alert('Registrering lyckades! Du kan nu logga in.');
            window.location.href = '/frontend/personalpage.html';
        } else {
            alert(data.message || 'Registrering misslyckades');
            // Rensa formulärfält vid fel
        }
    } catch (error) {
        console.error('Registration error:', error);
        alert('Ett fel inträffade vid registrering.');
    }
}

function isValidPassword(password) {
    return /^(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{5,}$/.test(password);
}




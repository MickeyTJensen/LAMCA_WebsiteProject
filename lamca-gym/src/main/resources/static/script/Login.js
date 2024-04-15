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
})

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    loginUser(email, password);
});

function loginUser(email, password) {
    fetch('/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.json();
    })
    .then(data => {
    console.log(data);
            localStorage.setItem('userToken', data.token);
            console.log("Sätter userId i localStorage:", data.userId);
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('userName', data.name);
            alert('Login lyckades! Du kommer nu att omdirigeras.');
            window.location.href = "/personalpage.html";
        })
        .catch(error => {
            console.error('Login error:', error);
            const loginMessageElement = document.getElementById("loginMessage");
            if (loginMessageElement) {
                // Använder serverns felmeddelande om tillgängligt
                loginMessageElement.innerText = error || "Invalid email or password";
            } else {
                console.error('Login message element not found');
            }
        });
    }


    // Hantera registrering
    document.getElementById('signupForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const name = document.getElementById('signupFullName').value; // Bytt variabelnamn från fullName till name för att matcha serverns förväntan
        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const phoneNumber = document.getElementById('signupPhoneNumber').value; // Anta att du har ett fält med ID 'signupPhoneNumber'

        registerUser(name, email, phoneNumber, password);
    });

    async function registerUser(name, email, phoneNumber, password) {
        const registerUrl = 'http://localhost:8080/user/register'; // Säkerställ att detta är korrekt URL
        const registerData = {
            name,
            email,
            phoneNumber,
            password
        };

        try {
            const response = await fetch(registerUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(registerData)
            });

            if (!response.ok) {
                const data = await response.json();
                alert(data.message || 'Registrering misslyckades, försök igen.');
            } else {
                alert('Registrering lyckades!');
                window.location.href = '/personalpage.html';
            }
        } catch (error) {
            console.error('Registration error:', error);
            alert('Ett fel inträffade vid registrering.');
        }
    }


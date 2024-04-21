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
    document.getElementById('loginForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;
        loginUser(email, password);
    });
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
        if (data.message === 'Successfully login') {
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('userName', data.name);
            alert('Login successful! You will now be redirected.');
            window.location.href = "/personalpage.html";
        } else {
            throw new Error(data.message);
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        const loginMessageElement = document.getElementById("loginMessage");
        if (loginMessageElement) {
            loginMessageElement.innerText = error.message || "Invalid email or password";
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
        const registerUrl = 'http://localhost:8080/user/register'; // Ensure this is the correct URL
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

            const data = await response.json(); // Always parse the JSON to access the data object
            //alert(data)
            if (!response.ok) {
                //alert(data.message || 'Registrering misslyckades, försök igen.');
                alert(data)
            } else {
                console.log("Registration successful, setting user details in localStorage:", data.userId);
                localStorage.setItem('userToken', data.token);  // Assuming token is part of the response
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('userName', data.name);
                alert('Registration successful! You will now be redirected..');
                redirectToLogin();
            }
        } catch (error) {
            console.error('Registration error:', error);
            alert('An error occurred during registration.');
        }
    }
    function redirectToLogin() {
           // Dirigera användaren till inloggningssidan
           document.getElementById('signupForm').style.display = 'none';
           document.getElementById('loginForm').style.display = 'block';
    }


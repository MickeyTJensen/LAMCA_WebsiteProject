document.addEventListener('DOMContentLoaded', () => {
    const userName = localStorage.getItem('userName');
    const loginLink = document.getElementById('loginLink');
    const personalPageLink = document.getElementById('personalPageLink');
    const logoutLink = document.getElementById('logoutLink');

    if (userName) {
        // Användaren är inloggad
        personalPageLink.textContent = `Welcome, ${userName}`; // Uppdatera texten till "Welcome, [userName]"
        personalPageLink.style.display = "inline";
        logoutLink.style.display = "inline";
        loginLink.style.display = "none"; // Dölj inloggning/register-länken
    } else {
        // Användaren är inte inloggad
        personalPageLink.style.display = "none";
        logoutLink.style.display = "none";
        loginLink.style.display = "inline"; // Visa inloggning/register-länken
    }

    // Visa "Logga ut" länken om användaren är inloggad
    if (userName) {
        if (logoutLink) {
            logoutLink.style.display = "inline"; // Visa länken
            logoutLink.onclick = logout; // Tilldela utloggning till klickhändelsen
        }
    }
});

// Visa "Logga ut" länken om användaren är inloggad
const isLoggedIn = localStorage.getItem('userName'); // Kontrollera om användaren är inloggad

if (isLoggedIn) {
    const logoutLink = document.getElementById("logoutLink");
    if (logoutLink) { // Kontrollera om länken finns på sidan
        logoutLink.style.display = "inline"; // Visa länken
        logoutLink.onclick = logout; // Tilldela utloggning till klickhändelsen
    }
}

// Funktion för utloggning
function logout() {
    // Ta bort användarnamnet från localStorage
    localStorage.removeItem('userName');
    // Ta bort inloggningsstatusen från localStorage
    localStorage.removeItem('isLoggedIn');
    // Omdirigera användaren till inloggningssidan
    window.location.href = "http://127.0.0.1:5501/frontend/index.html"; // Eller var du vill leda användaren efter utloggning
}

document.addEventListener('DOMContentLoaded', () => {
    // Hämta sökvägsdelen av den nuvarande sidans URL
    const currentPath = window.location.pathname;

    // Hämta alla navigationslänkar
    const navLinks = document.querySelectorAll('.navbar a');

    // Iterera över alla länkar
    navLinks.forEach(link => {
        // Ta bort 'active-link' från alla länkar
        link.classList.remove('active-link');

        // Hämta länktexten
        const linkText = link.textContent.trim();

        // Exkludera "Logout"-länken baserat på länktexten
        if (linkText === 'Logga ut') {
            return;
        }

        // Skapa en ny URL baserat på länkens href-attribut för att hantera relativa och absoluta URL:er korrekt
        const linkPath = new URL(link.href, window.location.origin).pathname;

        // Jämför länkens sökväg med den nuvarande sidans sökväg
        // och markera länken som aktiv om de matchar
        if (linkPath === currentPath) {
            link.classList.add('active-link');
        }
    });
});


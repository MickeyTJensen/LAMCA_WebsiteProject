function updateNavbar() {
    const userName = localStorage.getItem('userName');
    if (userName) {
        // Användaren är inloggad, visa välkomstmeddelande och göm "Logga in"/"Registrera" länken
        document.getElementById('loginRegisterLinks').style.display = 'none';
        const welcomeUserLink = document.getElementById('personalPageLink');
        if (welcomeUserLink) {
            welcomeUserLink.textContent += userName; // Lägg till användarnamnet till texten
            document.getElementById('welcomeUser').style.display = 'block';
        }
        document.getElementById('logoutContainer').style.display = 'block'; // Visa logga ut länken
    } else {
        // Användaren är inte inloggad
        document.getElementById('loginRegisterLinks').style.display = 'block';
        document.getElementById('welcomeUser').style.display = 'none';
        document.getElementById('logoutContainer').style.display = 'none'; // Göm logga ut länken
    }
}

document.getElementById('logoutLink')?.addEventListener('click', function(e) {
    e.preventDefault();
    localStorage.removeItem('userToken');
    localStorage.removeItem('userName');
    window.location.href = 'login.html'; // Omdirigera till inloggningssidan
});

// Kalla på funktionen när sidan laddas
updateNavbar();

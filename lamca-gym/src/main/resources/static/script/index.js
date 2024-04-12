document.getElementById('background-video').addEventListener('click', function() {
    // Spela/pausa videon vid klick
    if (this.paused) {
      this.play();
      this.classList.remove('muted');
    } else {
      this.pause();
      this.classList.add('muted');
    }

    // Fadea ut logotypen och fadea in texten efter att videon klickats
    fadeContent();
});

function fadeContent() {
    var logoImg = document.querySelector('#welcome .logo-img');
    var textContent = document.querySelector('#welcome .text-content');

    // Använd CSS transition för att fadea ut logotypen
    logoImg.style.opacity = 0;

    // Sätt en fördröjning på textens opacitetsändring
    setTimeout(() => {
        textContent.style.opacity = 1;
    }, 1000); // Justera tiden baserat på övergångens hastighet

    // Ändra z-index efter att övergången börjat
    logoImg.style.zIndex = 0;
    textContent.style.zIndex = 1;
}

window.addEventListener('DOMContentLoaded', (event) => {
    const video = document.getElementById('background-video');
    video.play(); // Spela videon automatiskt när sidan laddas

    document.body.addEventListener('click', () => {
        video.style.display = 'none'; // Dölj videon när användaren klickar någonstans på sidan
    });
});


document.addEventListener('DOMContentLoaded', function () {
    const animateElements = document.querySelectorAll('.animate-from-left');
    const windowHeight = window.innerHeight;

    function checkPosition() {
        animateElements.forEach(element => {
            const positionFromTop = element.getBoundingClientRect().top;

            if (positionFromTop - windowHeight <= -windowHeight / 3) {
                element.classList.add('in-view');
            }
        });
    }

    window.addEventListener('scroll', checkPosition);
    checkPosition(); // Kör vid laddning ifall elementet redan är i viewport
});




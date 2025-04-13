document.addEventListener('DOMContentLoaded', function() {
    createParticles();
    initEventListeners();
});

function initEventListeners() {
    document.getElementById('originalUrl').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') shortenUrl();
    });

    const statsLink = document.getElementById('statsLink');
    if (statsLink) {
        statsLink.addEventListener('click', function(e) {
            e.preventDefault();
            const shortUrl = document.getElementById('shortUrl').value.split('/').pop();
            if (shortUrl) window.location.href = `/stats/${shortUrl}`;
        });
    }
}

function createParticles() {
    const container = document.querySelector('.particles-container');
    const particleCount = Math.floor(window.innerWidth / 20);

    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.classList.add('particle');

        const size = Math.random() * 3 + 1;
        const posX = Math.random() * 100;
        const posY = Math.random() * 100;
        const duration = Math.random() * 20 + 10;
        const delay = Math.random() * 5;

        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;
        particle.style.left = `${posX}%`;
        particle.style.top = `${posY}%`;
        particle.style.animation = `float ${duration}s linear ${delay}s infinite`;

        container.appendChild(particle);
    }
}

async function shortenUrl() {
    const originalUrl = document.getElementById('originalUrl').value.trim();
    const resultDiv = document.getElementById('result');
    const shortUrlOutput = document.getElementById('shortUrl');

    if (!originalUrl) {
        showError('Please enter a URL');
        return;
    }

    try {
        const response = await fetch('/shorten', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `url=${encodeURIComponent(originalUrl)}`
        });

        if (!response.ok) throw new Error('Network response was not ok');

        const shortUrl = await response.text();
        const fullShortUrl = `${window.location.origin}/${shortUrl}`;

        shortUrlOutput.value = fullShortUrl;
        resultDiv.classList.remove('hidden');

        // Update stats link
        const statsLink = document.getElementById('statsLink');
        if (statsLink) statsLink.href = `/stats/${shortUrl}`;

    } catch (error) {
        console.error('Error:', error);
        showError('Failed to shorten URL. Please try again.');
    }
}

function copyToClipboard() {
    const shortUrlInput = document.getElementById('shortUrl');
    shortUrlInput.select();
    document.execCommand('copy');

    const copyButton = document.querySelector('.short-url-container button');
    copyButton.innerHTML = '<i class="fas fa-check"></i> Copied!';
    setTimeout(() => {
        copyButton.innerHTML = '<i class="far fa-copy"></i> Copy';
    }, 2000);
}

function showQRCodePanel() {
    const shortUrl = document.getElementById('shortUrl').value.split('/').pop();
    const qrCodeContainer = document.getElementById('qrCodeContainer');
    const qrCodeImage = document.getElementById('qrCodeImage');

    qrCodeImage.src = `/qr/${shortUrl}?width=200&height=200`;
    qrCodeContainer.classList.remove('hidden');
}

function hideQRCodePanel() {
    document.getElementById('qrCodeContainer').classList.add('hidden');
}

function downloadQRCode() {
    const shortUrl = document.getElementById('shortUrl').value.split('/').pop();
    const link = document.createElement('a');
    link.href = `/qr/${shortUrl}?width=400&height=400`;
    link.download = `qr-code-${shortUrl}.png`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function isValidUrl(string) {
    try {
        new URL(string);
        return true;
    } catch (_) {
        return false;
    }
}

function showError(message) {
    let errorBox = document.getElementById('error-notification');
    if (!errorBox) {
        errorBox = document.createElement('div');
        errorBox.id = 'error-notification';
        errorBox.style.position = 'fixed';
        errorBox.style.bottom = '20px';
        errorBox.style.left = '50%';
        errorBox.style.transform = 'translateX(-50%)';
        errorBox.style.backgroundColor = 'var(--error)';
        errorBox.style.color = 'white';
        errorBox.style.padding = '1rem 2rem';
        errorBox.style.borderRadius = '4px';
        errorBox.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
        errorBox.style.zIndex = '1000';
        errorBox.style.animation = 'fadeIn 0.3s ease-in-out';
        document.body.appendChild(errorBox);
    }

    errorBox.textContent = message;

    setTimeout(() => {
        errorBox.style.animation = 'fadeOut 0.3s ease-in-out';
        setTimeout(() => errorBox.remove(), 300);
    }, 3000);
}

const style = document.createElement('style');
style.textContent = `
    @keyframes fadeIn {
        from { opacity: 0; transform: translateX(-50%) translateY(20px); }
        to { opacity: 1; transform: translateX(-50%) translateY(0); }
    }
    @keyframes fadeOut {
        from { opacity: 1; transform: translateX(-50%) translateY(0); }
        to { opacity: 0; transform: translateX(-50%) translateY(20px); }
    }
`;
document.head.appendChild(style);
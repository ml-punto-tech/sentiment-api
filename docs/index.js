// ============================================================================
// 1. CONFIGURACIÓN Y CONSTANTES
// ============================================================================
const SENTIMENT_CONFIG = {
    positivo: {
        emoji: "😊",
        label: "Positivo",
        description: "El texto expresa emociones positivas",
        badgeClass: "sentiment-value sentiment-positive",
        progressClass: "progress-bar bg-positive",
        gradientClass: "gradient-positive",
        textClass: "text-positive",
    },
    negativo: {
        emoji: "😞",
        label: "Negativo",
        description: "El texto expresa emociones negativas",
        badgeClass: "sentiment-value sentiment-negative",
        progressClass: "progress-bar bg-negative",
        gradientClass: "gradient-negative",
        textClass: "text-negative",
    },
    neutral: {
        emoji: "😐",
        label: "Neutral",
        description: "El texto no expresa emociones fuertes",
        badgeClass: "sentiment-value sentiment-neutral",
        progressClass: "progress-bar bg-neutral",
        gradientClass: "gradient-neutral",
        textClass: "text-neutral"
    }
};

const MIN_CARACTERES = 10;
const API_URL = 'https://sentiment-tech-api.onrender.com/api/v1/sentiment';
// const API_URL = 'https://sentiment-tech-api.onrender.com/api/v1/sentiment';

// ============================================================================
// 2. ELEMENTOS DEL DOM
// ============================================================================
const elementos = {
    textarea: document.getElementById('text'),
    submitBtn: document.getElementById('submit-button'),
    contador: document.getElementById('characters-counter'),
    warnText: document.getElementById('warn'),
    main1: document.getElementById('main-one'),
    main2: document.getElementById('main-two'),
    text: document.getElementById("text-in-result"),
    emoji: document.getElementById('emoji'),
    previsionDiv: document.getElementById('prevision'),
    progressBar: document.getElementById('progress-bar'),
    probabilityText: document.getElementById('prob-value'),
    returnBtn: document.getElementById('return-button'),
    sentimentDescription: document.getElementById('description')
};

// Inicialización
elementos.submitBtn.disabled = true;

// ============================================================================
// 3. FUNCIONES UTILITARIAS
// ============================================================================
function mostrarAdvertencia(mensaje, mostrar = true) {
    elementos.warnText.textContent = mensaje;
    elementos.warnText.className = mostrar ? 'warn-text' : 'occult';
}

function navegarA(pantalla) {
    if (pantalla === 'formulario') {
        elementos.main1.className = 'container';
        elementos.main2.classList.add('occult');
    } else if (pantalla === 'resultado') {
        elementos.main1.className = 'occult';
        elementos.main2.classList.remove('occult');
    }
}

function actualizarUI(sentimentData, probabilidad, texto) {
    const config = SENTIMENT_CONFIG[sentimentData.toLowerCase()];
    if (!config) return;

    elementos.text.textContent = `'${texto}'`;
    elementos.text.className = config.textClass;
    elementos.previsionDiv.textContent = config.label;
    elementos.previsionDiv.className = config.badgeClass;
    elementos.progressBar.className = config.progressClass;
    elementos.progressBar.style.width = `${probabilidad}%`;
    elementos.progressBar.style.setProperty('--progress-value', `${probabilidad}%`);
    elementos.probabilityText.textContent = `${probabilidad}%`;
    elementos.emoji.textContent = config.emoji;
    elementos.sentimentDescription.textContent = config.description;
}

// ============================================================================
// 4. EVENT LISTENERS
// ============================================================================
elementos.returnBtn.addEventListener('click', () => {
    navegarA('formulario');
});

// Validación en tiempo real del textarea
elementos.textarea.addEventListener('input', function() {
    const texto = this.value.trim();
    const longitud = texto.length;

    // Contador de caracteres
    elementos.contador.textContent = `${longitud} caracteres`;

    // Validación mínima
    const valido = longitud > MIN_CARACTERES;
    elementos.submitBtn.disabled = !valido;

    // Mensajes informativos
    if (longitud > 0 && longitud <= MIN_CARACTERES) {
        mostrarAdvertencia('Escribe más de 10 caracteres para analizar');
    } else {
        mostrarAdvertencia('', false);
    }
});

// Envío del formulario
elementos.submitBtn.addEventListener('click', async function(e) {
    e.preventDefault();

    const texto = elementos.textarea.value.trim();

    if (texto.length <= MIN_CARACTERES) {
        mostrarAdvertencia('El mensaje debe tener más de 10 caracteres');
        return;
    }

    // Estado loading
    elementos.submitBtn.disabled = true;
    mostrarAdvertencia('Enviando mensaje...');

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ text: texto })
        });

        const data = await response.json();

        if (response.ok) {
            const probabilidad = Math.round(data.data.probabilidad * 100);
            actualizarUI(data.data.prevision, probabilidad, texto);
            navegarA('resultado');
            elementos.textarea.value = ''; // Limpiar formulario
            mostrarAdvertencia('', false);
        } else {
            throw new Error(data.message || `Error ${response.status}`);
        }

    } catch (error) {
        console.error('Error:', error);
        mostrarAdvertencia(`❌ Error: ${error.message}`);
    } finally {
        elementos.submitBtn.disabled = false;
    }
});

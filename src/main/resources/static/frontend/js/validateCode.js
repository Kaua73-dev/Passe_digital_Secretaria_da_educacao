const validateCodeForm = document.getElementById('validateCodeForm');
const codeInput = document.getElementById('code');
const statusMessage = document.getElementById('statusMessage');
const submitButton = document.getElementById('submitButton');

function showStatus(message, isError = true) {
  statusMessage.textContent = message;
  statusMessage.classList.toggle('text-red-600', isError);
  statusMessage.classList.toggle('text-emerald-600', !isError);
}

validateCodeForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  showStatus('', true);

  const code = codeInput.value.trim().toUpperCase();
  if (!/^[0-9A-Z]{6}$/.test(code)) {
    showStatus('Informe um código válido de 6 caracteres (0-9, A-Z).');
    return;
  }

  codeInput.value = code;
  submitButton.disabled = true;
  submitButton.textContent = 'Validando...';

  const apiBaseUrl = (typeof Auth !== 'undefined' && Auth.getApiBaseUrl)
    ? Auth.getApiBaseUrl()
    : 'http://localhost:8080';

  try {
    const response = await fetch(`${apiBaseUrl}/code/validate`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ code })
    });

    let data = {};
    try {
      data = await response.json();
    } catch (parseError) {
      const text = await response.text().catch(() => '');
      data = { message: text };
    }

    if (!response.ok) {
      throw new Error(data.message || 'Não foi possível validar o código.');
    }

    if (!data.uuid) {
      throw new Error('Resposta inválida do servidor.');
    }

    localStorage.setItem('resetPasswordUUID', data.uuid);
    window.location.href = 'change-password.html';
  } catch (error) {
    showStatus(error.message || 'Erro ao validar o código.');
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Validar código';
  }
});

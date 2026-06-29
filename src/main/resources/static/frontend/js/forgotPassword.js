const forgotPasswordForm = document.getElementById('forgotPasswordForm');
const registrationInput = document.getElementById('registration');
const statusMessage = document.getElementById('statusMessage');
const submitButton = document.getElementById('submitButton');

function showStatus(message, isError = true) {
  statusMessage.textContent = message;
  statusMessage.classList.toggle('text-red-600', isError);
  statusMessage.classList.toggle('text-emerald-600', !isError);
}

forgotPasswordForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  showStatus('', true);

  const registration = registrationInput.value.trim();
  if (!registration) {
    showStatus('Informe sua matrícula para continuar.');
    return;
  }

  submitButton.disabled = true;
  submitButton.textContent = 'Enviando...';

  const apiBaseUrl = (typeof Auth !== 'undefined' && Auth.getApiBaseUrl)
    ? Auth.getApiBaseUrl()
    : 'http://localhost:8080';

  try {
    const response = await fetch(`${apiBaseUrl}/email/send`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ registration })
    });

    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
      throw new Error(data.message || 'Não foi possível enviar o código.');
    }

    window.location.href = 'validate-code.html';
  } catch (error) {
    showStatus(error.message || 'Erro ao enviar o código.');
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Enviar código';
  }
});

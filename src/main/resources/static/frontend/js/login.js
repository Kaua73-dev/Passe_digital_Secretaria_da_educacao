const loginForm = document.getElementById('loginForm');
const registrationInput = document.getElementById('registration');
const passwordInput = document.getElementById('password');
const statusMessage = document.getElementById('statusMessage');
const submitButton = document.getElementById('submitButton');

function showStatus(message, isError = true) {
  statusMessage.textContent = message;
  statusMessage.classList.toggle('text-red-600', isError);
  statusMessage.classList.toggle('text-emerald-600', !isError);
}

const urlParams = new URLSearchParams(window.location.search);
const successMessage = urlParams.get('message');
if (successMessage) {
  showStatus(successMessage, false);
}

loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  showStatus('', true);

  const registration = registrationInput.value.trim();
  const password = passwordInput.value;

  if (!registration || !password) {
    statusMessage.textContent = 'Preencha todos os campos para continuar.';
    return;
  }

  submitButton.disabled = true;
  submitButton.textContent = 'Carregando...';

  try {
    await Auth.login(registration, password);
    Auth.redirectToApp();
  } catch (error) {
    statusMessage.textContent = error.message || 'Erro inesperado ao fazer login.';
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Entrar';
  }
});

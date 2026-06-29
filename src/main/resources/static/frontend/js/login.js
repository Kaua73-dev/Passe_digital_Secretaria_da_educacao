const loginForm = document.getElementById('loginForm');
const registrationInput = document.getElementById('registration');
const passwordInput = document.getElementById('password');
const statusMessage = document.getElementById('statusMessage');
const submitButton = document.getElementById('submitButton');

if (Auth.isAuthenticated()) {
  Auth.redirectToApp();
}

loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  statusMessage.textContent = '';

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
    window.location.href = "../../html/studentHomePage.html";
  } catch (error) {
    statusMessage.textContent = error.message || 'Erro inesperado ao fazer login.';
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Entrar';
  }
});

const changePasswordForm = document.getElementById('changePasswordForm');
const newPasswordInput = document.getElementById('newPassword');
const confirmPasswordInput = document.getElementById('confirmPassword');
const statusMessage = document.getElementById('statusMessage');
const submitButton = document.getElementById('submitButton');

function showStatus(message, isError = true) {
  statusMessage.textContent = message;
  statusMessage.classList.toggle('text-red-600', isError);
  statusMessage.classList.toggle('text-emerald-600', !isError);
}

changePasswordForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  showStatus('', true);

  const newPassword = newPasswordInput.value.trim();
  const confirmPassword = confirmPasswordInput.value.trim();
  const uuid = localStorage.getItem('resetPasswordUUID');

  if (!newPassword || !confirmPassword) {
    showStatus('Preencha todos os campos.');
    return;
  }

  if (newPassword !== confirmPassword) {
    showStatus('As senhas não coincidem.');
    return;
  }

  if (!uuid) {
    showStatus('UUID de redefinição não encontrado. Volte ao início do fluxo.');
    return;
  }

  submitButton.disabled = true;
  submitButton.textContent = 'Alterando...';

  const apiBaseUrl = (typeof Auth !== 'undefined' && Auth.getApiBaseUrl)
    ? Auth.getApiBaseUrl()
    : 'http://localhost:8080';

  try {
    const response = await fetch(`${apiBaseUrl}/auth/password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token: uuid, newPassword })
    });

    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
      throw new Error(data.message || 'Não foi possível alterar a senha.');
    }

    localStorage.removeItem('resetPasswordUUID');
    window.location.href = 'login.html?message=' + encodeURIComponent('Senha alterada com sucesso.');
  } catch (error) {
    showStatus(error.message || 'Erro ao alterar a senha.');
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Alterar senha';
  }
});
